import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Demo class showing how to use the VetClinicManager core logic
 * This can be used for testing or as a reference for GUI integration
 */
public class VetClinicDemo {
    public static void main(String[] args) {
        System.out.println("=== Veterinary Clinic Management System Demo ===\n");
        
        // Create the manager
        VetClinicManager manager = new VetClinicManager();
        
        // Demo 1: Add veterinarians
        System.out.println("1. Adding Veterinarians...");
        Veterinarian vet1 = new Veterinarian("V001", "Sarah Johnson", "General Practice", 
                                             "555-0101", "sjohnson@vetclinic.com");
        vet1.setSchedule(DayOfWeek.MONDAY, LocalTime.of(9, 0), LocalTime.of(17, 0));
        vet1.setSchedule(DayOfWeek.TUESDAY, LocalTime.of(9, 0), LocalTime.of(17, 0));
        vet1.setSchedule(DayOfWeek.WEDNESDAY, LocalTime.of(9, 0), LocalTime.of(17, 0));
        vet1.setSchedule(DayOfWeek.THURSDAY, LocalTime.of(9, 0), LocalTime.of(17, 0));
        vet1.setSchedule(DayOfWeek.FRIDAY, LocalTime.of(9, 0), LocalTime.of(15, 0));
        manager.addVeterinarian(vet1);
        
        Veterinarian vet2 = new Veterinarian("V002", "Michael Chen", "Surgery Specialist", 
                                             "555-0102", "mchen@vetclinic.com");
        vet2.setSchedule(DayOfWeek.MONDAY, LocalTime.of(10, 0), LocalTime.of(18, 0));
        vet2.setSchedule(DayOfWeek.WEDNESDAY, LocalTime.of(10, 0), LocalTime.of(18, 0));
        vet2.setSchedule(DayOfWeek.FRIDAY, LocalTime.of(10, 0), LocalTime.of(18, 0));
        manager.addVeterinarian(vet2);
        
        System.out.println("Added 2 veterinarians:");
        for (Veterinarian vet : manager.getAllVeterinarians()) {
            System.out.println("  - " + vet);
        }
        System.out.println();
        
        // Demo 2: Display services
        System.out.println("2. Available Services:");
        for (Service service : manager.getAllServices()) {
            System.out.println("  - " + service);
        }
        System.out.println();
        
        // Demo 3: Create appointments
        System.out.println("3. Creating Appointments...");
        try {
            LocalDateTime apt1Time = LocalDateTime.now().plusDays(1).withHour(10).withMinute(0);
            Appointment apt1 = manager.createAppointment(
                "Max", "John Smith", "555-1234", 
                apt1Time, "Checkup", "Sarah Johnson"
            );
            System.out.println("  Created: " + apt1);
            
            LocalDateTime apt2Time = LocalDateTime.now().plusDays(1).withHour(14).withMinute(30);
            Appointment apt2 = manager.createAppointment(
                "Bella", "Jane Doe", "555-5678", 
                apt2Time, "Vaccination", "Sarah Johnson"
            );
            System.out.println("  Created: " + apt2);
            
            LocalDateTime apt3Time = LocalDateTime.now().plusDays(2).withHour(11).withMinute(0);
            Appointment apt3 = manager.createAppointment(
                "Charlie", "Bob Wilson", "555-9012", 
                apt3Time, "Surgery", "Michael Chen"
            );
            System.out.println("  Created: " + apt3);
            
        } catch (Exception e) {
            System.out.println("  Error: " + e.getMessage());
        }
        System.out.println();
        
        // Demo 4: View upcoming appointments
        System.out.println("4. Upcoming Appointments:");
        List<Appointment> upcoming = manager.getUpcomingAppointments();
        for (Appointment apt : upcoming) {
            System.out.println("  - " + apt);
        }
        System.out.println();
        
        // Demo 5: Check available time slots
        System.out.println("5. Available Time Slots Tomorrow for Dr. Sarah Johnson:");
        LocalDateTime tomorrow = LocalDateTime.now().plusDays(1);
        List<LocalDateTime> availableSlots = manager.getAvailableTimeSlots(tomorrow, "Sarah Johnson");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a");
        int count = 0;
        for (LocalDateTime slot : availableSlots) {
            System.out.print("  " + slot.format(formatter) + "  ");
            count++;
            if (count % 4 == 0) System.out.println();
        }
        System.out.println("\n");
        
        // Demo 6: Update appointment status
        System.out.println("6. Updating Appointment Status...");
        if (!upcoming.isEmpty()) {
            Appointment firstApt = upcoming.get(0);
            manager.updateAppointmentStatus(firstApt.getAppointmentId(), 
                                           Appointment.AppointmentStatus.CHECKED_IN);
            System.out.println("  Updated " + firstApt.getAppointmentId() + " to CHECKED_IN");
        }
        System.out.println();
        
        // Demo 7: Get appointments by veterinarian
        System.out.println("7. Appointments for Dr. Sarah Johnson:");
        List<Appointment> vetApts = manager.getAppointmentsByVet("Sarah Johnson");
        for (Appointment apt : vetApts) {
            System.out.println("  - " + apt);
        }
        System.out.println();
        
        // Demo 8: Statistics
        System.out.println("8. Appointment Statistics:");
        var stats = manager.getAppointmentStatistics();
        for (var entry : stats.entrySet()) {
            System.out.println("  " + entry.getKey() + ": " + entry.getValue());
        }
        System.out.println();
        
        // Demo 9: Save data to files
        System.out.println("9. Saving Data to CSV Files...");
        try {
            manager.saveAllData();
            System.out.println("  ✓ Data saved successfully!");
            System.out.println("  Files created:");
            System.out.println("    - appointments.csv");
            System.out.println("    - services.csv");
            System.out.println("    - veterinarians.csv");
        } catch (Exception e) {
            System.out.println("  ✗ Error saving data: " + e.getMessage());
        }
        System.out.println();
        
        // Demo 10: Load data from files
        System.out.println("10. Testing Load from Files...");
        VetClinicManager newManager = new VetClinicManager();
        try {
            newManager.loadAllData();
            System.out.println("  ✓ Data loaded successfully!");
            System.out.println("  Loaded " + newManager.getAllAppointments().size() + " appointments");
            System.out.println("  Loaded " + newManager.getAllServices().size() + " services");
            System.out.println("  Loaded " + newManager.getAllVeterinarians().size() + " veterinarians");
        } catch (Exception e) {
            System.out.println("  ✗ Error loading data: " + e.getMessage());
        }
        
        System.out.println("\n=== Demo Complete ===");
        System.out.println("\nIntegration Tips for GUI Team:");
        System.out.println("1. Create a VetClinicManager instance in your main application");
        System.out.println("2. Call manager.loadAllData() on startup");
        System.out.println("3. Use manager methods to create/update/view appointments");
        System.out.println("4. Call manager.saveAllData() when data changes");
        System.out.println("5. All data is automatically saved to CSV files");
    }
}
