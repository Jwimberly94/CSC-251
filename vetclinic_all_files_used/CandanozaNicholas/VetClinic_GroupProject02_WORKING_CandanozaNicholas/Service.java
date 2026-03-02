
public class Service {
    private String serviceId;
    private String serviceName;
    private int durationMinutes;
    private double price;

    public Service(String id, String name, int duration, double price) {
        this.serviceId = id;
        this.serviceName = name;
        this.durationMinutes = duration;
        this.price = price;
    }
}
