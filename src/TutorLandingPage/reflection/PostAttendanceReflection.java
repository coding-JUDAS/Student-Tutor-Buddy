package TutorLandingPage.reflection;

import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;

public class PostAttendanceReflection {

    public Label txtBoxName;
    public TextField txtBoxAttributes;
    public Label q1;
    public Label q2;
    public Label q3;
    public Label q4;
    public Label q5;
    public Label q6;
    public Label q7;
    public Label q8;

    public RadioButton opt1;
    public RadioButton opt2;
    public RadioButton opt3;
    public RadioButton opt4;
    public RadioButton opt5;

    public void initializeRadioButtons(){
        opt1.setSelected(false);
        opt2.setSelected(false);
        opt3.setSelected(false);
        opt4.setSelected(false);
        opt5.setSelected(true);
    }
}
