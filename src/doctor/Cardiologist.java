package doctor;


import doctor.Doctor;

public class Cardiologist implements Doctor {
    @Override
    public void performDuty() {
        System.out.println("Cardiologist is performing heart-related duties.");
    }
}

