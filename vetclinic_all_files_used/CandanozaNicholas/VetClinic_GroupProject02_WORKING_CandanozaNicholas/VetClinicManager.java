// WORKING DRAFT VERSION


import java.io.*;
import java.time.LocalDateTime;
import java.util.*;

public class VetClinicManager {
    private List<Appointment> appointments = new ArrayList<>();
    private static final String FILE_NAME = "appointments.csv";

    // Written by: Nicholas Candanoza – Create appointment with validation
    public Appointment createAppointment(String pet, String owner, String phone,
                                         LocalDateTime time, String service, String vet) {

        if (pet.isEmpty() || owner.isEmpty()) {
            throw new IllegalArgumentException("Pet and Owner required.");
        }

        String id = "A" + (appointments.size() + 1);
        Appointment apt = new Appointment(id, pet, owner, phone, time, service, vet, "SCHEDULED");
        appointments.add(apt);
        return apt;
    }

    // Written by: Nicholas Candanoza – Save all data to CSV
    public void saveAllData() throws IOException {
        PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME));
        for (Appointment a : appointments) {
            writer.println(a.toCSV());
        }
        writer.close();
    }

    // Written by: Nicholas Candanoza – Load all data from CSV
    public void loadAllData() throws IOException {
        File file = new File(FILE_NAME);
        if (!file.exists()) return;

        BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME));
        String line;
        while ((line = reader.readLine()) != null) {
            appointments.add(Appointment.fromCSV(line));
        }
        reader.close();
    }

    public List<Appointment> getAppointments() {
        return appointments;
    }
}

// End of working draft
