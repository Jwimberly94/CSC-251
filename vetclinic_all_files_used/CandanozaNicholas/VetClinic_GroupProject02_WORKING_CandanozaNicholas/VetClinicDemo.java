
import java.time.LocalDateTime;

public class VetClinicDemo {
    public static void main(String[] args) {
        VetClinicManager manager = new VetClinicManager();

        try {
            manager.loadAllData();

            manager.createAppointment(
                "Max",
                "John Smith",
                "5551234567",
                LocalDateTime.now().plusDays(1),
                "Checkup",
                "Dr. Brown"
            );

            manager.saveAllData();

            System.out.println("Appointments saved successfully.");
            System.out.println("Total Appointments: " + manager.getAppointments().size());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
