package TutorLandingPage.FeeManager;

import Users.Tutor;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class FeeUpdate {

    public Label lblFee;
    public Button btnUpdate;
    public TextField txtFieldFee;
    private Tutor tutor = null;
    private Stage stage = null;

    public void initialiseUpdate(Tutor tutor){
        this.tutor = tutor;
        lblFee.setText(tutor.getHourlyRate().toString());
       // this.stage = stage;


    }


    public void btnApplyAction(MouseEvent mouseEvent) {

    }

    public void updateAction(ActionEvent actionEvent) {
        tutor.setHourlyRate(Double.parseDouble(txtFieldFee.getText()));
        lblFee.setText(this.tutor.getHourlyRate().toString());
    }
}
