
import appointment.Appointment;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MainUI extends JFrame {

    private JTextField patientNameField, doctorNameField;
    private JComboBox<String> doctorComboBox; // ComboBox for selecting doctors
    private JComboBox<String> appointmentDateField; // ComboBox for selecting appointment dates
    private JTable appointmentsTable, schedulesTable;
    private DefaultTableModel appointmentsTableModel, schedulesTableModel;

    private final ArrayList<Appointment> appointments = new ArrayList<>();
    private final Map<String, ArrayList<String>> doctorAvailableDates = new HashMap<>();

    public MainUI() {
        setTitle("Medical Clinic Management System");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Header
        add(createHeader(), BorderLayout.NORTH);

        // Tabs
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.add("Doctor Schedules", createDoctorSchedulesTab());
        tabbedPane.add("Appointments", createAppointmentsTab());
        add(tabbedPane, BorderLayout.CENTER);

        // Load data from the database
        loadDoctorSchedules();
        loadAppointments();
    }

    private JLabel createHeader() {
        JLabel headerLabel = new JLabel("Medical Clinic Management System", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headerLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        return headerLabel;
    }

    private JPanel createAppointmentsTab() {
        JPanel panel = new JPanel(new BorderLayout());
        JPanel inputPanel = createAppointmentsInputPanel();

        // Table for displaying appointments
        appointmentsTableModel = new DefaultTableModel(new String[]{"Patient Name", "Doctor Name", "Appointment Date"}, 0);
        appointmentsTable = new JTable(appointmentsTableModel);
        JScrollPane scrollPane = new JScrollPane(appointmentsTable);

        panel.add(inputPanel, BorderLayout.EAST);
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createAppointmentsInputPanel() {
        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = createGridBagConstraints();

        JLabel patientNameLabel = new JLabel("Patient Name:");
        JLabel doctorNameLabel = new JLabel("Select Doctor:");
        JLabel appointmentDateLabel = new JLabel("Appointment Date:");

        patientNameField = new JTextField(20);
        doctorComboBox = new JComboBox<>();
        appointmentDateField = new JComboBox<>();

        doctorComboBox.addActionListener(e -> updateAvailableDatesForDoctor());

        JButton scheduleButton = new JButton("Schedule Appointment");
        scheduleButton.addActionListener(e -> scheduleAppointment());

        // Layout
        gbc.gridx = 0;
        gbc.gridy = 0;
        inputPanel.add(patientNameLabel, gbc);
        gbc.gridx = 1;
        inputPanel.add(patientNameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        inputPanel.add(doctorNameLabel, gbc);
        gbc.gridx = 1;
        inputPanel.add(doctorComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        inputPanel.add(appointmentDateLabel, gbc);
        gbc.gridx = 1;
        inputPanel.add(appointmentDateField, gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.CENTER;
        inputPanel.add(scheduleButton, gbc);

        return inputPanel;
    }

    private JPanel createDoctorSchedulesTab() {
        JPanel panel = new JPanel(new BorderLayout());
        JPanel inputPanel = createDoctorSchedulesInputPanel();

        // Table for displaying doctor schedules
        schedulesTableModel = new DefaultTableModel(new String[]{"Doctor Name", "Available Days"}, 0);
        schedulesTable = new JTable(schedulesTableModel);
        JScrollPane scrollPane = new JScrollPane(schedulesTable);

        panel.add(inputPanel, BorderLayout.WEST);
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createDoctorSchedulesInputPanel() {
        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = createGridBagConstraints();

        JLabel doctorNameLabel = new JLabel("Name:");
        JLabel availableDaysLabel = new JLabel("Available Date (yyyy-MM-dd):");

        doctorNameField = new JTextField(20);
        JSpinner availableDaysSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(availableDaysSpinner, "yyyy-MM-dd");
        availableDaysSpinner.setEditor(dateEditor);

        JButton addScheduleButton = new JButton("Add Schedule");
        addScheduleButton.addActionListener(e -> {
            String doctorName = doctorNameField.getText().trim();
            Date availableDate = (Date) availableDaysSpinner.getValue();
            String formattedDate = new SimpleDateFormat("yyyy-MM-dd").format(availableDate);
            addDoctorSchedule(doctorName, formattedDate);
        });

        // Layout
        gbc.gridx = 0;
        gbc.gridy = 0;
        inputPanel.add(doctorNameLabel, gbc);
        gbc.gridx = 1;
        inputPanel.add(doctorNameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        inputPanel.add(availableDaysLabel, gbc);
        gbc.gridx = 1;
        inputPanel.add(availableDaysSpinner, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        inputPanel.add(addScheduleButton, gbc);

        return inputPanel;
    }

    private GridBagConstraints createGridBagConstraints() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        return gbc;
    }

    private Connection connectToDatabase() throws SQLException {
        String url = "jdbc:sqlserver://localhost:1433;databaseName=ClinicDB";
        String username = "sa";
        String password = "eelu123";
        return DriverManager.getConnection(url, username, password);
    }

    private void addDoctorSchedule(String doctorName, String availableDays) {
        try (Connection connection = connectToDatabase()) {
            if (doctorName.isEmpty() || availableDays.isEmpty()) {
                showErrorDialog("All fields must be filled out.");
                return;
            }

            Date availableDate = validateDate(availableDays);
            String formattedDate = new SimpleDateFormat("yyyy-MM-dd").format(availableDate);

            String sql = "INSERT INTO DoctorSchedules (doctor_name, available_date) VALUES (?, ?)";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, doctorName);
                stmt.setString(2, formattedDate);
                stmt.executeUpdate();
            }

            schedulesTableModel.addRow(new Object[]{doctorName, formattedDate});
            if (!doctorComboBoxContainsDoctor(doctorName)) {
                doctorComboBox.addItem(doctorName);
            }
            updateDoctorAvailableDates(doctorName, formattedDate);
            updateAvailableDatesForDoctor();

            JOptionPane.showMessageDialog(this, "Doctor schedule added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            doctorNameField.setText("");
        } catch (SQLException | ParseException ex) {
            showErrorDialog(ex.getMessage());
        }
    }

    private void scheduleAppointment() {
        try (Connection connection = connectToDatabase()) {
            String patientName = patientNameField.getText();
            String doctorName = (String) doctorComboBox.getSelectedItem();
            String appointmentDateStr = (String) appointmentDateField.getSelectedItem();

            if (patientName.isEmpty() || doctorName == null || appointmentDateStr == null) {
                throw new IllegalArgumentException("All fields must be filled out.");
            }

            Date appointmentDate = validateDate(appointmentDateStr);
            String formattedDate = new SimpleDateFormat("yyyy-MM-dd").format(appointmentDate);

            String sql = "INSERT INTO PatientAppointments (patient_name, doctor_name, appointment_date) VALUES (?, ?, ?)";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, patientName);
                stmt.setString(2, doctorName);
                stmt.setString(3, formattedDate);
                stmt.executeUpdate();
            }

            appointmentsTableModel.addRow(new Object[]{patientName, doctorName, formattedDate});
            JOptionPane.showMessageDialog(this, "Appointment scheduled successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            patientNameField.setText("");
        } catch (SQLException | ParseException | IllegalArgumentException ex) {
            showErrorDialog(ex.getMessage());
        }
    }

    private void loadDoctorSchedules() {
        try (Connection connection = connectToDatabase()) {
            String sql = "SELECT doctor_name, available_date FROM DoctorSchedules";
            try (PreparedStatement stmt = connection.prepareStatement(sql);
                    ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {
                    String doctorName = rs.getString("doctor_name");
                    String availableDate = rs.getDate("available_date").toString();

                    schedulesTableModel.addRow(new Object[]{doctorName, availableDate});
                    if (!doctorComboBoxContainsDoctor(doctorName)) {
                        doctorComboBox.addItem(doctorName);
                    }
                    updateDoctorAvailableDates(doctorName, availableDate);
                }
            }
        } catch (SQLException ex) {
            showErrorDialog(ex.getMessage());
        }
    }

    private void loadAppointments() {
        try (Connection connection = connectToDatabase()) {
            String sql = "SELECT patient_name, doctor_name, appointment_date FROM PatientAppointments";
            try (PreparedStatement stmt = connection.prepareStatement(sql);
                    ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {
                    String patientName = rs.getString("patient_name");
                    String doctorName = rs.getString("doctor_name");
                    String appointmentDate = rs.getDate("appointment_date").toString();

                    appointmentsTableModel.addRow(new Object[]{patientName, doctorName, appointmentDate});
                }
            }
        } catch (SQLException ex) {
            showErrorDialog(ex.getMessage());
        }
    }

    private boolean doctorComboBoxContainsDoctor(String doctorName) {
        for (int i = 0; i < doctorComboBox.getItemCount(); i++) {
            if (doctorComboBox.getItemAt(i).equals(doctorName)) {
                return true;
            }
        }
        return false;
    }

    private void updateDoctorAvailableDates(String doctorName, String availableDate) {
        doctorAvailableDates.computeIfAbsent(doctorName, k -> new ArrayList<>()).add(availableDate);
    }

    private void updateAvailableDatesForDoctor() {
        appointmentDateField.removeAllItems();
        String doctorName = (String) doctorComboBox.getSelectedItem();
        if (doctorName != null && doctorAvailableDates.containsKey(doctorName)) {
            for (String date : doctorAvailableDates.get(doctorName)) {
                appointmentDateField.addItem(date);
            }
        }
    }

    private Date validateDate(String dateStr) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        format.setLenient(false);
        return format.parse(dateStr);
    }

    private void showErrorDialog(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainUI ui = new MainUI();
            ui.setVisible(true);
        });
    }
}
