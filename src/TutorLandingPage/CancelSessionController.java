package TutorLandingPage;

import Login.LoginController;
import Users.Tutor;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;

public class CancelSessionController {
    public Stage curStage = null;
    public Scene curScene = null;
    Scene prevScene = null;
    Stage stage = null;
    public TableView<Session> tblSession;
    public TableColumn<Session, Date> clmDate;
    public TableColumn<Session, Time> clmTime;
    public TableColumn<Session, String> clmModule;
    public Button btnLogOut;
    public Button btnCancel;
    public Button btnBack;


    public void onLogoutClicked(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Logout");
        alert.setHeaderText(" ");
        alert.setContentText("Confirm Logout...");
        if(alert.showAndWait().get() == ButtonType.OK) {
            Scene scene = LoginController.curScene;
            stage.setScene(scene);
            stage.show();
        }
    }

    public void onCancelClicked(ActionEvent actionEvent) throws SQLException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Cancel Session");
        alert.setContentText("Press OK to confirm cancelling of selected session...");

        if(alert.showAndWait().get() == ButtonType.OK){
           Session session = tblSession.getSelectionModel().getSelectedItem();
            System.out.println("cancelling session");
            if(session != null){
             boolean deleted= session.cancelSession();
             if(deleted)
             {
                 sessions.remove(session);
             }
            }
        }


    }

    public void onBackClicked(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Cancel logon");
        alert.setContentText("Are you sure you want to leave the page? ");

        if (alert.showAndWait().get() == ButtonType.OK) {
            curStage.setScene(prevScene);
            curStage.show();
        }
    }

    private ObservableList<Session> sessions;
    Tutor curTutor = null;
    private ArrayList<Session> allSessions = new ArrayList<>();
    ResultSet set = null;
    Connection connection = null;
    Statement statement = null;

    private void connectToDatabase() throws SQLException {
        //Connect to the database
        try {
            //We only need the user tables to find if we do have that user in the database
            //Establish a connection to the database
            connection = DriverManager.getConnection("jdbc:ucanaccess://resources//database//TutorBuddy.accdb");
            //Create the statement
            statement = connection.createStatement();
            System.out.println("Successfully connected to the database");
        } catch (SQLException e) {
            System.out.println("failed to connect to database");
        }
    }

    private void closeDataBase() {
        //Method is used to close the database connection
        try {
            if (connection != null)
                connection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void initialiseData(Stage nextStage, Scene curScene, Tutor tutor) {
        this.curStage = nextStage;
        this.prevScene = curScene;
        this.curTutor = tutor;
        sessions = FXCollections.observableArrayList();
        getSessions();
        clmDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        clmTime.setCellValueFactory(new PropertyValueFactory<>("time"));
        clmModule.setCellValueFactory(new PropertyValueFactory<>("moduleCode"));
        tblSession.setItems(sessions);

        //You have told JavaFX want you want to be represented in the table
        //Now you need to actually add stuff to the observable array so they can be displayed
        for(Session session: allSessions)
            sessions.add(session);


    }

    public void getSessions() {
        try {
            connectToDatabase();
            //query to get this tutor's sessions
            String query ="SELECT * FROM GroupSession "+
            "WHERE GroupSession.TutorId ='"+curTutor.getTutorId()+"'";
            set= statement.executeQuery(query);
            while(set.next())
            {
                Session session = new Session(set.getString("SessionId"),set.getString("ModuleCode"),
                        set.getString("TutorId"),
                        set.getDate("SessionDate"),
                        set.getTime("SessionTime"));
                allSessions.add(session);
            }
            query = "SELECT SessionId,ModuleCode, TutorId, SessionDate, SessionTime "+
                    "FROM IndividualSession "+
                    " WHERE TutorId='"+curTutor.getTutorId()+"'";
            set= statement.executeQuery(query);
            while(set.next())
            {
                Session session = new Session(set.getString("SessionId"),set.getString("ModuleCode"),
                        set.getString("TutorId"),
                        set.getDate("SessionDate"),
                        set.getTime("SessionTime"));
                allSessions.add(session);
            }
            closeDataBase();
            System.out.println("Locked and loaded");

        } catch (SQLException throwables) {
            throwables.printStackTrace();

        }
    }
}
