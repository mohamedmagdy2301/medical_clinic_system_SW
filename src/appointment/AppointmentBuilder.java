package appointment;


 
import appointment.Appointment;
import java.util.Date;
import patient.Patient;

public class AppointmentBuilder {
    private Patient patient;
    private String doctorName;
    private Date appointmentDate;

    public AppointmentBuilder setPatient(Patient patient) {
        this.patient = patient;
        return this;
    }

    public AppointmentBuilder setDoctorName(String doctorName) {
        this.doctorName = doctorName;
        return this;
    }

    public AppointmentBuilder setAppointmentDate(Date appointmentDate) {
        this.appointmentDate = appointmentDate;
        return this;
    }

    public Appointment build() {
        return new Appointment(patient, doctorName, appointmentDate);
    }
}
