package Users;

public class Administrator extends User{
    //userId in this case also represents the staff number
    public Administrator(String userId, String firstName,
                         String lastName, String emailAddress) {
        super(userId, firstName, lastName, emailAddress);
    }
}
