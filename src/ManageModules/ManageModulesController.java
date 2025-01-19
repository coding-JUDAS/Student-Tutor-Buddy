package ManageModules;

import AdministratorLandingPage.Controller;
import Login.LoginController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ManageModulesController {

    public Button btnAdd;
    public Button btnDelete;

    Stage curStage = null;
    private Scene prevScene = null;
    private Scene curScene = null;

    public void initialiseData(Stage stage, Scene prevScene
    ){
        this.curStage = stage;
        this.prevScene = prevScene;
    }

    public void onLOGOUTClicked(MouseEvent mouseEvent) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Cancel logon");
        alert.setContentText("Are you sure you want to leave? ");

        if(alert.showAndWait().get() == ButtonType.OK) {
            Scene logout = LoginController.curScene;
            curStage.setScene(logout);
            curStage.show();
        }
    }

    public void onBackClicked(MouseEvent mouseEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Back");
        alert.setHeaderText(" ");
        alert.setContentText("Are you sure you want to leave this page?");
        if(alert.showAndWait().get() == ButtonType.OK) {
            curStage.setScene(prevScene);
            curStage.show();
        }
    }

    public void onDeleteModuleClicked(MouseEvent mouseEvent) throws IOException {
        curScene = ((Node)mouseEvent.getSource()).getScene();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/ManageModules/Delete Modules.fxml"));
        Parent deleteModuleParent = loader.load();
        Scene DeleteModuleScene = new Scene(deleteModuleParent);
        //now that we have the scene we can access the DeleteModuleController and pass through objects
        DeleteModuleController controller = loader.getController();
        //Now we get the stage that the scene is on
        Stage stage = (Stage)((Node)mouseEvent.getSource()).getScene().getWindow();
        //Now we send through the parameters
        controller.initialiseData(stage, curScene);
        stage.setScene(DeleteModuleScene);
        stage.show();
    }

    public void onAddModuleClicked(MouseEvent mouseEvent) throws IOException {
        curScene = ((Node)mouseEvent.getSource()).getScene();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/ManageModules/AddModule.fxml"));
        Parent addModuleParent = loader.load();
        Scene addModuleScene = new Scene(addModuleParent);
        //now that we have the scene we can access the AdminLandinController and pass through objects
        AddModuleController controller = loader.getController();
        //Now we get the stage that the scene is on
        Stage stage = (Stage)((Node)mouseEvent.getSource()).getScene().getWindow();
        //Now we send through the parameters
        controller.initialiseData(stage, curScene);
        stage.setScene(addModuleScene);
        stage.show();
    }


}
