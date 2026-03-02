/**
 * Represents a veterinary service offered by the clinic
 */
public class Service {
    private String serviceId;
    private String serviceName;
    private int durationMinutes;
    private double price;
    private String description;
    
    // Constructor
    public Service(String serviceId, String serviceName, int durationMinutes, 
                   double price, String description) {
        this.serviceId = serviceId;
        this.serviceName = serviceName;
        this.durationMinutes = durationMinutes;
        this.price = price;
        this.description = description;
    }

    // Source: Candanoza's codebase
    // Legacy constructor preserved from original Candanoza implementation
    public Service(String serviceId, String serviceName, int durationMinutes, double price) {
        this(serviceId, serviceName, durationMinutes, price, "");
    }
    
    // Getters
    public String getServiceId() { return serviceId; }
    public String getServiceName() { return serviceName; }
    public int getDurationMinutes() { return durationMinutes; }
    public double getPrice() { return price; }
    public String getDescription() { return description; }
    
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
    public String toString() {
        return String.format("%s - $%.2f (%d min) - %s", 
            serviceName, price, durationMinutes, description);
    }
}
