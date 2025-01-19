package ManageModules;

import Login.LoginController;
import StudentLandingPage.StudentLandingController;
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

public class DeleteModuleController {
    public TextArea txtModuleCode;
    public TextArea txtModuleName;
    public Button btnCancel;
    public Button btnConfirm;
    public Button btnBack;
    public Button btnLogOut;

    private Stage curStage = null;
    private Scene curScene = null;
    private Scene prevScene = null;



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

    public void onConfirmClicked(MouseEvent mouseEvent) {
        try{
            if(txtModuleName.getText() != null && txtModuleCode != null) {
                String moduleName = txtModuleName.getText();
                String moduleCode = txtModuleCode.getText();
                Module module = new Module(moduleCode, moduleName);
                //alerting the user that they are about to delete the module
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirm deletion of module");
                alert.setHeaderText("");
                alert.setContentText("You are about to delete the module "+moduleName+" with module code: "+moduleCode);

                //if user agrees to delete
                if(alert.showAndWait().get()==ButtonType.OK)
                {   Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                    alert1.setTitle("Upload Status");
                    alert1.setHeaderText(" ");
                    boolean deleted=  module.deleteFromDatabase();

                    if(deleted)
                    {
                        alert.setContentText("Module Successfully deleted");
                    }
                    else
                    {
                        alert.setContentText("Something went wrong, please try deleting again");
                    }

                    if(alert.showAndWait().get() == ButtonType.OK) {
                        txtModuleCode.setText("");
                        txtModuleName.setText("");
                        alert.close();
                    }
                }


            }
        }catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    public void initialiseData(Stage stage, Scene prevScene) {
        this.curStage = stage;
        this.prevScene = prevScene;
    }
    public void onBackClicked(ActionEvent actionEvent)
    {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Back");
        alert.setHeaderText(" ");
        alert.setContentText("Are you sure you want to leave this page?");
        if(alert.showAndWait().get() == ButtonType.OK){
            curStage.setScene(prevScene);
            curStage.show();
        }
    }
    public void onCancelClicked(ActionEvent actionEvent)
    {
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
