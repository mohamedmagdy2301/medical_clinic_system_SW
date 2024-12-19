package appointment;

import java.util.Date;
import patient.Patient;
public class Appointment implements Prototype {
    private Patient patient;
    private Date appointmentDate;
    private String doctorName;

    public Appointment(Patient patient, String doctorName, Date appointmentDate) {
        this.patient = patient;
        this.appointmentDate = appointmentDate;
        this.doctorName = doctorName;
    }

 
    @Override
    public Appointment clone() {
        // Create a new copy of the appointment with the same properties
        return new Appointment(patient, doctorName, appointmentDate);
    }

    // Getters and setters, toString()

     public Patient getPatient() {
        return patient;
    }

    public Date getAppointmentDate() {
        return appointmentDate;
    }

    public String getDoctorName() {
        return doctorName;
    }

    @Override
    public String toString() {
        return "Appointment with Dr. " + doctorName + " on " + appointmentDate;
    }
}