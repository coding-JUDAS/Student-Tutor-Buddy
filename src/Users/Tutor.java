package Users;

public class Tutor extends User{
    private Double hourlyRate;

    //UserId in this case also represents the tutorId
    public Tutor(String userId, String firstName, String lastName,
                 String emailAddress, Double hourlyRate) {

        super(userId, firstName, lastName, emailAddress);
        this.hourlyRate = hourlyRate;
    }

    public String getTutorId(){
        return userId;
    }

    public Double getHourlyRate() { return hourlyRate;
    }

    public void setHourlyRate(double parseDouble) {
    }
}
