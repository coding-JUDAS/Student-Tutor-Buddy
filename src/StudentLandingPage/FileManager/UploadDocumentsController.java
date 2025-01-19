package StudentLandingPage.FileManager;

import StudentLandingPage.StudentLandingController;
import Users.Student;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

import java.sql.*;
import java.util.ResourceBundle;

public class UploadDocumentsController implements Initializable {


    public Student student = null;
    public Stage stage;
    public Label lblNameSurname;

    @FXML
    Spinner gradeSelector;
    @FXML
    ChoiceBox moduleSelector;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //We need to get access to the database
        try {
            connectToDatabase();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }
    //Attributes used to access the database
    Connection connection = null;
    Statement statement = null;
    PreparedStatement prepStatement = null;
    ResultSet set = null;
    private void connectToDatabase() throws SQLException {
        //Connect to the database
        try{
            //We only need the user tables to find if we do have that user in the database
            //Establish a connection to the database
            connection = DriverManager.getConnection("jdbc:ucanaccess://resources//database//TutorBuddy.accdb");
            //Create the statement
            statement = connection.createStatement();
            //Execute the SQL query that you require
            String query = "Select * from Student";

            System.out.println("Successfully connected to the database");
        }
        catch(Exception e){
            System.out.println("failed to connect to database");
        }
    }
    public void setOnUploadTranscriptAction(ActionEvent e){

    }
    public void initializeDocumentUploader(Student student, Stage stage){
        this.student = student;
        this.stage = stage;
        lblNameSurname.setText(student.getFirstName() + " " + student.getLastName());

        try {
            populateModules();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //Spinner configuration
        gradeSelector.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(70, 100, 70));
    }
    Scene curScene = null;
    public void btnCancelOnAction(ActionEvent actionEvent) throws IOException {

        curScene = ((Node)actionEvent.getSource()).getScene();

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/StudentLandingPage/StudentLanding.fxml"));

        Parent studentLandingParent = loader.load();

        Scene studentLandingScene = new Scene(studentLandingParent);

        StudentLandingController controller = loader.getController();
        Scene curScene = studentLandingScene;
        controller.initialiseData(student, stage, curScene);

        stage.setScene(studentLandingScene);
        stage.show();
    }
    /**
     * This method populates the moduleChoiceBox by reading entries that are
     * already available in the dataBase.
     * @throws SQLException if dataBase has no entries.
     */
    public void populateModules() throws SQLException {
        String q = "SELECT * FROM Module ";
        set = statement.executeQuery(q);
        while(set.next()){
            String moduleCode = set.getString("ModuleCode");
            moduleSelector.getItems().addAll(moduleCode);
        }
    }

    public void btnSubmitOnAction(ActionEvent actionEvent) throws SQLException{
        connectToDatabase();
        String query = "INSERT INTO Application (StNum, Module, Mark) " +
                "VALUES(?, ?, ?)";
        try {
            prepStatement = connection.prepareStatement(query);
            prepStatement.setString(1, student.getUserId());
            prepStatement.setString(2, moduleSelector.getValue().toString());
            prepStatement.setInt(3, Integer.parseInt(gradeSelector.getValue().toString()));
            prepStatement.executeUpdate();
            Alert uploadSuccess = new Alert(Alert.AlertType.INFORMATION);
            uploadSuccess.setHeaderText(" ");
            uploadSuccess.setContentText("Application submitted.");
            if (uploadSuccess.showAndWait().get() == ButtonType.OK){
                uploadSuccess.close();
            }
        } catch (Exception e) {
            Alert uploadFail = new Alert(Alert.AlertType.ERROR);
            uploadFail.setTitle("Error");
            uploadFail.setContentText("Complete all missing fields or select close option to return.");
            if (uploadFail.showAndWait().get() == ButtonType.CLOSE){
                uploadFail.close();
            }
            System.out.println("failed . . ");
            e.printStackTrace();
        }
    }
}
