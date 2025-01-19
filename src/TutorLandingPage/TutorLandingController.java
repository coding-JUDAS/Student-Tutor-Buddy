package TutorLandingPage;

import Login.LoginController;
import StudentLandingPage.StudentLandingController;
import TutorLandingPage.FeeManager.FeeUpdate;
import Users.Tutor;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.sql.*;
import java.util.Optional;

public class TutorLandingController {

    public Button btnModules;
    public Button btnUpdateFee;
    public Button btnCreateSession;
    public Button btnCancelSession;
    public Button btnUploadDocuments;
    public Button btnUploadStudyMaterial;
    public Button logout;
    public Label nameAndSurname;
    private Scene curScene = null;
    private Scene prevScene = null;

    public void btnUpdateFeePressed(ActionEvent e) throws IOException, SQLException {
        FXMLLoader Loader = new FXMLLoader();
        Loader.setLocation(getClass().getResource("/TutorLandingPage/FeeManager/feeUpdateFXML.fxml"));
        DialogPane updateDialog = Loader.load();
        FeeUpdate controller = Loader.getController();
        controller.initialiseUpdate(tutor);

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setDialogPane(updateDialog);
        dialog.setTitle("Update fee");

        Optional<ButtonType> buttonOKClicked = dialog.showAndWait();
        if (buttonOKClicked.get() == ButtonType.APPLY){
            connectToDatabase();

            String query = "UPDATE Tutor SET HourlyRate = (?) WHERE TutorId = (?)";
            prepStatement = connection.prepareStatement(query);
            prepStatement.setDouble(1, tutor.getHourlyRate());
            prepStatement.setString(2, tutor.getTutorId());
            prepStatement.executeUpdate();

            Alert notify = new Alert(Alert.AlertType.INFORMATION);


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
            System.out.println("trying to connect a least...");
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
    public void btnCreateSessionPressed(ActionEvent actionEvent) throws IOException {
        openScreen(actionEvent);
    }
    public void openScreen(ActionEvent actionEvent) throws IOException {
        System.out.println("Creating session...");
        curScene = ((Node)actionEvent.getSource()).getScene();
        FXMLLoader Loader = new FXMLLoader();
        Loader.setLocation(getClass().getResource("/TutorLandingPage/CreateSession.fxml"));
        Parent createSessionViewParent = Loader.load();
        Scene createSessionLandingScene = new Scene(createSessionViewParent);
        //now that we have the scene we can access the StudentLandingController and pass through objects
        CreateSession controller = Loader.getController();
        //Now we get the stage that the scene is on
        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        //stage = new Stage(StageStyle.DECORATED);
        //Now we send through the parameters
        controller.initialiseStage(stage, curScene,tutor);
        stage.setScene(createSessionLandingScene);
        stage.show();
    }
    public void btnCancelSessionPressed(ActionEvent e) throws IOException {
      curScene = ((Node)e.getSource()).getScene();
      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(getClass().getResource("/TutorLandingPage/CancelSession.fxml"));
      Parent cancelSessionParent = loader.load();
      Scene cancelSessionScene = new Scene(cancelSessionParent);
      CancelSessionController controller= loader.getController();
      Stage nextStage = (Stage)((Node)e.getSource()).getScene().getWindow();
      controller.initialiseData(nextStage,curScene,tutor);
      nextStage.setScene(cancelSessionScene);
      nextStage.show();
    }

    Tutor tutor = null;
    Stage stage = null;

    /**
     * Method takes as input Student and stage.
     * @param tutor an instance of the class Tutor.
     * @param curStage stage of the scene.
     */
    public void initialiseData(Tutor tutor, Stage curStage, Scene prevScene){
        this.tutor = tutor;
        this.stage = curStage;
        this.prevScene = prevScene;
        nameAndSurname.setText(tutor.getFirstName() + " " + tutor.getLastName());
    }
    public void btnLogoutPressed(ActionEvent e){
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
}
