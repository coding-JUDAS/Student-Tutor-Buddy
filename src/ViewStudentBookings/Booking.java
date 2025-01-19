package ViewStudentBookings;

import Users.Student;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.sql.*;

public class Booking {
    private String type;
    private String bookingId;

    private String moduleCode;
    private String moduleName;

    private String tutorID;
    private String tutorName;

    private Date date;
    private Time time;

    public Booking(String moduleCode, String moduleName, String tutorID, String tutorName, Date date, Time time, String bookingId, String type) {
        this.moduleCode = moduleCode;
        this.moduleName = moduleName;
        this.tutorID = tutorID;
        this.tutorName = tutorName;
        this.date = date;
        this.time = time;

        this.bookingId = bookingId;
        this.type = type;
    }

    public String getModuleCode() {
        return moduleCode;
    }

    public String getModuleName() {
        return moduleName;
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

    public void cancelBooking(Student student) throws SQLException {

        if(this.type.equals("group")){
            connectToDatabase();
            String query = "Delete From Attend Where StudentNo='" + student.getUserId() + "'And SessionID = '"+this.bookingId+"';";
            statement.executeUpdate(query);
            Alert success = new Alert(Alert.AlertType.CONFIRMATION);
            success.setTitle("Booking Cancelled");
            success.setContentText("Booking has been Cancelled!");
            if(success.showAndWait().get() == ButtonType.OK)
                System.out.println("session has been cancelled");
            closeDataBase();
        }
        else{
            connectToDatabase();

            String query = "Delete From IndividualSession Where StudentNo='"+student.getUserId()+"'And SessionId = '"+this.bookingId+"';";
            statement.executeUpdate(query);
            Alert success = new Alert(Alert.AlertType.CONFIRMATION);
            success.setTitle("Booking Cancelled");
            success.setContentText("Booking has been Cancelled!");
            if(success.showAndWait().get() == ButtonType.OK)
                System.out.println("session has been cancelled");
            closeDataBase();
        }

    }

    public void confirmSession(Student student) throws SQLException {
        connectToDatabase();
        String query = "Update Attend Set Attended = Yes Where StudentNo = '" + student.getUserId() + "'";
        statement.executeUpdate(query);
        Alert success = new Alert(Alert.AlertType.CONFIRMATION);
        success.setTitle("Booking Confirmed");
        success.setContentText("Attendance has been confirmed...");
        if(success.showAndWait().get() == ButtonType.OK)
            System.out.println("Confirmed...");
        closeDataBase();
    }

    public Boolean equals(String moduleCode){
        return this.moduleCode.equals(moduleCode);
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
