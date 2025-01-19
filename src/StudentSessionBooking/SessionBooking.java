package StudentSessionBooking;

import Users.Student;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.sql.*;
import java.util.ArrayList;

public class SessionBooking {
    public Label nameSname;
    public TableColumn<Session, String> moduleCode;
    public TableColumn<Session, Time> time;
    public TableColumn<Session, Date> date;
    public TableColumn<Session, String> tutorName;
    public TableColumn<Session, String> moduleName;
    public Button bookButton;
    public Button exitButton;
    public Button searchButton;
    public TableView<Session> tblSessions;
    public TextField txtSearch;

    private Scene curScene = null;
    private Scene prevScene = null;

    public void onExitButtonPressed(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Back");
        alert.setContentText("Are you sure you want to leave the page? ");

        if(alert.showAndWait().get() == ButtonType.OK){
            curStage.setScene(prevScene);
            curStage.show();
        }
    }

    public void onBookButtonPressed(ActionEvent actionEvent) throws SQLException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Booking");
        alert.setContentText("Press OK to confirm booking of this session...");

        if(alert.showAndWait().get() == ButtonType.OK){
            Session session = tblSessions.getSelectionModel().getSelectedItem();
            if(session != null){
                session.bookSession(curStudent);
            }
        }

    }

    public void onSearchButtonPressed(ActionEvent actionEvent) {
        String moduleCode = txtSearch.getText();
        sessions.clear();
        if(moduleCode.length() > 0){
            for(int x = 0; x < allSessions.size(); x++){
                if(allSessions.get(x).equals(moduleCode)){
                    sessions.add(allSessions.get(x));
                    System.out.println(allSessions.get(x));
                }
            }
        }
        if(sessions.size() < 1){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Session Not Found");
            alert.setContentText("There are no Sessions for the module " + txtSearch.getText() + ".\n" +
                    "Please type a different module...");

            if(alert.showAndWait().get() == ButtonType.OK)
                txtSearch.setText("");
        }
        else{
            System.out.println("The module has been found!");
        }
    }

    private ObservableList<Session> sessions;
    private Stage curStage = null;
    Student curStudent = null;
    private ArrayList<Session> allSessions = new ArrayList<>();
    public void initialiseData(Stage stage, Scene prevScene, Student student) throws SQLException {
        this.curStage = stage;
        this.prevScene = prevScene;
        this.curStudent = student;
        nameSname.setText(student.getFirstName() + " " + student.getLastName());
        sessions = FXCollections.observableArrayList();
        getAvailableSessions();
        //Set the value factories for the columns
        moduleCode.setCellValueFactory(new PropertyValueFactory<>("moduleCode"));
        moduleName.setCellValueFactory(new PropertyValueFactory<>("moduleName"));
        tutorName.setCellValueFactory(new PropertyValueFactory<>("tutorName"));
        date.setCellValueFactory(new PropertyValueFactory<>("date"));
        time.setCellValueFactory(new PropertyValueFactory<>("time"));
        tblSessions.setItems(sessions);
    }

    private void getAvailableSessions() {
        //we need to create a connection to the database
        try {
            connectToDatabase();
            //Create a string query for the data the we need
            String query = "Select GroupSession.SessionId, Module.ModuleCode, Module.ModuleName, User.FirstName, " +
                    "GroupSession.SessionDate, GroupSession.SessionTime " +
                    "From (((User Inner Join Tutor On User.UserId = Tutor.TutorId ) " +
                    "Inner Join GroupSession On Tutor.TutorId = GroupSession.TutorId) " +
                    "Inner Join Module on GroupSession.ModuleCode = Module.ModuleCode)";

            set = statement.executeQuery(query);

            while(set.next()){
                Session session = new Session(set.getString("SessionId"),set.getString("ModuleCode"),
                        set.getString("ModuleName"),
                        set.getString("FirstName"),
                        set.getDate("SessionDate"),
                        set.getTime("SessionTime"));

                allSessions.add(session);
            }
            closeDataBase();
        }
        catch(SQLException e){
            e.printStackTrace();
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
