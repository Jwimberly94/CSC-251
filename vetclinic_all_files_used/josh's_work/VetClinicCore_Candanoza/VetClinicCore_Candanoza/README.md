# Veterinary Clinic Management System - Core Logic

## Overview
This is the core business logic layer for the veterinary clinic management system. It handles all data management, validation, and business rules without any GUI dependencies.

## Architecture

### Core Classes

#### 1. **VetClinicManager.java** (Main Controller)
The central hub for all business logic operations.

**Key Features:**
- Manages appointments, services, and veterinarians
- Handles all CRUD operations
- Validates business rules
- Provides CSV file I/O for data persistence
- Generates reports and statistics

**Main Methods:**
```java
// Appointment Management
createAppointment(petName, ownerName, phone, time, service, vet)
cancelAppointment(appointmentId)
rescheduleAppointment(appointmentId, newTime)
updateAppointmentStatus(appointmentId, status)
getUpcomingAppointments()
getAppointmentsByDate(date)
getAppointmentsByVet(vetName)
isTimeSlotAvailable(time, vetName)
getAvailableTimeSlots(date, vetName)

// Service Management
addService(service)
removeService(serviceId)
getAllServices()
findServiceByName(serviceName)

// Veterinarian Management
addVeterinarian(vet)
removeVeterinarian(vetId)
getAllVeterinarians()
getAvailableVeterinarians(time)

// Data Persistence
saveAllData()  // Saves to CSV files
loadAllData()  // Loads from CSV files
```

#### 2. **Appointment.java**
Represents a single veterinary appointment.

**Properties:**
- appointmentId (unique identifier)
- petName
- ownerName
- ownerPhone
- appointmentTime
- serviceType
- veterinarian
- status (SCHEDULED, CHECKED_IN, IN_PROGRESS, COMPLETED, CANCELLED, NO_SHOW)
- notes

**Features:**
- CSV import/export
- Status tracking
- Notes management

#### 3. **Service.java**
Represents veterinary services offered.

**Properties:**
- serviceId
- serviceName
- durationMinutes
- price
- description

**Default Services Included:**
- Checkup ($50, 30 min)
- Vaccination ($35, 20 min)
- Surgery ($500, 120 min)
- Dental Cleaning ($150, 60 min)
- Grooming ($60, 45 min)
- Emergency ($200, 60 min)

#### 4. **Veterinarian.java**
Represents a veterinarian with their schedule.

**Properties:**
- vetId
- name
- specialization
- phone
- email
- weeklySchedule (Map of DayOfWeek to Schedule)

**Features:**
- Weekly schedule management
- Availability checking
- Day-off management

#### 5. **VetClinicDemo.java**
Demonstration program showing how to use all the core functionality.

## Data Storage

All data is stored in CSV files:
- `appointments.csv` - All appointment records
- `services.csv` - Available services
- `veterinarians.csv` - Veterinarian information

Files are created in the same directory as the program.

## Integration Guide

### For GUI Team (Joshua):

#### Basic Setup:
```java
public class VetClinicGUI {
    private VetClinicManager manager;
    
    public VetClinicGUI() {
        manager = new VetClinicManager();
        
        // Load existing data on startup
        try {
            manager.loadAllData();
        } catch (IOException e) {
            System.err.println("Error loading data: " + e.getMessage());
        }
    }
}
```

#### Creating an Appointment:
```java
// Get form data from GUI
String petName = petNameField.getText();
String ownerName = ownerNameField.getText();
String ownerPhone = phoneField.getText();
LocalDateTime time = getSelectedDateTime(); // from date/time picker
String service = serviceComboBox.getSelectedItem();
String vet = vetComboBox.getSelectedItem();

try {
    Appointment apt = manager.createAppointment(
        petName, ownerName, ownerPhone, time, service, vet
    );
    
    // Save to file
    manager.saveAllData();
    
    // Update GUI
    showSuccess("Appointment created: " + apt.getAppointmentId());
    refreshAppointmentList();
    
} catch (IllegalArgumentException e) {
    showError("Invalid input: " + e.getMessage());
} catch (IllegalStateException e) {
    showError("Time slot not available");
} catch (IOException e) {
    showError("Error saving data");
}
```

#### Populating GUI Components:
```java
// Fill veterinarian dropdown
for (Veterinarian vet : manager.getAllVeterinarians()) {
    vetComboBox.addItem(vet.getName());
}

// Fill service dropdown
for (Service service : manager.getAllServices()) {
    serviceComboBox.addItem(service.getServiceName());
}

// Display appointments in table/list
List<Appointment> appointments = manager.getUpcomingAppointments();
for (Appointment apt : appointments) {
    // Add row to table with apt data
    tableModel.addRow(new Object[] {
        apt.getAppointmentId(),
        apt.getPetName(),
        apt.getOwnerName(),
        apt.getAppointmentTime(),
        apt.getServiceType(),
        apt.getVeterinarian(),
        apt.getStatus()
    });
}
```

#### Checking Available Time Slots:
```java
// When user selects a date and veterinarian
LocalDateTime selectedDate = getDateFromPicker();
String selectedVet = vetComboBox.getSelectedItem();

List<LocalDateTime> availableSlots = 
    manager.getAvailableTimeSlots(selectedDate, selectedVet);

// Display available slots in GUI
timeSlotList.clear();
for (LocalDateTime slot : availableSlots) {
    timeSlotList.addItem(slot.format(DateTimeFormatter.ofPattern("hh:mm a")));
}
```

### For Schedule/Services Team (Mariah):

#### Adding a Veterinarian:
```java
Veterinarian newVet = new Veterinarian(
    "V003",
    "Emily Brown",
    "Exotic Animals",
    "555-0103",
    "ebrown@vetclinic.com"
);

// Set work schedule
newVet.setSchedule(DayOfWeek.MONDAY, LocalTime.of(9, 0), LocalTime.of(17, 0));
newVet.setSchedule(DayOfWeek.TUESDAY, LocalTime.of(9, 0), LocalTime.of(17, 0));
// ... set other days
newVet.setDayOff(DayOfWeek.SUNDAY);

manager.addVeterinarian(newVet);
manager.saveAllData();
```

#### Adding/Modifying Services:
```java
// Add new service
Service newService = new Service(
    "S007",
    "X-Ray",
    45,
    125.00,
    "Diagnostic X-Ray imaging"
);
manager.addService(newService);

// Modify existing service
Service checkup = manager.findServiceByName("Checkup");
if (checkup != null) {
    checkup.setPrice(55.00);  // Update price
}

manager.saveAllData();
```

#### Getting Vet Schedule:
```java
Veterinarian vet = manager.findVeterinarianByName("Sarah Johnson");
Map<DayOfWeek, Veterinarian.Schedule> schedule = vet.getWeeklySchedule();

for (DayOfWeek day : DayOfWeek.values()) {
    Veterinarian.Schedule daySchedule = schedule.get(day);
    System.out.println(day + ": " + daySchedule);
}
```

## Error Handling

The core logic throws exceptions for invalid operations:

```java
try {
    manager.createAppointment(...);
} catch (IllegalArgumentException e) {
    // Invalid input (empty name, bad phone, etc.)
} catch (IllegalStateException e) {
    // Business rule violation (time slot taken, etc.)
}

try {
    manager.saveAllData();
} catch (IOException e) {
    // File I/O error
}
```

## Validation Rules

1. **Phone Numbers**: Must be 10 digits (formatting flexible)
2. **Pet/Owner Names**: Cannot be empty
3. **Time Slots**: 30-minute intervals, must be during vet's working hours
4. **Appointment Conflicts**: Same vet cannot have overlapping appointments

## Testing

Run `VetClinicDemo.java` to see all features in action:
```bash
javac VetClinicDemo.java
java VetClinicDemo
```

This will:
- Create sample veterinarians with schedules
- Display available services
- Create test appointments
- Show available time slots
- Demonstrate status updates
- Save/load data from CSV files

## CSV File Format

### appointments.csv
```
AppointmentID,PetName,OwnerName,OwnerPhone,AppointmentTime,ServiceType,Veterinarian,Status,Notes
A0001,Max,John Smith,555-1234,2026-02-07 10:00,Checkup,Sarah Johnson,SCHEDULED,""
```

### services.csv
```
ServiceID,ServiceName,DurationMinutes,Price,Description
S001,Checkup,30,50.00,"General health examination"
```

### veterinarians.csv
```
VetID,Name,Specialization,Phone,Email
V001,Sarah Johnson,General Practice,555-0101,sjohnson@vetclinic.com
```

## Next Steps

1. **Joshua (GUI)**: 
   - Create JFrame/Swing interface
   - Add buttons to call manager methods
   - Display appointments in JTable
   - Use JDatePicker for date selection

2. **Mariah (Schedule/Services)**:
   - Create forms to add/edit veterinarians
   - Build service management interface
   - Create schedule viewer/editor
   - Integrate CSV import/export features

3. **Nicholas (You)**:
   - Test all core functionality
   - Add any missing business logic
   - Help integrate GUI with core logic
   - Add additional validation as needed

## Contact
This core logic is complete and ready for integration. All team members can use the `VetClinicManager` class as the single point of contact for all business operations.
