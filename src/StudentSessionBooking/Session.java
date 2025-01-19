package StudentSessionBooking;

import Users.Student;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.sql.*;

public class Session {
    private final String sessionId;
    public String moduleCode;
    public String moduleName;

    public String tutorId;
    public String tutorName;

    public Date date;
    public Time time;

    public Session(String sessionId, String moduleCode, String moduleName, String tutorName, Date date, Time time) {
        this.sessionId = sessionId;
        this.moduleCode = moduleCode;
        this.moduleName = moduleName;
        this.tutorName = tutorName;
        this.date = date;
        this.time = time;
    }

    public String getSessionId() {
        return sessionId;
    }

    public String getModuleCode() {
        return moduleCode;
    }

    public String getModuleName() {
        return moduleName;
    }

    public String getTutorId() {
        return tutorId;
    }

    public String getTutorName() {
        return tutorName;
    }

    public Date getDate() {
        return date;
    }

    public Time getTime() {
        return time;
    }

    //Method is used to determine if the ID searched by the user is equal to the ID of the module code
    public Boolean equals(String moduleCode){
        return (this.moduleCode.equals(moduleCode));
    }

    //method connects to the database and books a session for the said student
    public void bookSession(Student student) throws SQLException {

        connectToDatabase();
        Boolean found = false;
        String check = "Select * From Attend";
        set = statement.executeQuery(check);
        while(set.next() && !found){
            if(set.getString("StudentNo").equals(student.getUserId()) &&
                    set.getString("SessionID").equals(this.sessionId)){
                found = true;
            }
        }
        if(!found){
            String query = "Insert Into Attend(SessionId, StudentNo)" +
                    "Values('" + this.sessionId + "','" + student.getUserId() + "')";

            statement.executeUpdate(query);
            //Let the user know that we have inserted into the database
            Alert success = new Alert(Alert.AlertType.CONFIRMATION);
            success.setTitle("Session Booked");
            success.setContentText("Session has been booked successfully");
            if(success.showAndWait().get() == ButtonType.OK)
                System.out.println("session booked");
        }
        else{
            Alert success = new Alert(Alert.AlertType.CONFIRMATION);
            success.setTitle("Session Booked");
            success.setContentText("You are already booked for this session...");
            if(success.showAndWait().get() == ButtonType.OK)
                System.out.println("session already booked");
        }
        closeDataBase();
    }

    //Attributes used to access the database
    Connection connection = null;
    Statement statement = null;
    ResultSet set = null;
    private void connectToDatabase() throws SQLException {
        //Connect to the database
        try{
            //We only need the user tables to find if we do have that user in the database
            //Establish a connection to the database
            connection = DriverManager.getConnection("jdbc:ucanaccess://resources//database//TutorBuddy.accdb");
            //Create the statement
            statement = connection.createStatement();
            System.out.println("Successfully connected to the database");
        }
        catch(SQLException e){
            System.out.println("failed to connect to database");
        }
    }

    private void closeDataBase() {
        //Method is used to close the database connection
        try{
            if(connection != null)
                connection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
