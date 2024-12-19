package appointment;


import java.util.Date;

// Adapter class that converts the new system's method to the existing system's interface
public class AppointmentAdapter implements AppointmentSystem {
    private Appointment appointment;

    public AppointmentAdapter(Appointment appointment) {
        this.appointment = appointment;
    }

    @Override
    public void scheduleAppointment(String patientName, String doctorName, Date appointmentDate) {
        System.out.println("Scheduling appointment with Dr. " + doctorName + " for patient " + patientName + " on " + appointmentDate);
    }

    public Appointment getAppointment() {
        return this.appointment;
    }
}