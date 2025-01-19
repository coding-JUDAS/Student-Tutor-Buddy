package ManageModules;

import Login.LoginController;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AddModuleController{
    public Button btnLogOut;
    public Button btnBack;
    public TextArea txtModuleName;
    public TextArea txtModuleCode;
    public Button btnAdd;
    public Button btnCancel;

    private Stage curStage = null;
    private Scene curScene = null;
    Scene prevScene = null;



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

    public void OnBackBtnClicked(MouseEvent mouseEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Back");
        alert.setHeaderText(" ");
        alert.setContentText("Are you sure you want to leave this page?");
        if(alert.showAndWait().get() == ButtonType.OK) {
            Scene logout = LoginController.curScene;
            curStage.setScene(logout);
            curStage.show();
        }
    }

    public void onAddBtnClicked(MouseEvent mouseEvent) {
        try{
            if(txtModuleName.getText() != null && txtModuleCode != null){
                String moduleName = txtModuleName.getText();
                String moduleCode = txtModuleCode.getText();
                Module module = new Module(moduleCode, moduleName);
                boolean added = module.addToDatabase();
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Upload Status");
                alert.setHeaderText(" ");
                if(added){
                    alert.setContentText("The module has been successfully added...");
                }
                else{
                    alert.setContentText("The module is already in the database...");
                    txtModuleName.setPromptText("Please enter a different module name here...");
                    txtModuleCode.setPromptText("Please enter a different module code here...");
                }

                if(alert.showAndWait().get() == ButtonType.OK) {
                    txtModuleCode.setText("");
                    txtModuleName.setText("");
                    alert.close();
                }
            }
            else{
                txtModuleName.setPromptText("Please enter module name here...");
                txtModuleCode.setPromptText("Please enter module code here...");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void OnCancelBtnClicked(MouseEvent mouseEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Cancellation");
        alert.setHeaderText(" ");
        alert.setContentText("Are you sure you want to leave this page?");
        if(alert.showAndWait().get() == ButtonType.OK){
            curStage.setScene(prevScene);
            curStage.show();
        }
    }


    public void initialiseData(Stage stage, Scene prevScene) {
        this.curStage = stage;
        this.prevScene = prevScene;
    }

    public void onBackBtnPressed(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Cancellation");
        alert.setHeaderText(" ");
        alert.setContentText("Are you sure you want to leave this page?");
        if(alert.showAndWait().get() == ButtonType.OK){
            curStage.setScene(prevScene);
            curStage.show();
        }
    }

}
