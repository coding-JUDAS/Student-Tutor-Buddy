package View;

import Login.LoginController;
import SessionReportPage.SessionReport;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.Date;
import java.util.ResourceBundle;

public class ViewASController {
    public Button btnBack;
    public Button btnLogOut;
    public Button btnSearch;
    public TextField txtSearch;
    public TableView<SessionsSummary> tblAttendance;
    public TableColumn<SessionsSummary, String> tbcMod;
    public TableColumn<SessionsSummary, Date> tbcDate;
    public TableColumn<SessionsSummary, Time> tbcTime;
    public TableColumn<SessionsSummary,String> tbcTutor;
    public TableColumn<SessionsSummary, String> tbcStNum;


    private Stage stage = null;
    private Scene curScene = null;
    private Scene prevScene = null;

    private ObservableList<SessionsSummary> sessions;
    public void initialiseData(Stage stage, Scene prevScene){
        this.stage = stage;
        this.prevScene = prevScene;
        sessions = FXCollections.observableArrayList();
      tbcMod.setCellValueFactory(new PropertyValueFactory<>("moduleCode"));
        tbcTutor.setCellValueFactory(new PropertyValueFactory<>("tutor"));
        tbcDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        tbcTime.setCellValueFactory(new PropertyValueFactory<>("time"));
        tbcStNum.setCellValueFactory(new PropertyValueFactory<>("stNum"));
        tblAttendance.setItems(sessions);

    }

    public void onLOGOUTClicked(MouseEvent mouseEvent) throws IOException {
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


    public void onBackClicked(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Go to Previous Page");
        alert.setContentText("Are you sure you want to leave the page? ");

        if(alert.showAndWait().get() == ButtonType.OK){
            stage.setScene(prevScene);
            stage.show();}
    }

    public void onSearchClicked(ActionEvent actionEvent) throws SQLException {
        String text = txtSearch.getText();
        sessions.clear();
        if(text == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Incorrect Input....");
            alert.setHeaderText("");
            alert.setContentText("Please enter module code in the text area...");
            if(alert.showAndWait().get() == ButtonType.OK){
                txtSearch.setText("");
            }
            return;
        }
        try {
            connectToDatabase();
            //initialiseObservableList();
            String query = "Select SessionID, TutorId, ModuleCode, StudentNo, SessionDate, SessionTime\n" +
                    "From IndividualSession WHERE Attend =Yes";
            System.out.println("query created");
            set = statement.executeQuery(query);
            System.out.println("statement executed");
            while (set.next()) {
                if (text.toUpperCase().equals(set.getString("ModuleCode"))) {
                    System.out.println(text.toUpperCase() + ", " + set.getString("ModuleCode"));
                    SessionsSummary report = new SessionsSummary(set.getString("ModuleCode"),set.getDate("SessionDate"),
                            set.getTime("SessionTime"),
                            set.getString("TutorId"), set.getString("StudentNo"));
                    sessions.add(report);
                }
            }
            query = "Select GroupSession.TutorId, GroupSession.ModuleCode, GroupSession.SessionDate, GroupSession.SessionTime, Attend.StudentNo "+
                    "From (GroupSession Inner Join Attend On GroupSession.SessionId = Attend.SessionId)"+
                    "WHERE Attended = Yes";
            set = statement.executeQuery(query);
            System.out.println("statement, executed");
            while (set.next()) {
                if (text.toUpperCase().equals(set.getString("ModuleCode")))
                {
                    System.out.println(text.toUpperCase() + ", " + set.getString("ModuleCode"));
                    SessionsSummary report = new SessionsSummary(set.getString("ModuleCode"),
                            set.getDate("SessionDate"),
                            set.getTime("SessionTime"),
                            set.getString("TutorId"),
                            set.getString("StudentNo"));
                    sessions.add(report);
                }
            }
            closeDataBase();

        } catch(SQLException exception){
            System.out.println("Something definitely went wrong connecting to the database");
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
