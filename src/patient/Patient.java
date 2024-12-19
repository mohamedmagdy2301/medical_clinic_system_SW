package patient;

 
public class Patient {
    private String name;
    private String medicalHistory;

    // Constructor
    public Patient(String name, String medicalHistory) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Patient name cannot be empty");
        }
        this.name = name;
        this.medicalHistory = medicalHistory;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Patient name cannot be empty");
        }
        this.name = name;
    }

    public String getMedicalHistory() {
        return medicalHistory;
    }

    public void setMedicalHistory(String medicalHistory) {
        this.medicalHistory = medicalHistory;
    }
}
