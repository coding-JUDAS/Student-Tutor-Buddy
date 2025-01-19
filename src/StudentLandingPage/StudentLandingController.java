package StudentLandingPage;

import Login.LoginController;
import StudentLandingPage.FileManager.UploadDocumentsController;
import StudentLandingPage.Reflection.ReflectionController;
import StudentSessionBooking.SessionBooking;
import StudentViewInvoice.StudentViewInvoiceController;
import Users.Student;
import ViewStudentBookings.StudentBookings;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;


public class StudentLandingController {
    public Button btnViewInvoice;
    public Label nameSname;
    public Button btnBookSession;
    public Button btnReflection;
    public Button btnAttendanceReport;
    public Button btnTutorApplication;
    public Button btnLogout;
    public Button btnViewBookings;

    private Scene curScene = null;
    private Scene prevScene = null;

    public void btnBookSessionPressed(ActionEvent actionEvent) throws IOException, SQLException {
        //The instance is sent through to the student landing page
        curScene = ((Node)actionEvent.getSource()).getScene();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/StudentSessionBooking/SessionBooking.fxml"));
        Parent studentBookingParent = loader.load();
        Scene studentBookingScene = new Scene(studentBookingParent);
        //now that we have the scene we can access the StudentBookingController and pass through objects
        SessionBooking controller = loader.getController();
        //Now we get the stage that the scene is on
        Stage nextStage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        //Now we send through the parameters
        controller.initialiseData(nextStage, curScene,student);
        nextStage.setScene(studentBookingScene);
        nextStage.show();
    }

    public void btnSetOnViewBookingsPressed(ActionEvent actionEvent) throws IOException, SQLException {
        //The instance is sent through to the student landing page
        curScene = ((Node)actionEvent.getSource()).getScene();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/ViewStudentBookings/StudentBookings.fxml"));
        Parent studentViewBookingParent = loader.load();
        Scene studentViewBookingScene = new Scene(studentViewBookingParent);
        //now that we have the scene we can access the StudentBookingController and pass through objects
        StudentBookings controller = loader.getController();
        //Now we get the stage that the scene is on
        Stage nextStage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        //Now we send through the parameters
        controller.initialiseData(nextStage, curScene, student);
        nextStage.setScene(studentViewBookingScene);
        nextStage.show();
    }

    public void btnReflectionPressed(ActionEvent actionEvent) throws IOException {
        FXMLLoader Loader = new FXMLLoader();
        Loader.setLocation(getClass().getResource("/StudentLandingPage/Reflection/ReflectFXML.fxml"));
        Parent reflectionParent = Loader.load();

        Scene uploadDocumentsScene = new Scene(reflectionParent);

        ReflectionController controller = Loader.getController();
        controller.initializer(student, stage);

        stage.setScene(uploadDocumentsScene);
        stage.show();
    }

    public void btnViewInvoicePressed(ActionEvent actionEvent) throws IOException {
        //The instance is sent through to the student landing page
        curScene = ((Node)actionEvent.getSource()).getScene();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/StudentViewInvoice/StudentViewInvoice.fxml"));
        Parent studentViewInvoice = loader.load();
        Scene studentViewBookingScene = new Scene(studentViewInvoice);
        //now that we have the scene we can access the StudentBookingController and pass through objects
        StudentViewInvoiceController controller = loader.getController();
        //Now we get the stage that the scene is on
        Stage nextStage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        //Now we send through the parameters
        controller.initialiseData(nextStage, curScene, student);
        nextStage.setScene(studentViewBookingScene);
        nextStage.show();
    }

    public void btnTutorApplicationPressed(ActionEvent actionEvent) throws IOException {
        FXMLLoader Loader = new FXMLLoader();
        Loader.setLocation(getClass().getResource("/StudentLandingPage/FileManager/UploadDocumentsFXML.fxml"));
        Parent uploadDocumentsParent = Loader.load();

        Scene uploadDocumentsScene = new Scene(uploadDocumentsParent);

        UploadDocumentsController controller = Loader.getController();
        controller.initializeDocumentUploader(student, stage);

        stage.setScene(uploadDocumentsScene);
        stage.show();
    }

    //The following attributes will be used to store data attributes
    Student student = null;
    Stage stage = null;

    /**
     * The method accepts a Student instance, as well as the stage created in the previous scene
     * so that the close/cancel buttons can easily be utilised.
     * @param student - Student that is logged on
     * @param curStage - stage that the scene is on.
     */
    public void initialiseData(Student student, Stage curStage, Scene prevScene){
        this.student = student;
        this.stage = curStage;
        this.prevScene = prevScene;
        nameSname.setText(student.getFirstName() + " " + student.getLastName());
    }

    public void btnLogoutPressed(ActionEvent actionEvent) {
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
