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
        
        public Schedule(LocalTime startTime, LocalTime endTime) {
            this.startTime = startTime;
            this.endTime = endTime;
            this.isWorking = true;
        }
        
        public Schedule() {
            this.isWorking = false;
        }
        
        public LocalTime getStartTime() { return startTime; }
        public LocalTime getEndTime() { return endTime; }
        public boolean isWorking() { return isWorking; }
        
        @Override
        public String toString() {
            if (!isWorking) return "OFF";
            return startTime + " - " + endTime;
        }
    }
    
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
    
    // Getters
    public String getVetId() { return vetId; }
    public String getName() { return name; }
    public String getSpecialization() { return specialization; }
    public String getPhone() { return phone; }
    public String getEmail() { return email; }
    public Map<DayOfWeek, Schedule> getWeeklySchedule() { return weeklySchedule; }
    
    // Setters
    public void setName(String name) {
        this.name = name;
    }
    
    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    /**
     * Sets work schedule for a specific day
     */
    public void setSchedule(DayOfWeek day, LocalTime startTime, LocalTime endTime) {
        weeklySchedule.put(day, new Schedule(startTime, endTime));
    }
    
    /**
     * Sets a day as off
     */
    public void setDayOff(DayOfWeek day) {
        weeklySchedule.put(day, new Schedule());
    }
    
    /**
     * Checks if vet is available at a specific time
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
    
    @Override
    public String toString() {
        return String.format("Dr. %s (%s) - %s", name, specialization, phone);
    }
}
