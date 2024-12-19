package patient;


import patient.Patient;
import java.util.List;
import java.util.ArrayList;

public class PatientDatabaseManager {

    private static PatientDatabaseManager instance;
    private List<Patient> patients;

    private PatientDatabaseManager() {
        patients = new ArrayList<>();
    }

    public static PatientDatabaseManager getInstance() {
        if (instance == null) {
            instance = new PatientDatabaseManager();
        }
        return instance;
    }

    public void addPatient(Patient patient) {
        patients.add(patient);
    }

    public List<Patient> getPatients() {
        return patients;
    }
}
