
import java.time.LocalDateTime;

public class Appointment {
    private String appointmentId;
    private String petName;
    private String ownerName;
    private String ownerPhone;
    private LocalDateTime appointmentTime;
    private String serviceType;
    private String veterinarian;
    private String status;

    // Written by: Nicholas Candanoza – Appointment constructor and CSV logic
    public Appointment(String id, String pet, String owner, String phone,
                       LocalDateTime time, String service, String vet, String status) {
        this.appointmentId = id;
        this.petName = pet;
        this.ownerName = owner;
        this.ownerPhone = phone;
        this.appointmentTime = time;
        this.serviceType = service;
        this.veterinarian = vet;
        this.status = status;
    }

    public String toCSV() {
        return appointmentId + "," + petName + "," + ownerName + "," +
               ownerPhone + "," + appointmentTime + "," +
               serviceType + "," + veterinarian + "," + status;
    }

    public static Appointment fromCSV(String line) {
        String[] parts = line.split(",");
        return new Appointment(
            parts[0],
            parts[1],
            parts[2],
            parts[3],
            LocalDateTime.parse(parts[4]),
            parts[5],
            parts[6],
            parts[7]
        );
    }

    public String getAppointmentId() { return appointmentId; }
}
