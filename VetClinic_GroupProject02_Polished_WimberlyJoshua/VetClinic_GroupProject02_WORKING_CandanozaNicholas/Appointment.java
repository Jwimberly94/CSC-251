// Source: Candanoza's codebase
// Merged into: Candanoza's codebase
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents a veterinary appointment
 */
public class Appointment {
    private String appointmentId;
    private String petName;
    private String ownerName;
    private String ownerPhone;
    private LocalDateTime appointmentTime;
    private String serviceType;
    private String veterinarian;
    private AppointmentStatus status;
    private String notes;
    
    public enum AppointmentStatus {
        SCHEDULED,
        CHECKED_IN,
        IN_PROGRESS,
        COMPLETED,
        CANCELLED,
        NO_SHOW
    }
    
    // Source: Candanoza's codebase
    // Constructor
    public Appointment(String appointmentId, String petName, String ownerName, 
                      String ownerPhone, LocalDateTime appointmentTime, 
                      String serviceType, String veterinarian) {
        this.appointmentId = appointmentId;
        this.petName = petName;
        this.ownerName = ownerName;
        this.ownerPhone = ownerPhone;
        this.appointmentTime = appointmentTime;
        this.serviceType = serviceType;
        this.veterinarian = veterinarian;
        this.status = AppointmentStatus.SCHEDULED;
        this.notes = "";
    }

    // Source: Candanoza's codebase
    // Legacy constructor kept from original Candanoza model
    public Appointment(String appointmentId, String petName, String ownerName,
                      String ownerPhone, LocalDateTime appointmentTime,
                      String serviceType, String veterinarian, String status) {
        this(appointmentId, petName, ownerName, ownerPhone, appointmentTime, serviceType, veterinarian);
        try {
            this.status = AppointmentStatus.valueOf(status);
        } catch (Exception ex) {
            this.status = AppointmentStatus.SCHEDULED;
        }
    }
    
    // Source: Candanoza's codebase
    // Getter
    public String getAppointmentId() { return appointmentId; }
    // Source: Candanoza's codebase
    // Getter
    public String getPetName() { return petName; }
    // Source: Candanoza's codebase
    // Getter
    public String getOwnerName() { return ownerName; }
    // Source: Candanoza's codebase
    // Getter
    public String getOwnerPhone() { return ownerPhone; }
    // Source: Candanoza's codebase
    // Getter
    public LocalDateTime getAppointmentTime() { return appointmentTime; }
    // Source: Candanoza's codebase
    // Getter
    public String getServiceType() { return serviceType; }
    // Source: Candanoza's codebase
    // Getter
    public String getVeterinarian() { return veterinarian; }
    // Source: Candanoza's codebase
    // Getter
    public AppointmentStatus getStatus() { return status; }
    // Source: Candanoza's codebase
    // Getter
    public String getNotes() { return notes; }
    
    // Source: Candanoza's codebase
    // Setter
    public void setAppointmentTime(LocalDateTime appointmentTime) {
        this.appointmentTime = appointmentTime;
    }
    
    // Source: Candanoza's codebase
    // Setter
    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }
    
    // Source: Candanoza's codebase
    // Setter
    public void setVeterinarian(String veterinarian) {
        this.veterinarian = veterinarian;
    }
    
    // Source: Candanoza's codebase
    // Setter
    public void setStatus(AppointmentStatus status) {
        this.status = status;
    }
    
    // Source: Candanoza's codebase
    // Setter
    public void setNotes(String notes) {
        this.notes = notes;
    }
    
    // Source: Candanoza's codebase
    public void addNote(String note) {
        if (this.notes.isEmpty()) {
            this.notes = note;
        } else {
            this.notes += "\n" + note;
        }
    }
    
    /**
     * Converts appointment to CSV format
        * Source: Candanoza's codebase
     */
    public String toCSV() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return String.format("%s,%s,%s,%s,%s,%s,%s,%s,\"%s\"",
            appointmentId,
            petName,
            ownerName,
            ownerPhone,
            appointmentTime.format(formatter),
            serviceType,
            veterinarian,
            status.toString(),
            notes.replace("\"", "\"\"") // Escape quotes in notes
        );
    }
    
    /**
     * Creates appointment from CSV line
        * Source: Candanoza's codebase
     */
    public static Appointment fromCSV(String csvLine) {
        String[] parts = csvLine.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)"); // Handle quoted fields
        
        if (parts.length < 8) {
            throw new IllegalArgumentException("Invalid CSV format");
        }
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        
        Appointment apt = new Appointment(
            parts[0].trim(),
            parts[1].trim(),
            parts[2].trim(),
            parts[3].trim(),
            LocalDateTime.parse(parts[4].trim(), formatter),
            parts[5].trim(),
            parts[6].trim()
        );
        
        apt.setStatus(AppointmentStatus.valueOf(parts[7].trim()));
        
        if (parts.length > 8) {
            apt.setNotes(parts[8].trim().replace("\"\"", "\"").replaceAll("^\"|\"$", ""));
        }
        
        return apt;
    }
    
    // Source: Candanoza's codebase
    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy hh:mm a");
        return String.format("ID: %s | Pet: %s | Owner: %s | Time: %s | Service: %s | Vet: %s | Status: %s",
            appointmentId, petName, ownerName, appointmentTime.format(formatter), 
            serviceType, veterinarian, status);
    }
}

