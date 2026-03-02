/*
 * Source Note:
 * Shared/identical in both codebases (Mariah_Mitchell and Josh''s project).
 * Kept from Josh''s project copy for the polished merge.
 */
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
        * Source Block: Shared/identical in both (Josh''s project + Mariah_Mitchell)
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
    
    // Source Block: Shared/identical in both (Josh''s project + Mariah_Mitchell)
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
    
    // Source Block: Shared/identical in both (Josh''s project + Mariah_Mitchell)
    // Getters
    public String getVetId() { return vetId; }
    public String getName() { return name; }
    public String getSpecialization() { return specialization; }
    public String getPhone() { return phone; }
    public String getEmail() { return email; }
    public Map<DayOfWeek, Schedule> getWeeklySchedule() { return weeklySchedule; }
    
    // Source Block: Shared/identical in both (Josh''s project + Mariah_Mitchell)
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
        * Source Block: Shared/identical in both (Josh''s project + Mariah_Mitchell)
     */
    public void setSchedule(DayOfWeek day, LocalTime startTime, LocalTime endTime) {
        weeklySchedule.put(day, new Schedule(startTime, endTime));
    }
    
    /**
     * Sets a day as off
        * Source Block: Shared/identical in both (Josh''s project + Mariah_Mitchell)
     */
    public void setDayOff(DayOfWeek day) {
        weeklySchedule.put(day, new Schedule());
    }
    
    /**
     * Checks if vet is available at a specific time
        * Source Block: Shared/identical in both (Josh''s project + Mariah_Mitchell)
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
        * Source Block: Shared/identical in both (Josh''s project + Mariah_Mitchell)
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
        * Source Block: Shared/identical in both (Josh''s project + Mariah_Mitchell)
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
    // Source Block: Shared/identical in both (Josh''s project + Mariah_Mitchell)
    public String toString() {
        return String.format("Dr. %s (%s) - %s", name, specialization, phone);
    }
}

