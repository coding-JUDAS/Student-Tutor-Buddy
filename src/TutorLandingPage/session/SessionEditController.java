package TutorLandingPage.session;

import javafx.scene.control.*;
import javafx.util.converter.LocalTimeStringConverter;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalTime;
import java.time.format.FormatStyle;

public class SessionEditController {
    public TextField txtFieldStudentNo;
    public ChoiceBox choiceBoxModuleCode;
    public DatePicker datePickerSessionDate;
    public Spinner spinnerSessionTime;
    public TextField txtFieldNumStudents;
    public TextField txtFieldFee;
    public RadioButton radioBtnGroupSession;
    public RadioButton radioBtnIndividualSession;
    public Label lblNumStudents;
    public Label lblFee;

    SessionClass session = null;

    // Initialize DialogPane
    public void initialize(){
        radioBtnGroupSession.setSelected(true);
        txtFieldFee.setVisible(false);
        lblFee.setVisible(false);

        // Initialize time spinner
        spinnerSessionTime.setEditable(true);

        //Spinner configuration
        SpinnerValueFactory<LocalTime> LocalTimeSpinnerValueFactory = new SpinnerValueFactory<LocalTime>() {
            {
                setConverter(new LocalTimeStringConverter(FormatStyle.SHORT));
            }

            @Override
            public void decrement(int steps) {
                if (this.getValue() == null){
                    setValue(LocalTime.now());
                }
                else{
                    LocalTime time = (LocalTime) getValue();
                    setValue(time.minusMinutes(steps));
                }
            }

            @Override
            public void increment(int steps) {
                if(this.getValue() == null){
                    setValue(LocalTime.now());
                }
                else{
                    LocalTime time = (LocalTime) getValue();
                    setValue(time.plusMinutes(steps));

                }
            }
        };
        this.spinnerSessionTime.setValueFactory(LocalTimeSpinnerValueFactory);
        this.spinnerSessionTime.setEditable(true);
        this.spinnerSessionTime.setFocusTraversable(true);
    }

    //Create Session
    public SessionClass createSession(){
        session = new SessionClass();
        session.setModCode(choiceBoxModuleCode.getSelectionModel().getSelectedItem().toString());
        session.setSessionType("Group");
        session.setDate(Date.valueOf(datePickerSessionDate.getValue()));
        session.setTime((Time) spinnerSessionTime.getValue());
        if (radioBtnGroupSession.isSelected()){

            session.setNumStds(Integer.parseInt(txtFieldNumStudents.getText()));
        }
        else {
            session.setSessionType("Individual");
            session.setFee(Float.parseFloat(txtFieldFee.getText()));
        }

       return session;
    }
}
