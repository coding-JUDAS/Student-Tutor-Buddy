package Users;

import java.io.File;

public class Student extends User{
    private int yearOfStudy;
    private File transcript = null;
    private File idDocument = null;

    //UserId in this case also represents the student number
    public Student(String userId, String firstName, String lastName,
                   String emailAddress, int yearOfStudy) {

        super(userId, firstName, lastName, emailAddress);
        this.yearOfStudy = yearOfStudy;
    }


}
