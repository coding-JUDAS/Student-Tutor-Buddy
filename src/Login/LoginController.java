package Login;

import AdministratorLandingPage.Controller;
import StudentLandingPage.StudentLandingController;
import TutorLandingPage.TutorLandingController;
import Users.Administrator;
import Users.Student;
import Users.Tutor;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

public class LoginController {

    //Fields from the FXML document
    public TextField txtUsername;
    public Button btnLogin;
    public TextField txtPassword;
    public Button btnCancel;
    public RadioButton rbAdmin;
    public RadioButton rbTutor;
    public RadioButton rbStudent;
    
    Stage curStage = null;
    public static Scene curScene = null;

    //Attributes to note who is going to logon (All initially set to zero until the user information is verified)
    Student curStudent = null;
    Tutor curTutor = null;
    Administrator curAdmin = null;


    public void initialiseData(Stage stage){
        this.curStage = stage;
        initialiseRadioButtons();
    }

    private void initialiseRadioButtons(){
        //Initially set the student radio button to be selected
        rbAdmin.setSelected(false);
        rbTutor.setSelected(false);
        rbStudent.setSelected(true);
    }


    //Attributes used to access the database
    Connection connection = null;
    Statement statement = null;
    ResultSet set = null;
    private void connectToDatabase() throws SQLException {
        //Connect to the database
        try{
            //Establish a connection to the database
            connection = DriverManager.getConnection("jdbc:ucanaccess://resources//database//TutorBuddy.accdb");
            //Create the statement
            statement = connection.createStatement();
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

    public void setOnLoginPressed(ActionEvent actionEvent) throws IOException, SQLException {

        connectToDatabase();

        //First we read the data stored in the username text field
        try{
            //will be using a switch statement here to determine which landing page should be open from here
            String username = txtUsername.getText();
            String password = txtPassword.getText();

            //now we need to check what the user has selected as their role
            if(rbStudent.isSelected()){
                //Create a sql Query combining all the user table and the student table
                String query = "Select *" +
                        "From User Inner Join Student " +
                        "On User.UserId = Student.StudentNo";
                //Get the resultant set
                set = statement.executeQuery(query);

                //After getting the resultant set we search for the said student
                Boolean found = false;
                while(set.next() && !found){
                    //if the userId entered matches the student number
                    if(username.equals(set.getString("StudentNo"))){
                        //We create a new instance of student
                        System.out.println("Student has been found in the database");
                        curStudent = new Student(set.getString("StudentNo"),
                                set.getString("FirstName"),
                                set.getString("LastName"),
                                set.getString("EmailAddress"),
                                set.getInt("YearStudy"));

                        //region Remove the values in the txtField
                        txtUsername.setText("");
                        txtPassword.setText("");
                        //endregion

                        //The instance is sent through to the student landing page
                        curScene = ((Node)actionEvent.getSource()).getScene();
                        FXMLLoader loader = new FXMLLoader();
                        loader.setLocation(getClass().getResource("/StudentLandingPage/StudentLanding.fxml"));
                        Parent studentViewParent = loader.load();
                        Scene studentLandingScene = new Scene(studentViewParent);
                        //now that we have the scene we can access the StudentLandinController and pass through objects
                        StudentLandingController controller = loader.getController();
                        //Now we get the stage that the scene is on
                        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
                        //Now we send through the parameters
                        controller.initialiseData(curStudent, stage, curScene);
                        stage.setScene(studentLandingScene);
                        stage.show();
                        found = true;
                    }
                }
            }
            else if(rbAdmin.isSelected()){
                //Create a sql Query combining all the user table and the admin table
                String query = "Select *" +
                        "From User Inner Join Administrator " +
                        "On User.UserId = Administrator.StaffId";

                //Get the resultant set
                set = statement.executeQuery(query);
                Boolean found = false;
                while(set.next() && !found){
                    //if the userId entered matches the admin number
                    if(username.equals(set.getString("StaffId"))){
                        //We create a new instance of student
                        System.out.println("Admin has been found in the database");
                        curAdmin = new Administrator(set.getString("userId"),
                                set.getString("FirstName"),
                                set.getString("LastName"),
                                set.getString("EmailAddress"));

                        //region Remove the values in the txtField
                        txtUsername.setText("");
                        txtPassword.setText("");
                        //endregion

                        //The instance is sent through to the admin landing page
                        curScene = ((Node)actionEvent.getSource()).getScene();
                        FXMLLoader loader = new FXMLLoader();
                        loader.setLocation(getClass().getResource("/AdministratorLandingPage/Admin.fxml"));
                        Parent AdminViewParent = loader.load();
                        Scene AdminLandingScene = new Scene(AdminViewParent);
                        //now that we have the scene we can access the AdminLandinController and pass through objects
                        Controller controller = loader.getController();
                        //Now we get the stage that the scene is on
                        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
                        //Now we send through the parameters
                        controller.initialiseData(curAdmin, stage, curScene);
                        stage.setScene(AdminLandingScene);
                        stage.show();
                        found = true;
                    }
                }
            }
            else if(rbTutor.isSelected()){
                //Create a sql Query combining all the user table and the tutor table
                String query = "Select *" +
                        "From User Inner Join Tutor " +
                        "On User.UserId = Tutor.TutorId";

                //Get the resultant set
                set = statement.executeQuery(query);
                Boolean found = false;
                while(set.next() && !found){
                    if(username.equals(set.getString("TutorID"))){
                        // curTutor = new Tutor(set.getString("TutorID"),
                        //set.getDouble("HourlyRate"));
                        curTutor = new Tutor(set.getString("UserID"),
                                set.getString("FirstName"),
                                set.getString("LastName"),
                                set.getString("EmailAddress"),
                                set.getDouble("HourlyRate"));
                        curScene = ((Node)actionEvent.getSource()).getScene();
                        FXMLLoader loader = new FXMLLoader();
                        loader.setLocation(getClass().getResource("/TutorLandingPage/TutorLanding.fxml"));
                        Parent tutorViewParent = loader.load();
                        Scene tutorLandingScene = new Scene(tutorViewParent);
                        //now that we have the scene we can access the StudentLandingController and pass through objects
                        TutorLandingController controller = loader.getController();
                        //Now we get the stage that the scene is on
                        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
                        //Now we send through the parameters
                        controller.initialiseData(curTutor, stage, curScene);
                        stage.setScene(tutorLandingScene);
                        stage.show();
                        found = true;
                    }
                }
            }
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
        closeDataBase();
    }
    public void setOnCancelPressed(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Cancel logon");
        alert.setContentText("Are you sure you want to leave? ");

        if(alert.showAndWait().get() == ButtonType.OK)
            curStage.close();
    }
}
