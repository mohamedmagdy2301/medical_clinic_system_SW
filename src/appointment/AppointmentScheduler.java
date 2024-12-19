package appointment;

 
import java.util.ArrayList;
import java.util.List;

public class AppointmentScheduler {
    // singelton
    private static AppointmentScheduler instance;
    private List<Appointment> appointments;

    private AppointmentScheduler() {
        appointments = new ArrayList<>();
    }

    public static AppointmentScheduler getInstance() {
        if (instance == null) {
            instance = new AppointmentScheduler();
        }
        return instance;
    }

    public void scheduleAppointment(Appointment appointment) {
        appointments.add(appointment);
    }

    public List<Appointment> getAppointments() {
        return appointments;
    }
}
