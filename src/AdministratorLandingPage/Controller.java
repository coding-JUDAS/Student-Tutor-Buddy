package AdministratorLandingPage;

import Login.LoginController;
import ManageModules.AddModuleController;
import ManageModules.ManageModulesController;
import ReviewTutorApplication.ReviewController;
import SessionReportPage.ViewSessionReportController;
import Users.Administrator;
import View.ViewController;
import javafx.event.ActionEvent;
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

import java.io.*;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import java.util.StringTokenizer;

public class Controller implements Initializable {
    //controls from FXML file
    public Button btnView;
    public Button btnLogOut;
    public Button btnLoadStudents;
    public Button btnManageModule;
    public Button btnReviewTutorApps;

    Connection connection = null;
    Statement statement = null;
    ResultSet set = null;

    Scene prevScene = null;
    Scene curScene = null;

    Stage stage = null;

    Administrator admin = null;
/*
use to load the student registered that year into the database using a textfile
 */
    public void LoadStudentsClicked(MouseEvent mouseEvent) {
        try {
            //reading in student information from textfile
            BufferedReader in = new BufferedReader(new FileReader(new File("Students.txt")));
            while (in.ready()) {
                String s = in.readLine();
                StringTokenizer line = new StringTokenizer(s, ",");

                String stNum = line.nextToken();
                String fName = line.nextToken();
                String lname = line.nextToken();
                String emailAd = line.nextToken();
                int yearStudy = (Integer.parseInt(line.nextToken()));
                String usNum = line.nextToken();
//adding student information to relevant tables but first checking if student is not already in table

                        String sql = "INSERT INTO User(UserId, FirstName, LastName, EmailAddress,UserType) VALUES('" + stNum + "','" + fName + "','" + lname + "','" + emailAd + "','Student')";
                        String sql2 = "INSERT INTO Student(StudentNo, YearStudy) VALUES('" + stNum + "','" + yearStudy + "')";
                        try
                        {
                            statement.execute(sql);
                            statement.executeUpdate(sql2);

                        }
                        catch (SQLException e)
                        {
                            System.out.println("ERR: student could not be added");
                            e.printStackTrace();
                        }

                    }
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Upload Status");
            alert.setHeaderText(" ");

            alert.setContentText("The list of all registered students  has been successfully added...");


            if(alert.showAndWait().get() == ButtonType.OK)
                alert.close();
                }
        catch (Exception e) {
                    e.printStackTrace();
                    System.exit(1);
                }
    }



        public void ManageModulesClicked(MouseEvent mouseEvent) throws IOException {
            curScene = ((Node)mouseEvent.getSource()).getScene();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/ManageModules/ManageModules.fxml"));
            Parent manageModuleParent = loader.load();
            Scene manageModuleScene = new Scene(manageModuleParent);
            ManageModulesController controller = loader.getController();
            //Now we get the stage that the scene is on
            Stage stage = (Stage)((Node)mouseEvent.getSource()).getScene().getWindow();
            //Now we send through the parameters
            controller.initialiseData(stage, curScene);
            stage.setScene(manageModuleScene);
            stage.show();
        }




    public void ReviewTutorAppsClicked(MouseEvent mouseEvent) throws IOException {
        curScene = ((Node)mouseEvent.getSource()).getScene();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/ReviewTutorApplication/ReviewTutorApplications.fxml"));
        Parent ReviewApplicationParent = loader.load();
        Scene ReviewApplicationScene = new Scene(ReviewApplicationParent);
        ReviewController controller = loader.getController();
        //Now we get the stage that the scene is on
        Stage stage = (Stage)((Node)mouseEvent.getSource()).getScene().getWindow();
        //Now we send through the parameters
        controller.initialiseData(stage, curScene);
        stage.setScene(ReviewApplicationScene);
        stage.show();
    }


    public void ViewClicked(MouseEvent mouseEvent) throws IOException {
        curScene = ((Node)mouseEvent.getSource()).getScene();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/View/View.fxml"));
        Parent ViewParent = loader.load();
        Scene ViewScene = new Scene(ViewParent);
        ViewController controller = loader.getController();
        //Now we get the stage that the scene is on
        Stage stage = (Stage)((Node)mouseEvent.getSource()).getScene().getWindow();
        //Now we send through the parameters
        controller.initialiseData(stage, curScene);
        stage.setScene(ViewScene);
        stage.show();
    }

    public void onLOGOUTClicked(MouseEvent mouseEvent) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Cancel logon");
        alert.setContentText("Are you sure you want to leave? ");

        if(alert.showAndWait().get() == ButtonType.OK) {
            Scene logout = LoginController.curScene;
            stage.setScene(logout);
            stage.show();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // we start by getting access to the database
        try {
            connectToDatabase();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
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

            System.out.println("Successfully connected to the database");
        }
        catch(Exception e){
            System.out.println("failed to connect to database");
        }
    }

    public void initialiseData(Administrator curAdmin, Stage stage, Scene prevScene) {
        this.admin = curAdmin;
        this.stage = stage;
        this.prevScene = prevScene;
    }
}
