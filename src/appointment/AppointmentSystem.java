package appointment;



import java.util.Date;

// Interface for the existing Appointment system
public interface AppointmentSystem {
    void scheduleAppointment(String patientName, String doctorName, Date appointmentDate);
}
