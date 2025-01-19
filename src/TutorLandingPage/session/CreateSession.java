package TutorLandingPage.session;

import TutorLandingPage.TutorLandingController;
import Users.Tutor;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.converter.LocalTimeStringConverter;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.FormatStyle;

public class CreateSession {
    public Button btnCancel;
    public Button btnSave;
    public RadioButton radioBtnIndividualSession;
    public RadioButton radioBtnGroupSession;
    public Stage stage;
    public GridPane sessionDataDialog;
    public Button btnAddSession;
    public Button btnDeleteSession;


    private Tutor tutor;
    private Scene scene;

    //TableView Items
    public TableView<SessionClass> sessionTableView;
    public TableColumn<SessionClass, Integer> sessionIdColumn;
    public TableColumn<SessionClass, String> moduleCodeColumn;
    public TableColumn<SessionClass, LocalDate> SessionDateColumn;
    public TableColumn<SessionClass, LocalTime> SessionTimeColumn;
    public TableColumn<SessionClass, String> sessionTypeColumn;

    public Spinner<LocalTime> timeSpinner;

    public Label lblNumStudents;
    public Label lblSessionTime;
    public Label lblSessionDate;
    public Label lblModuleCode;


    public GridPane sessionDataGrid;

    public Label lblFee;
    public Label lblStudentId;
    public TextField numStudentsTxtField;
    public TextField studentIdTxtField;
    public TextField feeTxtField;
    public DatePicker sessionDatePicker;


    //ChoiceBox items
    @FXML
    private ChoiceBox moduleChoiceBox;

    //Attributes used to access the database
    Connection connection = null;
    Statement statement = null;
    PreparedStatement prepStatement = null;
    ResultSet set = null;

    ObservableList<SessionClass> s;

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

    /**
     * This method populates the moduleChoiceBox by reading entries that are
     * already available in the dataBase.
     * @throws SQLException if dataBase has no entries.
     */
    public void populateModules() throws SQLException {
        String q = "SELECT * FROM Module ";
        set = statement.executeQuery(q);
        while(set.next()){
            String moduleCode = set.getString("ModuleCode");
            moduleChoiceBox.getItems().add(moduleCode);
        }
    }

    /**
     * Helper method to populate the tableview items.
     * @throws SQLException
     */
    public void populateTableView() throws SQLException {

        sessionTableView.setItems(getSessions());
    }
    /**
     * This method Initialises the TableView
     */
    public void initializeTableView() {
        //Set up the columns in the TableView.
        sessionIdColumn.setCellValueFactory(new PropertyValueFactory<>("sessionId"));
        moduleCodeColumn.setCellValueFactory(new PropertyValueFactory<>("modCode"));
        SessionTimeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));
        SessionDateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        sessionTypeColumn.setCellValueFactory(new PropertyValueFactory<>("SessionType"));

        timeSpinner.setEditable(true);
    }

    /**
     * This method returns an ObservableList of sessions.
     */
    public ObservableList<SessionClass> getSessions() throws SQLException {
        ObservableList<SessionClass> sessions = FXCollections.observableArrayList();
        connectToDatabase();
        String query = "SELECT * FROM IndividualSession";
        set = statement.executeQuery(query);
        SessionClass session;
        while(set.next()){
            sessions.add(new SessionClass(
                    set.getInt("SessionId"),
                    set.getString("ModuleCode"),
                    set.getDate("SessionDate"),
                    set.getTime("SessionTime"),
                    "Individual"));

        }
        query = "SELECT * FROM GroupSession";
        set = statement.executeQuery(query);
        while(set.next()){
            sessions.add(new SessionClass(
                    set.getInt("SessionId"),
                    set.getString("ModuleCode"),
                    set.getDate("SessionDate"),
                    set.getTime("SessionTime"),
                    "Group"));

        }
        return  s = sessions;

    }

    /**
     * Method closes Session Landing page and returns to the previous scene.
     * @param e Triggers Close action when the cancel button is used.
     */
    public void btnCancelPressed(ActionEvent e){

        Alert cancelAlert = new Alert(Alert.AlertType.CONFIRMATION);
        cancelAlert.setTitle("Exit");
        cancelAlert.setContentText("Are you sure you want to EXIT?");
        if (cancelAlert.showAndWait().get() == ButtonType.YES){
            cancelAlert.close();
        }

        FXMLLoader Loader = new FXMLLoader();
        Loader.setLocation(getClass().getResource("/TutorLandingPage/TutorLanding.fxml"));
        Parent tutorLandingParent = null;
        try {
            tutorLandingParent = Loader.load();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        Scene tutorLandingScene = new Scene(tutorLandingParent);

        TutorLandingController controller = Loader.getController();
        controller.initialiseData(tutor, stage,scene);

        stage.setScene(tutorLandingScene);
        stage.show();
    }

    /**
     * This method will upload entries to database when the save button is pressed.
     * Method uploads to the appropriate table when the radio button is toggled from the UI.
     * @param actionEvent Listens to action on button to trigger upload sequence.
     * @throws SQLException
     */
    public void btnSavePressed(ActionEvent actionEvent) throws SQLException {
        System.out.println("Inserting new records...");

        Alert confirmSave = new Alert(Alert.AlertType.CONFIRMATION);
        confirmSave.setTitle("Cancel logon");
        confirmSave.setContentText("Are you sure you want to leave? ");

        if(confirmSave.showAndWait().get() == ButtonType.OK) {
            confirmSave.close();
        }
        else {
            return;
        }
        if(radioBtnGroupSession.isSelected()){
            try {
                String query = "INSERT INTO GroupSession (TutorId, ModuleCode, SessionDate," +
                        "SessionTime, NoStudents) VALUES (?, ?, ?, ?, ?)";
                prepStatement = connection.prepareStatement(query);
                prepStatement.setString(1, tutor.getUserId());
                prepStatement.setString(2, moduleChoiceBox.getValue().toString());
                prepStatement.setDate(3, Date.valueOf(sessionDatePicker.getValue()));
                prepStatement.setTime(4, Time.valueOf(timeSpinner.getValue()));
                prepStatement.setInt(5, Integer.parseInt(numStudentsTxtField.getText()));
                prepStatement.execute();
                System.out.println("\tDone!");

            } catch (Exception e) {
                System.out.println("Could not insert new record... " + e.getMessage());
            }
        }
        else if(radioBtnIndividualSession.isSelected()) {
            try {
                String query = "INSERT INTO IndividualSession (TutorId, ModuleCode, SessionDate, SessionTime, Fee)" +
                        "VALUES(?, ?, ?, ?, ?)";
                prepStatement = connection.prepareStatement(query);
                prepStatement.setString(1, tutor.getTutorId());
                prepStatement.setString(2, moduleChoiceBox.getValue().toString());
                prepStatement.setDate(3, Date.valueOf(sessionDatePicker.getValue()));
                prepStatement.setTime(4, Time.valueOf(timeSpinner.getValue()));
                prepStatement.setFloat(5, Float.parseFloat(feeTxtField.getText()));
                prepStatement.execute();
                System.out.println("\tDone!");
            } catch (Exception e) {
                System.out.println("Could not insert new record... " + e.getMessage());
                return;
            }
        }
        populateTableView();
        Alert notify = new Alert(Alert.AlertType.INFORMATION);
        notify.setTitle("Create Session");
        notify.setContentText("Session saved.");
        if (notify.showAndWait().get() == ButtonType.OK){
            notify.close();
        }

    }


    public void radioBtnIndividualSessionSelected(ActionEvent actionEvent) {
        radioBtnIndividualSession.setSelected(true);
        radioBtnGroupSession.setSelected(false);
        numStudentsTxtField.setVisible(false);
    }

    public void radioBtnGroupSessionSelected(ActionEvent actionEvent) {
        radioBtnGroupSession.setSelected(true);
        radioBtnIndividualSession.setSelected(false);
        numStudentsTxtField.setVisible(true);

    }

    public void initialiseStage(Stage curStage, Tutor tutor, Scene scene) throws SQLException {
        connectToDatabase();
        stage = curStage;
        this.tutor = tutor;
        this.scene = scene;
        radioBtnGroupSession.setSelected(true);
        radioBtnIndividualSession.setSelected(false);

        // ChoiceBox configuration
        initializeTableView();
        populateModules();
        populateTableView();



        //Spinner configuration
        SpinnerValueFactory<LocalTime> localTimeSpinnerValueFactory = new SpinnerValueFactory() {
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
        this.timeSpinner.setValueFactory(localTimeSpinnerValueFactory);
        this.timeSpinner.setEditable(true);
        this.timeSpinner.setFocusTraversable(true);
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

    public void btnAddSessionAction(ActionEvent actionEvent) throws IOException {

        SessionClass session;
        try{
            if (radioBtnIndividualSession.isSelected()){
                session = new SessionClass(tutor.getTutorId(),
                        moduleChoiceBox.getValue().toString(),
                        Date.valueOf(sessionDatePicker.getValue()),
                        Time.valueOf(timeSpinner.getValue()),
                        Float.parseFloat(feeTxtField.getText()),
                        "Individual");
                s.add(session);
            }
            else if (radioBtnGroupSession.isSelected()){
                session = new SessionClass(moduleChoiceBox.getValue().toString(),
                        " ",
                        Integer.parseInt(numStudentsTxtField.getText()),
                        Date.valueOf(sessionDatePicker.getValue()),
                        Time.valueOf(timeSpinner.getValue()),
                        "GROUP");
                s.add(session);

            }
            Alert addAlert = new Alert(Alert.AlertType.INFORMATION);
            addAlert.setTitle("Add Session");
            addAlert.setHeaderText(" ");
            addAlert.setContentText("New Session Added !");
            if (addAlert.showAndWait().get() == ButtonType.OK){
                addAlert.close();
            }
        }catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Missing or incorrect values");
            if (alert.showAndWait().get() == ButtonType.CLOSE){
                alert.close();
            }
            e.printStackTrace();
        }

    }

    public void btnDeleteSessionAction(ActionEvent actionEvent) {
    }
}
