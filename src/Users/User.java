package Users;

public class User {

    protected String userId;
    protected String firstName;
    protected String lastName;
    protected String emailAddress;

    public User(String userId, String firstName, String lastName, String emailAddress) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailAddress = emailAddress;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public String getUserId(){
        return userId;
    }
}
