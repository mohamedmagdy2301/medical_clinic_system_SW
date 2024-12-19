package medical;

import patient.PatientHistory;

public class MedicalRecordFactory {
    public static MedicalRecord createRecord(String type, String historyText, String surgeries, String treatments) {
        if ("History".equalsIgnoreCase(type)) {
            return new PatientHistory(historyText, surgeries, treatments);
        }
        // Add more record types if needed
        return null;
    }
}