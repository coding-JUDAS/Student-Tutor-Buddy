package StudentLandingPage.Reflection;

import Login.LoginController;
import StudentLandingPage.StudentLandingController;
import Users.Student;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class ReflectionController implements Initializable {

    public Button btnBack;

    /**
     * TableView items to hold session objects.
     */
    public TableView reflectionTableView;
    public TableColumn sessionIDTableViewColumn;
    public TableColumn attendedTableViewColumn;
    public TableColumn ratingTableViewColumn;
    public TableColumn commentTableViewColumn;
    public TextField txtFieldComment;


    public Button btnUploadComment;
    public Label NameSurname;
    public Spinner ratingSpinner;

    //Attributes used to access the database
    Connection connection = null;
    Statement statement = null;
    PreparedStatement prepStatement = null;
    ResultSet set = null;

    /**
     * Method establishes connection to Database using the JDBC.
     * @throws SQLException when connection is not successful.
     */
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Set columns
        try {
            initializeTableView();
            txtFieldComment.setVisible(false);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }
    /**
     * This will return an ObservableList of attended sessions.
     */
    public ObservableList<Attend> getAttended() throws SQLException {
        ObservableList<Attend> attendedSessions = FXCollections.observableArrayList();
        connectToDatabase();
        String query = "SELECT * FROM Attend";
        set = statement.executeQuery(query);
        while(set.next()){
            attendedSessions.add(new Attend(
                    set.getInt("sessionId"),
                    set.getString("StudentNo"),
                    set.getBoolean("Attended"),
                    set.getString("Comment"),
                    set.getInt("Rating")));
        }
        return  attendedSessions;
    }

    /**
     * This method Initialises the TableView
     */
    public void initializeTableView() throws SQLException {
        //Set up the columns in the TableView.
        sessionIDTableViewColumn.setCellValueFactory(new PropertyValueFactory<>("sessionID"));
        attendedTableViewColumn.setCellValueFactory(new PropertyValueFactory<>("boolStringValue"));
        commentTableViewColumn.setCellValueFactory(new PropertyValueFactory<>("Comment"));
        ratingTableViewColumn.setCellValueFactory(new PropertyValueFactory<>("Rating"));

        // Load objects to tableView.
        attendedTableViewColumn.getTableView().setItems(getAttended());

        //Allow for the comment column to be editable.
        attendedTableViewColumn.setEditable(true);

        // Configure spinner
        SpinnerValueFactory<Integer> ratingValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 5);
        ratingSpinner.setValueFactory(ratingValueFactory);



    }
    public String getComment(){
        return txtFieldComment.getText();
    }
    public void addCommentToTableView(String comment){
        Attend attend = (Attend) reflectionTableView.getSelectionModel().getSelectedItem();
        attend.setComment(getComment());
    }
    Attend attend = null;
    public void tableViewSelectedItem() throws SQLException {
        attend = (Attend) reflectionTableView.getSelectionModel().getSelectedItem();
        if (attend.getComment() == null || attend.getComment() == " "){
            commentTableViewColumn.setCellFactory(TextFieldTableCell.forTableColumn());
            txtFieldComment.setVisible(true);
            btnUploadComment.setVisible(true);
            ratingSpinner.setVisible(true);
        }
        else{
            btnUploadComment.setVisible(false);
            ratingSpinner.setVisible(false);
            System.out.println(attend.getComment());
        }
    }

    /**
     * Method will perform update on database upon confirmation.
     * Method begins by establishing connection to database.
     * Update is performed if connection is established.
     * @throws SQLException if connection fails.
     */
    public void uploadToDataBase() throws SQLException {
        connectToDatabase();
        String q = "UPDATE Attend SET Comment = ?, Rating = ? WHERE SessionId = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(q);
        preparedStatement.setString(1, txtFieldComment.getText());
        preparedStatement.setString(2, String.valueOf(ratingSpinner.getValue()));
        preparedStatement.setString(3, String.valueOf(attend.getSessionID()));
        preparedStatement.executeUpdate();
    }

    private Student student = null;
    private Stage stage = null;
    public void initializer(Student student, Stage curStage){
        this.student = student;
        this.stage = curStage;
        NameSurname.setText(student.getFirstName() + " " + student.getLastName());
    }

    /**
     * Method to return to the previous scene.
     * @param actionEvent back button pressed action.
     * @throws IOException if stage fails to initialize.
     */
    public void btnBackOnAction(ActionEvent actionEvent) throws IOException {
        Scene curScene = ((Node)actionEvent.getSource()).getScene();
        FXMLLoader Loader = new FXMLLoader();
        Loader.setLocation(getClass().getResource("/StudentLandingPage/StudentLanding.fxml"));
        Parent studentLandingParent = Loader.load();

        StudentLandingController controller = Loader.getController();
       // Scene curScene = studentLandingParent.getScene();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Logout");
        alert.setHeaderText(" ");
        alert.setContentText("Are you sure you want to go back to the home page?");
        if(alert.showAndWait().get() == ButtonType.OK) {
            controller.initialiseData(this.student, this.stage, curScene);

            Scene tutorLandingScene = new Scene(studentLandingParent);
            this.stage.setScene(tutorLandingScene);
            this.stage.show();
        }

    }

    /**
     * Helper method to upload comments on session attended.
     * @param actionEvent triggers the upload action;
     * @throws SQLException
     */
    public void btnUploadCommentOnAction(ActionEvent actionEvent) {
        try {
            uploadToDataBase();
            initializeTableView();
            Alert uploadAlert = new Alert(Alert.AlertType.INFORMATION);
            uploadAlert.setTitle("Uploading Comment. . .");
            uploadAlert.setContentText("Upload successful");
            if(uploadAlert.showAndWait().get() == ButtonType.OK){
                uploadAlert.close();
            }
        } catch (SQLException e) {
            Alert uploadFail = new Alert(Alert.AlertType.ERROR);
            uploadFail.setTitle("Fail");
            uploadFail.setContentText("Comment not saved");
            if (uploadFail.showAndWait().get() == ButtonType.CLOSE){
                uploadFail.close();
            }
            e.printStackTrace();
        }

    }
}
