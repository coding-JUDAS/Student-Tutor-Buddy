package SessionReportPage;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.sql.*;

public class IndividualSessionReport {
    public TableView<Rating> tblRatings;
    public TableColumn<Rating, String> colComment;
    public TableColumn<Rating, Integer> colRating;
    public Button btnBack;
    public Label lblDate;
    public Label lblTutor;
    public Label lblSessionType;
    public Label lblRating;


    private Scene curScene = null;
    private Scene prevScene = null;
    private Stage stage = null;

    private ObservableList<Rating> ratings;


    public void initialiseData(Stage stage, Scene scene, SessionReport report){
        this.stage = stage;
        this.prevScene = scene;

        ratings = FXCollections.observableArrayList();
        loadRatings(report);

        colComment.setCellValueFactory(new PropertyValueFactory<>("comment"));
        colRating.setCellValueFactory(new PropertyValueFactory<>("rating"));
        tblRatings.setItems(ratings);

        //set the labels
        lblDate.setText("Date:\t " + report.getDate().toString());
        lblTutor.setText(report.getTutorName());
        lblSessionType.setText("Session Type:\t " + report.getSessionType());
        calculateRating();
    }

    private void loadRatings(SessionReport report) {
        try{
            connectToDatabase();
            String query;
            if(report.getSessionType().equals("single")){
                query = "Select SessionId, Comment, Rating, StudentNo, SessionDate, SessionTime, TutorId\n" +
                        "From IndividualSession;";
                set = statement.executeQuery(query);
                while(set.next()){
                    if(report.getSessionId().equals(set.getString("SessionId"))){

                        Rating rating = new Rating(report.getSessionId(), report.getSessionType(), report.getTutorName());
                        rating.setComment(set.getString("Comment"));
                        System.out.println("The rating number is..." + set.getString("Rating"));
                        rating.setRating(Integer.parseInt(set.getString("Rating")));
                        rating.setDate(set.getDate("SessionDate"));
                        rating.setTime(set.getTime("SessionTime"));
                        ratings.add(rating);
                    }
                }
            }
            else{
                query = "Select GroupSession.SessionID, Attend.Comment, Attend.Rating, Attend.StudentNo, GroupSession.SessionDate, " +
                        "GroupSession.SessionTime, GroupSession.TutorId\n" +
                        "From(Attend Inner Join GroupSession On Attend.SessionId = GroupSession.SessionID);";

                set = statement.executeQuery(query);
                while(set.next()){
                    if(report.getSessionId().equals(set.getString("SessionID"))){

                        Rating rating = new Rating(report.getSessionId(), report.getSessionType(), report.getTutorName());
                        rating.setComment(set.getString("Comment"));
                        rating.setRating(Integer.parseInt(set.getString("Rating")));
                        rating.setDate(set.getDate("SessionDate"));
                        rating.setTime(set.getTime("SessionTime"));
                        ratings.add(rating);
                    }
                }
            }

            closeDataBase();
        }
        catch (SQLException e){
            System.out.println("Something went wrong...");
        }
    }

    private void calculateRating(){
        int sum = 0;
        for(int x = 0; x < ratings.size(); x++){
            sum += ratings.get(x).getRating();
        }

        if(sum == 0)
            lblRating.setText("5.0/5");
        else
            lblRating.setText(sum*1.0/ratings.size() + "/5");
    }

    //Attributes used to access the database
    Connection connection = null;
    Statement statement = null;
    ResultSet set = null;
    private void connectToDatabase() throws SQLException {
        //Connect to the database
        try{
            //We only need the user tables to find if we do have that user in the database
            //Establish a connection to the database
            connection = DriverManager.getConnection("jdbc:ucanaccess://resources//database//TutorBuddy.accdb");
            //Create the statement
            statement = connection.createStatement();
            System.out.println("Successfully connected to the database");
        }
        catch(SQLException e){
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

    public void setOnBackClicked(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Go to Previous Page");
        alert.setContentText("Are you sure you want to leave the page? ");

        if(alert.showAndWait().get() == ButtonType.OK){
            stage.setScene(prevScene);
            stage.show();
        }
    }
}
