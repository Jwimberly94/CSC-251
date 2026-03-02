import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

public class AppGUI {
    private static final VetClinicManager clinicManager = new VetClinicManager();
    private static final DateTimeFormatter INPUT_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private static final DateTimeFormatter UPDATED_FORMAT = DateTimeFormatter.ofPattern("MMM dd, yyyy hh:mm a");
    private static JFrame frame;
    private static JLabel appointmentCountLabel;
    private static JLabel upcomingCountLabel;
    private static JLabel serviceCountLabel;
    private static JLabel vetCountLabel;
    private static JLabel checkedInCountLabel;
    private static JLabel lastUpdatedLabel;

    // Launch the Swing UI on the event dispatch thread.
    public static void main(String[] args) {
        SwingUtilities.invokeLater(AppGUI::showApp);
    }

    // Build and show the main application window.
    private static void showApp() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            // Default look and feel is fine if system LAF fails.
        }

        frame = new JFrame("Vet Clinic Management");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.add(createHeaderPanel(), BorderLayout.NORTH);
        frame.add(createNavPanel(), BorderLayout.WEST);
        frame.add(createSummaryPanel(), BorderLayout.CENTER);
        frame.setSize(940, 580);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        loadDataSafely();
        updateSummaryLabels();
    }

    // Create the top header bar with title and subtitle.
    private static JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(16, 20, 16, 20));
        panel.setBackground(new Color(33, 64, 54));

        JLabel title = new JLabel("Red Barn Vet Clinic");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("SansSerif", Font.BOLD, 20));
        panel.add(title, BorderLayout.WEST);

        JLabel subtitle = new JLabel("Appointments, Services, and Veterinarians");
        subtitle.setForeground(new Color(220, 235, 230));
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 13));
        panel.add(subtitle, BorderLayout.EAST);

        return panel;
    }

    // Create the left navigation panel with action buttons.
    private static JPanel createNavPanel() {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        panel.setBackground(new Color(248, 245, 240));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setPreferredSize(new Dimension(240, 0));

        JLabel navTitle = new JLabel("Actions");
        navTitle.setFont(new Font("SansSerif", Font.BOLD, 14));
        navTitle.setAlignmentX(JLabel.LEFT_ALIGNMENT);
        panel.add(navTitle);
        panel.add(new JPanel());

        panel.add(createNavButton("Schedule appointment", AppGUI::handleAppointmentSchedule));
        panel.add(createNavButton("Add service", AppGUI::handleAddService));
        panel.add(createNavButton("Add veterinarian", AppGUI::handleAddVeterinarian));
        panel.add(createNavButton("View quick summary", AppGUI::showSummary));
        panel.add(createNavButton("Run core demo (console)", AppGUI::runCoreDemo));
        panel.add(createNavButton("Exit", () -> frame.dispose()));

        return panel;
    }

    // Build a navigation button that runs the provided action.
    private static JButton createNavButton(String label, Runnable action) {
        JButton button = new JButton(label);
        button.setAlignmentX(JButton.LEFT_ALIGNMENT);
        button.setMaximumSize(new Dimension(220, 36));
        button.addActionListener(event -> action.run());
        return button;
    }

    // Create the center summary grid of key stats.
    private static JPanel createSummaryPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel header = new JLabel("Quick Summary");
        header.setFont(new Font("SansSerif", Font.BOLD, 18));
        panel.add(header, BorderLayout.NORTH);

        JPanel grid = new JPanel(new GridLayout(3, 2, 16, 16));
        grid.add(createStatCard("Total appointments", appointmentCountLabel = new JLabel("0")));
        grid.add(createStatCard("Upcoming appointments", upcomingCountLabel = new JLabel("0")));
        grid.add(createStatCard("Services", serviceCountLabel = new JLabel("0")));
        grid.add(createStatCard("Veterinarians", vetCountLabel = new JLabel("0")));
        grid.add(createStatCard("Checked-in appointments", checkedInCountLabel = new JLabel("0")));
        grid.add(createStatCard("Last updated", lastUpdatedLabel = new JLabel("Just now")));

        panel.add(grid, BorderLayout.CENTER);
        return panel;
    }

    // Create a small card panel for a labeled metric.
    private static JPanel createStatCard(String title, JLabel valueLabel) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout());
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220)),
            BorderFactory.createEmptyBorder(12, 12, 12, 12)
        ));
        card.setBackground(new Color(252, 252, 252));
        card.setOpaque(true);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));

        valueLabel.setFont(new Font("SansSerif", Font.BOLD, 18));

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);
        return card;
    }

    // Collect input and schedule a new appointment.
    private static void handleAppointmentSchedule() {
        String petName = prompt("Pet name:");
        if (petName == null || petName.isEmpty()) {
            return;
        }
        String ownerName = prompt("Owner name:");
        if (ownerName == null || ownerName.isEmpty()) {
            return;
        }
        String ownerPhone = prompt("Owner phone (10 digits):");
        if (ownerPhone == null || ownerPhone.isEmpty()) {
            return;
        }
        String serviceType = promptServiceType();
        if (serviceType == null || serviceType.isEmpty()) {
            return;
        }
        String vetName = promptVetName();
        if (vetName == null || vetName.isEmpty()) {
            return;
        }
        LocalDateTime appointmentTime = promptDateTime("Appointment time (yyyy-MM-dd HH:mm):");
        if (appointmentTime == null) {
            return;
        }

        try {
            Appointment appointment = clinicManager.createAppointment(
                petName,
                ownerName,
                ownerPhone,
                appointmentTime,
                serviceType,
                vetName
            );
            saveDataSafely();
            JOptionPane.showMessageDialog(frame, "Appointment scheduled: " + appointment.getAppointmentId());
            updateSummaryLabels();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame, ex.getMessage(), "Unable to schedule", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Collect input and add a new service offering.
    private static void handleAddService() {
        String serviceName = prompt("Service name:");
        if (serviceName == null || serviceName.isEmpty()) {
            return;
        }
        int durationMinutes = promptInt("Duration in minutes:");
        if (durationMinutes <= 0) {
            return;
        }
        double price = promptDouble("Price:");
        if (price <= 0) {
            return;
        }
        String description = prompt("Description:");
        if (description == null) {
            description = "";
        }

        Service service = new Service(generateServiceId(), serviceName, durationMinutes, price, description);
        clinicManager.addService(service);
        saveDataSafely();
        JOptionPane.showMessageDialog(frame, "Service added: " + service.getServiceName());
        updateSummaryLabels();
    }

    // Collect input and add a new veterinarian.
    private static void handleAddVeterinarian() {
        String name = prompt("Veterinarian name:");
        if (name == null || name.isEmpty()) {
            return;
        }
        String specialization = prompt("Specialization:");
        if (specialization == null || specialization.isEmpty()) {
            return;
        }
        String phone = prompt("Phone (10 digits):");
        if (phone == null || phone.isEmpty()) {
            return;
        }
        String email = prompt("Email:");
        if (email == null || email.isEmpty()) {
            return;
        }

        Veterinarian vet = new Veterinarian(generateVetId(), name, specialization, phone, email);
        setDefaultSchedule(vet);
        clinicManager.addVeterinarian(vet);
        saveDataSafely();
        JOptionPane.showMessageDialog(frame, "Veterinarian added: Dr. " + vet.getName());
        updateSummaryLabels();
    }

    // Run the core demo in the console for reference.
    private static void runCoreDemo() {
        VetClinicDemo.main(new String[0]);
        JOptionPane.showMessageDialog(frame, "Demo output written to the console.");
    }

    // Show a modal summary of key counts.
    private static void showSummary() {
        Map<String, Integer> stats = clinicManager.getAppointmentStatistics();
        int checkedInCount = stats.getOrDefault(Appointment.AppointmentStatus.CHECKED_IN.toString(), 0);
        String summary =
            "Total appointments: " + clinicManager.getAllAppointments().size() + "\n" +
            "Upcoming appointments: " + clinicManager.getUpcomingAppointments().size() + "\n" +
            "Services: " + clinicManager.getAllServices().size() + "\n" +
            "Veterinarians: " + clinicManager.getAllVeterinarians().size() + "\n" +
            "Checked-in appointments: " + checkedInCount;
        JOptionPane.showMessageDialog(frame, summary, "Summary", JOptionPane.INFORMATION_MESSAGE);
    }

    // Refresh the summary labels after changes.
    private static void updateSummaryLabels() {
        if (appointmentCountLabel == null) {
            return;
        }
        Map<String, Integer> stats = clinicManager.getAppointmentStatistics();
        appointmentCountLabel.setText(String.valueOf(clinicManager.getAllAppointments().size()));
        upcomingCountLabel.setText(String.valueOf(clinicManager.getUpcomingAppointments().size()));
        serviceCountLabel.setText(String.valueOf(clinicManager.getAllServices().size()));
        vetCountLabel.setText(String.valueOf(clinicManager.getAllVeterinarians().size()));
        checkedInCountLabel.setText(String.valueOf(
            stats.getOrDefault(Appointment.AppointmentStatus.CHECKED_IN.toString(), 0))
        );
        lastUpdatedLabel.setText(LocalDateTime.now().format(UPDATED_FORMAT));
    }

    // Load clinic data from CSV files and report errors.
    private static void loadDataSafely() {
        try {
            clinicManager.loadAllData();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame, "Unable to load data: " + ex.getMessage(),
                "Load Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Save clinic data to CSV files and report errors.
    private static void saveDataSafely() {
        try {
            clinicManager.saveAllData();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame, "Unable to save data: " + ex.getMessage(),
                "Save Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Prompt the user for a string value.
    private static String prompt(String message) {
        return JOptionPane.showInputDialog(frame, message, "Input", JOptionPane.QUESTION_MESSAGE);
    }

    // Prompt for an integer and validate input.
    private static int promptInt(String message) {
        String value = prompt(message);
        if (value == null) {
            return -1;
        }
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(frame, "Please enter a whole number.");
            return -1;
        }
    }

    // Prompt for a decimal and validate input.
    private static double promptDouble(String message) {
        String value = prompt(message);
        if (value == null) {
            return -1;
        }
        try {
            return Double.parseDouble(value.trim());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(frame, "Please enter a valid number.");
            return -1;
        }
    }

    // Prompt for a date/time using the required format.
    private static LocalDateTime promptDateTime(String message) {
        String value = prompt(message);
        if (value == null || value.isEmpty()) {
            return null;
        }
        try {
            return LocalDateTime.parse(value.trim(), INPUT_FORMAT);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame, "Use format: yyyy-MM-dd HH:mm");
            return null;
        }
    }

    // Prompt for a service name with a short list of options.
    private static String promptServiceType() {
        List<Service> services = clinicManager.getAllServices();
        String hint = services.isEmpty() ? "" : " Available: " + summarizeServices(services);
        return prompt("Service type:" + hint);
    }

    // Prompt for a veterinarian name with a short list of options.
    private static String promptVetName() {
        List<Veterinarian> vets = clinicManager.getAllVeterinarians();
        String hint = vets.isEmpty() ? "" : " Available: " + summarizeVets(vets);
        return prompt("Veterinarian name:" + hint);
    }

    // Provide a short list of service names for prompts.
    private static String summarizeServices(List<Service> services) {
        StringBuilder builder = new StringBuilder();
        int count = 0;
        for (Service service : services) {
            if (count == 5) {
                builder.append("...");
                break;
            }
            if (count > 0) {
                builder.append(", ");
            }
            builder.append(service.getServiceName());
            count++;
        }
        return builder.toString();
    }

    // Provide a short list of veterinarian names for prompts.
    private static String summarizeVets(List<Veterinarian> vets) {
        StringBuilder builder = new StringBuilder();
        int count = 0;
        for (Veterinarian vet : vets) {
            if (count == 5) {
                builder.append("...");
                break;
            }
            if (count > 0) {
                builder.append(", ");
            }
            builder.append(vet.getName());
            count++;
        }
        return builder.toString();
    }

    // Generate a basic service ID based on the current list size.
    private static String generateServiceId() {
        int nextId = clinicManager.getAllServices().size() + 1;
        return String.format("S%03d", nextId);
    }

    // Generate a basic veterinarian ID based on the current list size.
    private static String generateVetId() {
        int nextId = clinicManager.getAllVeterinarians().size() + 1;
        return String.format("V%03d", nextId);
    }

    // Apply a standard weekday schedule to a new veterinarian.
    private static void setDefaultSchedule(Veterinarian vet) {
        LocalTime start = LocalTime.of(9, 0);
        LocalTime end = LocalTime.of(17, 0);
        vet.setSchedule(DayOfWeek.MONDAY, start, end);
        vet.setSchedule(DayOfWeek.TUESDAY, start, end);
        vet.setSchedule(DayOfWeek.WEDNESDAY, start, end);
        vet.setSchedule(DayOfWeek.THURSDAY, start, end);
        vet.setSchedule(DayOfWeek.FRIDAY, start, end);
    }
}
