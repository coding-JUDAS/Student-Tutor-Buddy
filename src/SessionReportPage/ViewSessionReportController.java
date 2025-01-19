package SessionReportPage;

import Login.LoginController;
import ViewStudentBookings.StudentBookings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

public class ViewSessionReportController {
    public Button btnBack;
    public Button btnLogout;
    public TextField txtSearch;
    public TableView<SessionReport> tblReports;
    public TableColumn<SessionReport, String> colModuleName;
    public TableColumn<SessionReport, String> colTutor;
    public TableColumn<SessionReport, Date> colDate;
    public TableColumn<SessionReport, Time> colTime;
    public Button btnViewReport;
    public Button btnSearch;

    private Scene curScene = null;
    private Scene prevScene = null;
    private Stage stage = null;



    public void setOnBackClicked(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Go to Previous Page");
        alert.setContentText("Are you sure you want to leave the page? ");

        if(alert.showAndWait().get() == ButtonType.OK){
            stage.setScene(prevScene);
            stage.show();
        }
    }

    public void setOnLogoutClicked(ActionEvent actionEvent) {
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

    private ObservableList<SessionReport> reports;
    public void initialiseData(Stage stage, Scene prevScene){
        this.stage = stage;
        this.prevScene = prevScene;
        reports = FXCollections.observableArrayList();
        colModuleName.setCellValueFactory(new PropertyValueFactory<>("moduleName"));
        colTutor.setCellValueFactory(new PropertyValueFactory<>("tutorName"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colTime.setCellValueFactory(new PropertyValueFactory<>("time"));
        tblReports.setItems(reports);
    }

    /*
    private void initialiseObservableList(){
        reports = FXCollections.observableArrayList();
        colModuleName.setCellValueFactory(new PropertyValueFactory<>("moduleName"));
        colTutor.setCellValueFactory(new PropertyValueFactory<>("tutorName"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colTime.setCellValueFactory(new PropertyValueFactory<>("time"));
        tblReports.setItems(reports);
    }

     */

    public void setOnSearchClicked(ActionEvent actionEvent) {
        //Get the text from the text field
        String text = txtSearch.getText();
        reports.clear();
        if(text == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Incorrect Input...");
            alert.setHeaderText("");
            alert.setContentText("Please enter module code in the text area...");
            if(alert.showAndWait().get() == ButtonType.OK){
                txtSearch.setText("");
            }
            return;
        }

        try{
            connectToDatabase();
            //initialiseObservableList();
            String query = "Select SessionID, TutorId, ModuleCode, SessionDate, SessionTime\n" +
                    "From GroupSession;";
            System.out.println("query created");
            set = statement.executeQuery(query);
            System.out.println("statement executed");
            while(set.next()){
               if(text.toUpperCase().equals(set.getString("ModuleCode"))){
                   System.out.println(text.toUpperCase() + ", " + set.getString("ModuleCode"));
                   SessionReport report = new SessionReport(set.getString("SessionID"),
                           set.getString("TutorId"), set.getString("ModuleCode"),
                           set.getDate("SessionDate"), set.getTime("SessionTime"),
                           "group");
                   reports.add(report);
               }
            }


            query = "Select SessionId, TutorId, ModuleCode, SessionDate, SessionTime\n" +
                    "From IndividualSession;";
            System.out.println("individual query created");
            set = statement.executeQuery(query);
            System.out.println("statement executed");
            while(set.next()){
                if(text.toUpperCase().equals(set.getString("ModuleCode"))){
                    System.out.println(text.toUpperCase() + ", " + set.getString("ModuleCode"));
                    SessionReport report = new SessionReport(set.getString("SessionId"),
                            set.getString("TutorId"), set.getString("ModuleCode"),
                            set.getDate("SessionDate"), set.getTime("SessionTime"),
                            "single");
                    reports.add(report);
                }
            }

            closeDataBase();
        }
        catch(SQLException exception){
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

    public void setOnViewReportClicked(ActionEvent actionEvent) throws IOException {
        SessionReport sessionReport = tblReports.getSelectionModel().getSelectedItem();
        if(sessionReport == null)
            return;


        curScene = ((Node)actionEvent.getSource()).getScene();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/SessionReportPage/IndividualSessionReport.fxml"));
        Parent individualSessionReportParent = loader.load();
        Scene individualSessionReportScene = new Scene(individualSessionReportParent);
        //now that we have the scene we can access the StudentBookingController and pass through objects
        IndividualSessionReport controller = loader.getController();
        //Now we get the stage that the scene is on
        Stage nextStage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        //Now we send through the parameters
        controller.initialiseData(nextStage, curScene, sessionReport);
        nextStage.setScene(individualSessionReportScene);
        nextStage.show();
    }
}
