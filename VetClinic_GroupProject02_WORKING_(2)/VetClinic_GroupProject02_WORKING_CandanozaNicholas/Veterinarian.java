// Source: Mitchell's codebase
// Merged into: Candanoza's codebase
import java.time.LocalTime;
import java.time.DayOfWeek;
import java.util.*;

/**
 * Represents a veterinarian in the clinic
 */
public class Veterinarian {
    private String vetId;
    private String name;
    private String specialization;
    private String phone;
    private String email;
    private Map<DayOfWeek, Schedule> weeklySchedule;
    
    /**
     * Represents a single day's work schedule
     */
    public static class Schedule {
        private LocalTime startTime;
        private LocalTime endTime;
        private boolean isWorking;
        
        // Source: Mitchell's codebase
        public Schedule(LocalTime startTime, LocalTime endTime) {
            this.startTime = startTime;
            this.endTime = endTime;
            this.isWorking = true;
        }
        
        // Source: Mitchell's codebase
        public Schedule() {
            this.isWorking = false;
        }
        
        // Source: Mitchell's codebase
        public LocalTime getStartTime() { return startTime; }
        // Source: Mitchell's codebase
        public LocalTime getEndTime() { return endTime; }
        // Source: Mitchell's codebase
        public boolean isWorking() { return isWorking; }
        
        // Source: Mitchell's codebase
        @Override
        public String toString() {
            if (!isWorking) return "OFF";
            return startTime + " - " + endTime;
        }
    }
    
    // Source: Mitchell's codebase
    // Constructor
    public Veterinarian(String vetId, String name, String specialization, 
                       String phone, String email) {
        this.vetId = vetId;
        this.name = name;
        this.specialization = specialization;
        this.phone = phone;
        this.email = email;
        this.weeklySchedule = new HashMap<>();
        
        // Initialize with default off days
        for (DayOfWeek day : DayOfWeek.values()) {
            weeklySchedule.put(day, new Schedule());
        }
    }

    // Source: Candanoza's codebase
    // Legacy constructor kept from original Candanoza model
    public Veterinarian(String vetId, String name, String specialization) {
        this(vetId, name, specialization, "", "");
    }
    
    // Source: Mitchell's codebase
    // Getter
    public String getVetId() { return vetId; }
    // Source: Mitchell's codebase
    // Getter
    public String getName() { return name; }
    // Source: Mitchell's codebase
    // Getter
    public String getSpecialization() { return specialization; }
    // Source: Mitchell's codebase
    // Getter
    public String getPhone() { return phone; }
    // Source: Mitchell's codebase
    // Getter
    public String getEmail() { return email; }
    // Source: Mitchell's codebase
    // Getter
    public Map<DayOfWeek, Schedule> getWeeklySchedule() { return weeklySchedule; }
    
    // Source: Mitchell's codebase
    // Setter
    public void setName(String name) {
        this.name = name;
    }
    
    // Source: Mitchell's codebase
    // Setter
    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }
    
    // Source: Mitchell's codebase
    // Setter
    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    // Source: Mitchell's codebase
    // Setter
    public void setEmail(String email) {
        this.email = email;
    }
    
    /**
     * Sets work schedule for a specific day
        * Source: Mitchell's codebase
     */
    public void setSchedule(DayOfWeek day, LocalTime startTime, LocalTime endTime) {
        weeklySchedule.put(day, new Schedule(startTime, endTime));
    }
    
    /**
     * Sets a day as off
        * Source: Mitchell's codebase
     */
    public void setDayOff(DayOfWeek day) {
        weeklySchedule.put(day, new Schedule());
    }
    
    /**
     * Checks if vet is available at a specific time
        * Source: Mitchell's codebase
     */
    public boolean isAvailable(DayOfWeek day, LocalTime time) {
        Schedule schedule = weeklySchedule.get(day);
        if (schedule == null || !schedule.isWorking()) {
            return false;
        }
        return !time.isBefore(schedule.getStartTime()) && !time.isAfter(schedule.getEndTime());
    }
    
    /**
     * Converts veterinarian to CSV format
        * Source: Mitchell's codebase
     */
    public String toCSV() {
        return String.format("%s,%s,%s,%s,%s",
            vetId,
            name,
            specialization,
            phone,
            email
        );
    }
    
    /**
     * Creates veterinarian from CSV line
        * Source: Mitchell's codebase
     */
    public static Veterinarian fromCSV(String csvLine) {
        String[] parts = csvLine.split(",");
        
        if (parts.length < 5) {
            throw new IllegalArgumentException("Invalid CSV format");
        }
        
        return new Veterinarian(
            parts[0].trim(),
            parts[1].trim(),
            parts[2].trim(),
            parts[3].trim(),
            parts[4].trim()
        );
    }
    
    // Source: Mitchell's codebase
    @Override
    public String toString() {
        return String.format("Dr. %s (%s) - %s", name, specialization, phone);
    }
}

