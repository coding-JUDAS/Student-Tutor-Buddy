package View;

import Login.LoginController;
import SessionReportPage.ViewSessionReportController;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class ViewController {
    Connection connection = null;
    Statement statement = null;
    ResultSet set = null;

    Scene prevScene = null;
    Scene curScene = null;
    Stage stage=null;


    public void onLogOutClicked(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Cancel logon");
        alert.setContentText("Are you sure you want to leave? ");

        if(alert.showAndWait().get() == ButtonType.OK) {
            Scene logout = LoginController.curScene;
            stage.setScene(logout);
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

    public void onViewSessionSummary(ActionEvent actionEvent) throws IOException {
        //The instance is sent through to the student landing page
        curScene = ((Node)actionEvent.getSource()).getScene();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/SessionReportPage/ViewSessionReport.fxml"));
        Parent viewSessionReportParent = loader.load();
        Scene viewSessionReportScene = new Scene(viewSessionReportParent);
        //now that we have the scene we can access the StudentBookingController and pass through objects
        ViewSessionReportController controller = loader.getController();
        //Now we get the stage that the scene is on
        Stage nextStage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        //Now we send through the parameters
        controller.initialiseData(nextStage, curScene);
        nextStage.setScene(viewSessionReportScene);
        nextStage.show();
    }

    public void onViewAttendanceSummary(ActionEvent actionEvent) throws IOException {
        //The instance is sent through to the student landing page
        curScene = ((Node)actionEvent.getSource()).getScene();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/View/ViewAttendanceSummary.fxml"));
        Parent viewASParent = loader.load();
        Scene viewASScene = new Scene(viewASParent);
        //now that we have the scene we can access the StudentBookingController and pass through objects
        ViewASController controller = loader.getController();
        //Now we get the stage that the scene is on
        Stage nextStage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        //Now we send through the parameters
        controller.initialiseData(nextStage, curScene);
        nextStage.setScene(viewASScene);
        nextStage.show();
    }

    public void onViewBookingsReport(ActionEvent actionEvent) throws IOException {

    }

    public void initialiseData(Stage stage, Scene curScene) {
        this.prevScene = curScene;
        this.stage = stage;
    }
}
