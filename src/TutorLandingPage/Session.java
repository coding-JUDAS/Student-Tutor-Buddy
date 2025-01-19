package TutorLandingPage;

import Users.Tutor;
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

    public Session(String sessionId, String moduleCode, String tutorId, Date date, Time time) {
        this.sessionId = sessionId;
        this.moduleCode = moduleCode;
        this.tutorId = tutorId;
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
    public Boolean equals(String moduleCode) {
        return (this.moduleCode.equals(moduleCode));
    }


     public Boolean cancelSession() throws SQLException {

        connectToDatabase();
         boolean found = false;
         boolean group = false;
         String check = "Select * From IndividualSession ";
         set = statement.executeQuery(check);
         while(set.next()&&!found)
         {
             Session session = new Session(set.getString("SessionId"),set.getString("ModuleCode"),
                     set.getString("TutorId"),
                     set.getDate("SessionDate"),
                     set.getTime("SessionTime"));
             if(session.sessionId.equals(this.sessionId))
                 found= true;

         }
         check = "Select * From GroupSession";
         set = statement.executeQuery(check);
         while(set.next()&&!found)
         {
             Session session = new Session(set.getString("SessionId"),set.getString("ModuleCode"),
                     set.getString("TutorId"),
                     set.getDate("SessionDate"),
                     set.getTime("SessionTime"));
             if(session.sessionId.equals(this.sessionId))
                 found= true;
             group = true;
         }closeDataBase();
         connectToDatabase();
         if(found)
         {
             String deleteQuery;
             if(group)
            {
                deleteQuery = "Delete From GroupSession Where SessionId='" + this.sessionId + "'";
            }
            else {
                 deleteQuery = "Delete From IndividualSession Where SessionId='" + this.sessionId + "'";
             }
             statement.executeUpdate(deleteQuery);
             Alert success = new Alert(Alert.AlertType.CONFIRMATION);
             success.setTitle("Session Canceled");
             success.setContentText("Session has been canceled successfully");
             if(success.showAndWait().get() == ButtonType.OK)
                 System.out.println("session canceled");
         }
         closeDataBase();
         if(found)
             return true;
             return false;
     }

    //Attributes used to access the database
    Connection connection = null;
    Statement statement = null;
    ResultSet set = null;
    ResultSet resultSet = null;
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
