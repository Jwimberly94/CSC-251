// Source: Wimberly's code base
// Merged into: Candanoza's codebase
import java.io.*;
import java.time.LocalDateTime;
import java.time.DayOfWeek;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Main business logic controller for the veterinary clinic
 * Manages appointments, services, and veterinarians
 */
public class VetClinicManager {
    private List<Appointment> appointments;
    private List<Service> services;
    private List<Veterinarian> veterinarians;
    
    private static final String APPOINTMENTS_FILE = "appointments.csv";
    private static final String SERVICES_FILE = "services.csv";
    private static final String VETS_FILE = "veterinarians.csv";
    
    // Source: Wimberly's code base
    // Constructor
    public VetClinicManager() {
        this.appointments = new ArrayList<>();
        this.services = new ArrayList<>();
        this.veterinarians = new ArrayList<>();
        
        initializeDefaultServices();
    }
    
    // ==================== APPOINTMENT MANAGEMENT ====================
    
    /**
     * Creates a new appointment
        * Source: Wimberly's code base
     */
    public Appointment createAppointment(String petName, String ownerName, String ownerPhone,
                                        LocalDateTime appointmentTime, String serviceType, 
                                        String veterinarian) {
        // Validate inputs
        if (petName == null || petName.trim().isEmpty()) {
            throw new IllegalArgumentException("Pet name cannot be empty");
        }
        if (ownerName == null || ownerName.trim().isEmpty()) {
            throw new IllegalArgumentException("Owner name cannot be empty");
        }
        if (!isValidPhoneNumber(ownerPhone)) {
            throw new IllegalArgumentException("Invalid phone number");
        }
        
        // Check if time slot is available
        if (!isTimeSlotAvailable(appointmentTime, veterinarian)) {
            throw new IllegalStateException("Time slot is not available");
        }
        
        // Generate unique ID
        String appointmentId = generateAppointmentId();
        
        // Create appointment
        Appointment appointment = new Appointment(
            appointmentId, petName, ownerName, ownerPhone,
            appointmentTime, serviceType, veterinarian
        );
        
        appointments.add(appointment);
        return appointment;
    }
    
    /**
     * Cancels an appointment
        * Source: Wimberly's code base
     */
    public boolean cancelAppointment(String appointmentId) {
        Appointment apt = findAppointmentById(appointmentId);
        if (apt != null) {
            apt.setStatus(Appointment.AppointmentStatus.CANCELLED);
            return true;
        }
        return false;
    }
    
    /**
     * Reschedules an appointment
        * Source: Wimberly's code base
     */
    public boolean rescheduleAppointment(String appointmentId, LocalDateTime newTime) {
        Appointment apt = findAppointmentById(appointmentId);
        if (apt != null) {
            if (isTimeSlotAvailable(newTime, apt.getVeterinarian())) {
                apt.setAppointmentTime(newTime);
                return true;
            }
        }
        return false;
    }
    
    /**
     * Updates appointment status
        * Source: Wimberly's code base
     */
    public boolean updateAppointmentStatus(String appointmentId, Appointment.AppointmentStatus status) {
        Appointment apt = findAppointmentById(appointmentId);
        if (apt != null) {
            apt.setStatus(status);
            return true;
        }
        return false;
    }
    
    /**
     * Finds appointment by ID
        * Source: Wimberly's code base
     */
    public Appointment findAppointmentById(String appointmentId) {
        return appointments.stream()
            .filter(apt -> apt.getAppointmentId().equals(appointmentId))
            .findFirst()
            .orElse(null);
    }
    
    /**
     * Gets all appointments
        * Source: Wimberly's code base
     */
    public List<Appointment> getAllAppointments() {
        return new ArrayList<>(appointments);
    }

    // Source: Wimberly's code base
    // Legacy accessor kept from original Candanoza model
    public List<Appointment> getAppointments() {
        return appointments;
    }
    
    /**
     * Gets appointments for a specific date
        * Source: Wimberly's code base
     */
    public List<Appointment> getAppointmentsByDate(LocalDateTime date) {
        return appointments.stream()
            .filter(apt -> apt.getAppointmentTime().toLocalDate().equals(date.toLocalDate()))
            .sorted(Comparator.comparing(Appointment::getAppointmentTime))
            .collect(Collectors.toList());
    }
    
    /**
     * Gets appointments for a specific veterinarian
        * Source: Wimberly's code base
     */
    public List<Appointment> getAppointmentsByVet(String vetName) {
        return appointments.stream()
            .filter(apt -> apt.getVeterinarian().equalsIgnoreCase(vetName))
            .sorted(Comparator.comparing(Appointment::getAppointmentTime))
            .collect(Collectors.toList());
    }
    
    /**
     * Gets upcoming appointments
        * Source: Wimberly's code base
     */
    public List<Appointment> getUpcomingAppointments() {
        LocalDateTime now = LocalDateTime.now();
        return appointments.stream()
            .filter(apt -> apt.getAppointmentTime().isAfter(now))
            .filter(apt -> apt.getStatus() == Appointment.AppointmentStatus.SCHEDULED ||
                          apt.getStatus() == Appointment.AppointmentStatus.CHECKED_IN)
            .sorted(Comparator.comparing(Appointment::getAppointmentTime))
            .collect(Collectors.toList());
    }

    // Source: Wimberly's code base
    // Legacy appointment creation flow kept for backwards compatibility
    public Appointment createAppointmentLegacy(String pet, String owner, String phone,
                                              LocalDateTime time, String service, String vet) {
        if (pet == null || pet.isEmpty() || owner == null || owner.isEmpty()) {
            throw new IllegalArgumentException("Pet and Owner required.");
        }

        String id = "A" + (appointments.size() + 1);
        Appointment appointment = new Appointment(id, pet, owner, phone, time, service, vet, "SCHEDULED");
        appointments.add(appointment);
        return appointment;
    }
    
    /**
     * Checks if a time slot is available
          * Source: Candanoza's codebase
     */
    public boolean isTimeSlotAvailable(LocalDateTime time, String vetName) {
        // Check if vet is working at this time
        Veterinarian vet = findVeterinarianByName(vetName);
        if (vet != null) {
            DayOfWeek day = time.getDayOfWeek();
            if (!vet.isAvailable(day, time.toLocalTime())) {
                return false;
            }
        }
        
        // Check if there's already an appointment at this time
        return appointments.stream()
            .filter(apt -> apt.getVeterinarian().equalsIgnoreCase(vetName))
            .filter(apt -> apt.getStatus() == Appointment.AppointmentStatus.SCHEDULED ||
                          apt.getStatus() == Appointment.AppointmentStatus.CHECKED_IN)
            .noneMatch(apt -> {
                LocalDateTime aptTime = apt.getAppointmentTime();
                // Check if times overlap (assuming 30-minute slots)
                return Math.abs(java.time.Duration.between(time, aptTime).toMinutes()) < 30;
            });
    }
    
    /**
     * Gets available time slots for a specific date and vet
          * Source: Candanoza's codebase
     */
    public List<LocalDateTime> getAvailableTimeSlots(LocalDateTime date, String vetName) {
        List<LocalDateTime> availableSlots = new ArrayList<>();
        Veterinarian vet = findVeterinarianByName(vetName);
        
        if (vet == null) {
            return availableSlots;
        }
        
        DayOfWeek day = date.getDayOfWeek();
        Veterinarian.Schedule schedule = vet.getWeeklySchedule().get(day);
        
        if (schedule == null || !schedule.isWorking()) {
            return availableSlots;
        }
        
        // Generate 30-minute time slots
        LocalDateTime slotTime = date.withHour(schedule.getStartTime().getHour())
                                     .withMinute(schedule.getStartTime().getMinute());
        LocalDateTime endTime = date.withHour(schedule.getEndTime().getHour())
                                   .withMinute(schedule.getEndTime().getMinute());
        
        while (!slotTime.isAfter(endTime.minusMinutes(30))) {
            if (isTimeSlotAvailable(slotTime, vetName)) {
                availableSlots.add(slotTime);
            }
            slotTime = slotTime.plusMinutes(30);
        }
        
        return availableSlots;
    }
    
    // ==================== SERVICE MANAGEMENT ====================
    
    /**
     * Adds a new service
          * Source: Candanoza's codebase
     */
    public void addService(Service service) {
        services.add(service);
    }
    
    /**
     * Removes a service
          * Source: Candanoza's codebase
     */
    public boolean removeService(String serviceId) {
        return services.removeIf(s -> s.getServiceId().equals(serviceId));
    }
    
    /**
     * Finds service by name
          * Source: Candanoza's codebase
     */
    public Service findServiceByName(String serviceName) {
        return services.stream()
            .filter(s -> s.getServiceName().equalsIgnoreCase(serviceName))
            .findFirst()
            .orElse(null);
    }
    
    /**
     * Gets all services
          * Source: Candanoza's codebase
     */
    public List<Service> getAllServices() {
        return new ArrayList<>(services);
    }
    
    /**
     * Initializes default services
          * Source: Candanoza's codebase
     */
    private void initializeDefaultServices() {
        services.add(new Service("S001", "Checkup", 30, 50.00, "General health examination"));
        services.add(new Service("S002", "Vaccination", 20, 35.00, "Routine vaccination"));
        services.add(new Service("S003", "Surgery", 120, 500.00, "Surgical procedures"));
        services.add(new Service("S004", "Dental Cleaning", 60, 150.00, "Professional dental cleaning"));
        services.add(new Service("S005", "Grooming", 45, 60.00, "Full grooming service"));
        services.add(new Service("S006", "Emergency", 60, 200.00, "Emergency care"));
    }
    
    // ==================== VETERINARIAN MANAGEMENT ====================
    
    /**
     * Adds a new veterinarian
          * Source: Candanoza's codebase
     */
    public void addVeterinarian(Veterinarian vet) {
        veterinarians.add(vet);
    }
    
    /**
     * Removes a veterinarian
          * Source: Candanoza's codebase
     */
    public boolean removeVeterinarian(String vetId) {
        return veterinarians.removeIf(v -> v.getVetId().equals(vetId));
    }
    
    /**
     * Finds veterinarian by name
          * Source: Candanoza's codebase
     */
    public Veterinarian findVeterinarianByName(String name) {
        return veterinarians.stream()
            .filter(v -> v.getName().equalsIgnoreCase(name))
            .findFirst()
            .orElse(null);
    }
    
    /**
     * Gets all veterinarians
          * Source: Candanoza's codebase
     */
    public List<Veterinarian> getAllVeterinarians() {
        return new ArrayList<>(veterinarians);
    }
    
    /**
     * Gets available veterinarians for a specific time
          * Source: Candanoza's codebase
     */
    public List<Veterinarian> getAvailableVeterinarians(LocalDateTime time) {
        DayOfWeek day = time.getDayOfWeek();
        return veterinarians.stream()
            .filter(vet -> vet.isAvailable(day, time.toLocalTime()))
            .collect(Collectors.toList());
    }
    
    // ==================== FILE I/O OPERATIONS ====================
    
    /**
     * Saves all appointments to CSV file
          * Source: Mitchell's codebase
     */
    public void saveAppointmentsToFile() throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(APPOINTMENTS_FILE))) {
            // Write header
            writer.println("AppointmentID,PetName,OwnerName,OwnerPhone,AppointmentTime,ServiceType,Veterinarian,Status,Notes");
            
            // Write data
            for (Appointment apt : appointments) {
                writer.println(apt.toCSV());
            }
        }
    }
    
    /**
     * Loads appointments from CSV file
          * Source: Mitchell's codebase
     */
    public void loadAppointmentsFromFile() throws IOException {
        File file = new File(APPOINTMENTS_FILE);
        if (!file.exists()) {
            return;
        }
        
        appointments.clear();
        
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = reader.readLine(); // Skip header
            
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    try {
                        appointments.add(Appointment.fromCSV(line));
                    } catch (Exception e) {
                        System.err.println("Error parsing appointment: " + e.getMessage());
                    }
                }
            }
        }
    }
    
    /**
     * Saves all services to CSV file
          * Source: Mitchell's codebase
     */
    public void saveServicesToFile() throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(SERVICES_FILE))) {
            // Write header
            writer.println("ServiceID,ServiceName,DurationMinutes,Price,Description");
            
            // Write data
            for (Service service : services) {
                writer.println(service.toCSV());
            }
        }
    }
    
    /**
     * Loads services from CSV file
          * Source: Mitchell's codebase
     */
    public void loadServicesFromFile() throws IOException {
        File file = new File(SERVICES_FILE);
        if (!file.exists()) {
            return;
        }
        
        services.clear();
        
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = reader.readLine(); // Skip header
            
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    try {
                        services.add(Service.fromCSV(line));
                    } catch (Exception e) {
                        System.err.println("Error parsing service: " + e.getMessage());
                    }
                }
            }
        }
        
        // If no services loaded, initialize defaults
        if (services.isEmpty()) {
            initializeDefaultServices();
        }
    }
    
    /**
     * Saves all veterinarians to CSV file
          * Source: Mitchell's codebase
     */
    public void saveVeterinariansToFile() throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(VETS_FILE))) {
            // Write header
            writer.println("VetID,Name,Specialization,Phone,Email");
            
            // Write data
            for (Veterinarian vet : veterinarians) {
                writer.println(vet.toCSV());
            }
        }
    }
    
    /**
     * Loads veterinarians from CSV file
          * Source: Mitchell's codebase
     */
    public void loadVeterinariansFromFile() throws IOException {
        File file = new File(VETS_FILE);
        if (!file.exists()) {
            return;
        }
        
        veterinarians.clear();
        
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = reader.readLine(); // Skip header
            
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    try {
                        veterinarians.add(Veterinarian.fromCSV(line));
                    } catch (Exception e) {
                        System.err.println("Error parsing veterinarian: " + e.getMessage());
                    }
                }
            }
        }
    }
    
    /**
     * Saves all data to files
          * Source: Mitchell's codebase
     */
    public void saveAllData() throws IOException {
        saveAppointmentsToFile();
        saveServicesToFile();
        saveVeterinariansToFile();
    }
    
    /**
     * Loads all data from files
          * Source: Mitchell's codebase
     */
    public void loadAllData() throws IOException {
        loadAppointmentsFromFile();
        loadServicesFromFile();
        loadVeterinariansFromFile();
    }
    
    // ==================== UTILITY METHODS ====================
    
    /**
     * Generates a unique appointment ID
          * Source: Mitchell's codebase
     */
    private String generateAppointmentId() {
        int maxId = 0;
        for (Appointment apt : appointments) {
            try {
                int id = Integer.parseInt(apt.getAppointmentId().substring(1));
                if (id > maxId) {
                    maxId = id;
                }
            } catch (Exception e) {
                // Ignore invalid IDs
            }
        }
        return String.format("A%04d", maxId + 1);
    }
    
    /**
     * Validates phone number format
          * Source: Mitchell's codebase
     */
    private boolean isValidPhoneNumber(String phone) {
        if (phone == null) return false;
        // Remove all non-digits
        String digits = phone.replaceAll("[^0-9]", "");
        // Should have 10 digits
        return digits.length() == 10;
    }
    
    /**
     * Gets statistics about appointments
          * Source: Mitchell's codebase
     */
    public Map<String, Integer> getAppointmentStatistics() {
        Map<String, Integer> stats = new HashMap<>();
        
        for (Appointment apt : appointments) {
            String status = apt.getStatus().toString();
            stats.put(status, stats.getOrDefault(status, 0) + 1);
        }
        
        return stats;
    }

    // Source: Mitchell's codebase
    // Convenience summary for UI/reporting flows
    public String getClinicSummary() {
        return "Appointments: " + appointments.size()
            + " | Services: " + services.size()
            + " | Veterinarians: " + veterinarians.size();
    }
}

