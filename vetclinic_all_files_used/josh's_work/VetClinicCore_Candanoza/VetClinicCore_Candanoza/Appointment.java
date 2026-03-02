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
    
    // Getters
    public String getAppointmentId() { return appointmentId; }
    public String getPetName() { return petName; }
    public String getOwnerName() { return ownerName; }
    public String getOwnerPhone() { return ownerPhone; }
    public LocalDateTime getAppointmentTime() { return appointmentTime; }
    public String getServiceType() { return serviceType; }
    public String getVeterinarian() { return veterinarian; }
    public AppointmentStatus getStatus() { return status; }
    public String getNotes() { return notes; }
    
    // Setters
    public void setAppointmentTime(LocalDateTime appointmentTime) {
        this.appointmentTime = appointmentTime;
    }
    
    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }
    
    public void setVeterinarian(String veterinarian) {
        this.veterinarian = veterinarian;
    }
    
    public void setStatus(AppointmentStatus status) {
        this.status = status;
    }
    
    public void setNotes(String notes) {
        this.notes = notes;
    }
    
    public void addNote(String note) {
        if (this.notes.isEmpty()) {
            this.notes = note;
        } else {
            this.notes += "\n" + note;
        }
    }
    
    /**
     * Converts appointment to CSV format
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
    
    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy hh:mm a");
        return String.format("ID: %s | Pet: %s | Owner: %s | Time: %s | Service: %s | Vet: %s | Status: %s",
            appointmentId, petName, ownerName, appointmentTime.format(formatter), 
            serviceType, veterinarian, status);
    }
}
