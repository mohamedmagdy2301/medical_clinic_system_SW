package doctor;


import doctor.Doctor;

public class DoctorFactory {
    public static Doctor createDoctor(String specialty) {
        switch (specialty.toLowerCase()) {
            case "cardiologist":
                return new Cardiologist();
            case "neurologist":
                return new Neurologist();
            default:
                return null;
        }
    }
}
