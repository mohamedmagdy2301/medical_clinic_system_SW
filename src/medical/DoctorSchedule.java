package medical;

public class DoctorSchedule {
    private String doctorName;
    private String workDays; // e.g., "Monday, Wednesday, Friday"
    private String workHours; // e.g., "9:00 AM - 5:00 PM"

    public DoctorSchedule(String doctorName, String workDays, String workHours) {
        this.doctorName = doctorName;
        this.workDays = workDays;
        this.workHours = workHours;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public String getWorkDays() {
        return workDays;
    }

    public String getWorkHours() {
        return workHours;
    }

    @Override
    public String toString() {
        return "Doctor: " + doctorName + "\nWork Days: " + workDays + "\nWork Hours: " + workHours;
    }
}
