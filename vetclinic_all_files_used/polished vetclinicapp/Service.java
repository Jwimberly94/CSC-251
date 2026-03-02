/*
 * Source Note:
 * Shared/identical in both codebases (Mariah_Mitchell and Josh''s project).
 * Kept from Josh''s project copy for the polished merge.
 */
/**
 * Represents a veterinary service offered by the clinic
 */
public class Service {
    private String serviceId;
    private String serviceName;
    private int durationMinutes;
    private double price;
    private String description;
    
    // Source Block: Shared/identical in both (Josh''s project + Mariah_Mitchell)
    // Constructor
    public Service(String serviceId, String serviceName, int durationMinutes, 
                   double price, String description) {
        this.serviceId = serviceId;
        this.serviceName = serviceName;
        this.durationMinutes = durationMinutes;
        this.price = price;
        this.description = description;
    }
    
    // Source Block: Shared/identical in both (Josh''s project + Mariah_Mitchell)
    // Getters
    public String getServiceId() { return serviceId; }
    public String getServiceName() { return serviceName; }
    public int getDurationMinutes() { return durationMinutes; }
    public double getPrice() { return price; }
    public String getDescription() { return description; }
    
    // Source Block: Shared/identical in both (Josh''s project + Mariah_Mitchell)
    // Setters
    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }
    
    public void setDurationMinutes(int durationMinutes) {
        this.durationMinutes = durationMinutes;
    }
    
    public void setPrice(double price) {
        this.price = price;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    /**
     * Converts service to CSV format
        * Source Block: Shared/identical in both (Josh''s project + Mariah_Mitchell)
     */
    public String toCSV() {
        return String.format("%s,%s,%d,%.2f,\"%s\"",
            serviceId,
            serviceName,
            durationMinutes,
            price,
            description.replace("\"", "\"\"")
        );
    }
    
    /**
     * Creates service from CSV line
        * Source Block: Shared/identical in both (Josh''s project + Mariah_Mitchell)
     */
    public static Service fromCSV(String csvLine) {
        String[] parts = csvLine.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
        
        if (parts.length < 5) {
            throw new IllegalArgumentException("Invalid CSV format");
        }
        
        return new Service(
            parts[0].trim(),
            parts[1].trim(),
            Integer.parseInt(parts[2].trim()),
            Double.parseDouble(parts[3].trim()),
            parts[4].trim().replace("\"\"", "\"").replaceAll("^\"|\"$", "")
        );
    }
    
    @Override
    // Source Block: Shared/identical in both (Josh''s project + Mariah_Mitchell)
    public String toString() {
        return String.format("%s - $%.2f (%d min) - %s", 
            serviceName, price, durationMinutes, description);
    }
}

