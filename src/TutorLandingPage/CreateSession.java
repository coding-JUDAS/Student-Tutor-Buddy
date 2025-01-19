package TutorLandingPage;

import Login.LoginController;
import Users.Tutor;
import javafx.beans.binding.StringBinding;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

public class CreateSession {
    public Button btnCancel;
    public Button btnSave;
    public RadioButton radioBtnIndividualSession;
    public RadioButton radioBtnGroupSession;
    public Stage stage;

    public GridPane sessionDataGrid;

    public Label lblFee;
    public Label lblStudentId;
    public TextField tutorIdTxtField;
    public TextField moduleCodeTxtField;
    public TextField sessionTimeTxtField;
    public TextField numStudentsTxtField;
    public TextField studentIdTxtField;
    public TextField feeTxtField;
    public DatePicker sessionDatePicker;

    //Attributes used to access the database
    Connection connection = null;
    Statement statement = null;
    PreparedStatement prepStatement = null;
    ResultSet set = null;

    public CreateSession() throws SQLException {
    }

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

    private void closeDataBase() {
        //Method is used to close the database connection
        try{
            if(connection != null)
                connection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void cancelBtnPressed(ActionEvent actionEvent) throws IOException {
        stage.hide();
    }

    public void btnSavePressed(ActionEvent actionEvent) throws SQLException {
        connectToDatabase();
        System.out.println("Inserting new records...");
        StringBuilder sbGroup;
        if(radioBtnGroupSession.isSelected()){
            sbGroup = new StringBuilder("INSERT INTO GroupSession VALUES ");
            sbGroup.append("( '"+ System.currentTimeMillis() + "','" + tutorIdTxtField.getText() + "','" + moduleCodeTxtField.getText() + "','"+
                    sessionTimeTxtField.getText() + "','"+ sessionDatePicker.getValue().toString() + "','" +
                    numStudentsTxtField.getText() + "')");
            try {
                prepStatement = connection.prepareStatement(sbGroup.toString());
                prepStatement.execute();
                System.out.println("\tDone!");
            } catch (Exception e) {
                System.out.println("Could not insert new record... " + e.getMessage());
            }
        }
        else {
            sbGroup = new StringBuilder("INSERT INTO IndividualSession (SessionId, StudentNo, TutorId, ModuleCode, SessionDate," +
                    " SessionTime, Fee)");
            sbGroup.append(" VALUES ('" + System.currentTimeMillis() + "','" + studentIdTxtField.getText() + "','" + tutorIdTxtField.getText() +
                     "','" + moduleCodeTxtField.getText() +"','" + sessionDatePicker.getValue().toString() +
                    "','" + sessionTimeTxtField.getText() + "','" + feeTxtField.getText() +"')");
            try {
                prepStatement = connection.prepareStatement(sbGroup.toString());
                prepStatement.execute();
                System.out.println("\tDone!");
            } catch (Exception e) {
                System.out.println("Could not insert new record... " + e.getMessage());
            }
        }

    }

    Tutor tutor = null;
    Scene prevScene = null;
    public void initialiseStage(Stage curStage, Scene prevScene, Tutor tutor){
        stage = curStage;
        this.prevScene = prevScene;
        this.tutor = tutor;
        tutorIdTxtField.setText(tutor.getTutorId());
        radioBtnGroupSession.setSelected(true);
        radioBtnIndividualSession.setSelected(false);
        setVisibility(false);
    }
    public void setVisibility(Boolean SetVisible){
        feeTxtField.setVisible(SetVisible);
        lblFee.setVisible(SetVisible);
        studentIdTxtField.setVisible(SetVisible);
        lblStudentId.setVisible(SetVisible);
    }

    public void radioBtnIndividualSessionSelected(ActionEvent actionEvent) {
        radioBtnIndividualSession.setSelected(true);
        radioBtnGroupSession.setSelected(false);
        setVisibility(true);
    }

    public void radioBtnGroupSessionSelected(ActionEvent actionEvent) {
        radioBtnGroupSession.setSelected(true);
        radioBtnIndividualSession.setSelected(false);
        setVisibility(false);

    }
}
