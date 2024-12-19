package doctor;


import doctor.Doctor;

public class Neurologist implements Doctor {
    @Override
    public void performDuty() {
        System.out.println("Neurologist is performing brain-related duties.");
    }
}
