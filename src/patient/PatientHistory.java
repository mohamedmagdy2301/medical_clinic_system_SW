package patient;

import medical.MedicalRecord;


public class PatientHistory extends MedicalRecord {
    private String historyText;
    private String surgeries;
    private String treatments;

    public PatientHistory(String historyText, String surgeries, String treatments) {
        this.historyText = historyText;
        this.surgeries = surgeries;
        this.treatments = treatments;
    }

    @Override
    public String getDetails() {
        return "History: " + historyText + ", Surgeries: " + surgeries + ", Treatments: " + treatments;
    }
}


