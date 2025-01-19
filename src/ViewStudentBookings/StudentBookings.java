package ViewStudentBookings;

import Users.Student;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.sql.*;
import java.util.Calendar;

public class StudentBookings {
    public Label nameSname;
    public TableColumn<Booking, Time> columnTime;
    public TableColumn<Booking, Date> columnDate;
    public TableColumn<Booking, String> columnTutor;
    public TableColumn<Booking, String> columnModuleName;
    public TableColumn<Booking, String> columnModuleCode;
    public TableView<Booking> tblViewBookings;
    public Button cancelButton;
    public Button confirmButton;
    public Button backButton;

    private Scene prevScene = null;
    private Scene curScene = null;

    public void onCancelButtonPressed(ActionEvent actionEvent) throws SQLException {
        Booking booking = tblViewBookings.getSelectionModel().getSelectedItem();
        if(booking != null){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Booking Confirmation");
            alert.setContentText("Click on OK to confirm this booking...");
            if(alert.showAndWait().get() == ButtonType.OK){
                booking.cancelBooking(curStudent);
                bookings.remove(tblViewBookings.getSelectionModel().getSelectedItem());
            }
        }
    }

    public void onConfirmButtonPressed(ActionEvent actionEvent) throws SQLException {
        Booking booking = tblViewBookings.getSelectionModel().getSelectedItem();
        if(booking!=null){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Booking Confirmation");
            alert.setContentText("Click on OK to confirm this booking...");
            if(alert.showAndWait().get() == ButtonType.OK){
                booking.confirmSession(curStudent);
            }
        }

    }

    public void onBackButtonPressed(ActionEvent actionEvent) {
        //Must still add
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Back the the previous page...");
        alert.setContentText("Are you sure you want to go back? ");

        if(alert.showAndWait().get() == ButtonType.OK){
            curStage.setScene(prevScene);
            curStage.show();
        }
    }

    private ObservableList<Booking> bookings;
    private Stage curStage = null;
    Student curStudent = null;

    public void initialiseData(Stage stage, Scene prevScene, Student student){
        this.curStage = stage;
        this.prevScene = prevScene;
        this.curStudent = student;
        nameSname.setText(student.getFirstName() + " " + student.getLastName());
        bookings = FXCollections.observableArrayList();
        getBookedSessions();
        //set the value factories for the columns
        columnModuleCode.setCellValueFactory(new PropertyValueFactory<>("moduleCode"));
        columnModuleName.setCellValueFactory(new PropertyValueFactory<>("moduleName"));
        columnTutor.setCellValueFactory(new PropertyValueFactory<>("tutorName"));
        columnDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        columnTime.setCellValueFactory(new PropertyValueFactory<>("time"));
        tblViewBookings.setItems(bookings);
    }

    private void getBookedSessions() {
        //We need to create a connection to the database so we can retrieve the data
        try{
            connectToDatabase();
            //Create a query that will retrieve all the information that we need
            String query = "Select GroupSession.SessionID, Module.ModuleCode, Module.ModuleName, User.FirstName, Tutor.TutorId, " +
                    "GroupSession.SessionDate, GroupSession.SessionTime, Attend.StudentNo " +
                    "From (((((User Inner Join Tutor on User.UserId = Tutor.TutorId) " +
                    "Inner Join GroupSession On Tutor.TutorId = GroupSession.TutorId) " +
                    "Inner Join Module On GroupSession.ModuleCode = Module.ModuleCode) " +
                    "Inner Join Attend On Attend.SessionId = GroupSession.SessionId) " +
                    "Inner Join Student On Attend.StudentNo = Student.StudentNo)";

            set = statement.executeQuery(query);

            while(set.next()){
                if(set.getString("StudentNo").equals(curStudent.getUserId())){
                    Booking booking = new Booking(set.getString("ModuleCode"),
                            set.getString("ModuleName"),
                            set.getString("TutorId"),
                            set.getString("FirstName"),
                            set.getDate("SessionDate"),
                            set.getTime("SessionTime"),
                            set.getString("SessionID"), "group");

                    bookings.add(booking);
                }
            }

            query = "Select IndividualSession.SessionId, Module.ModuleCode, Module.ModuleName, IndividualSession.TutorId, IndividualSession.SessionDate, " +
                    "IndividualSession.SessionTime, individualSession.StudentNo, User.FirstName\n" +
                    "From ((IndividualSession Inner Join Module On individualSession.ModuleCode = Module.ModuleCode) " +
                    "Inner Join Tutor On IndividualSession.TutorId = Tutor.TutorID) " +
                    "Inner Join User on Tutor.TutorID = User.UserId;";

            set = statement.executeQuery(query);
            while(set.next()){
                if(set.getString("StudentNo").equals(curStudent.getUserId())){
                    Booking booking = new Booking(set.getString("ModuleCode"),
                            set.getString("ModuleName"),
                            set.getString("TutorId"),
                            set.getString("FirstName"),
                            set.getDate("SessionDate"),
                            set.getTime("SessionTime"), set.getString("SessionId"), "single");

                    bookings.add(booking);
                }
            }
            closeDataBase();
        }
        catch (SQLException sq){
            sq.printStackTrace();
        }
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
