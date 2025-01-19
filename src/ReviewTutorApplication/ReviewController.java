package ReviewTutorApplication;

import Login.LoginController;
import StudentSessionBooking.Session;
import Users.Tutor;
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
import net.ucanaccess.complex.Attachment;

import javax.swing.*;
import java.io.*;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ReviewController implements Initializable {
    public Stage curStage = null;
    public Scene curScene = null;
    Scene prevScene = null;
    Stage stage = null;

    public TableView<Application> tblApplications;
    public TableColumn<Application, String> tbcStNum;
    public TableColumn<Application, String> tbcMod;
    public TableColumn<Application, Integer> tbcTranscript;
    public Button btnDone;
    public Button btnReject;
    public Button btnApprove;
    public Button btnLogOut;
    public Button btnBack;
    public Button btnCheckDocuments;


    private ObservableList<Application> applications;
    private ArrayList<Application> allApplications = new ArrayList<>();
    ResultSet set = null;
    Connection connection = null;
    Statement statement = null;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

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

    public void onLOGOUTClicked(MouseEvent mouseEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/Login/LoginFXML.fxml"));
        Parent LogOutParent = loader.load();
        Scene LogIn = new Scene(LogOutParent);
        //now that we have the scene we can access the StudentLandinController and pass through objects
        LoginController controller = loader.getController();
        //Now we get the stage that the scene is on
        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        //Now we send through the parameters
        controller.initialiseData(stage);
        stage.setScene(LogIn);
        stage.show();
    }

    public void onDoneClicked(MouseEvent mouseEvent) {

        curStage.setScene(prevScene);
        curStage.show();

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

    public void onApproveClicked(ActionEvent actionEvent) throws SQLException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Approval of Tutor Application");
        alert.setContentText("Press OK to confirm acceptance of Tutor...");

        if(alert.showAndWait().get() == ButtonType.OK){
          Application application = tblApplications.getSelectionModel().getSelectedItem();
            if(application != null){
                application.approveTutor();
            }
        }

    }

    public void onRejectClIcked(ActionEvent actionEvent) throws SQLException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Rejection of Tutor Application");
        alert.setContentText("Press OK to confirm rejection of Tutor...");

        if(alert.showAndWait().get() == ButtonType.OK){
            Application application = tblApplications.getSelectionModel().getSelectedItem();
            if(application != null){
                application.rejectTutor();
            }
        }

    }

    public void initialiseData(Stage nextStage, Scene curScene) {
        this.curStage = nextStage;
        this.prevScene = curScene;
        applications = FXCollections.observableArrayList();
        getApplications();
        tbcStNum.setCellValueFactory(new PropertyValueFactory<>("StNum"));
        tbcTranscript.setCellValueFactory(new PropertyValueFactory<>("Mark"));
        tbcMod.setCellValueFactory(new PropertyValueFactory<>("Module"));

        tblApplications.setItems(applications);

        for (Application application : allApplications) {
            applications.add(application);
        }

    }

    private void getApplications() {
        try {
            connectToDatabase();
            //query to get this tutor's sessions
            String query = "SELECT * FROM Application";
            set = statement.executeQuery(query);
            while (set.next()) {
                Application application = new Application(set.getInt("ID"),set.getString("StNum"),
                        set.getString("Module"),
                        set.getInt("Mark"));
                allApplications.add(application);
            }
            closeDataBase();
            System.out.println("Locked and loaded");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void onApplicationSelected(ActionEvent mouseEvent) throws SQLException, IOException {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Choose Document to View");
       // int somf = fileChooser.showOpenDialog(stage);
        File file = fileChooser.getSelectedFile();
        System.out.println(file.getAbsoluteFile());



    }
}
