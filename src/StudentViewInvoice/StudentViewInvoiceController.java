package StudentViewInvoice;

import StudentSessionBooking.Session;
import Users.Student;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.sql.*;

public class StudentViewInvoiceController {
    public Label nameSname;
    public TableView tblView;
    public TableColumn <InvoiceItem,String>colSessionId;
    public TableColumn <InvoiceItem, Date>colDate;
    public TableColumn <InvoiceItem, Double>colAmount;
    public Button btnClose;
    public Label lblTotal;



    public void setOnCloseClicked(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Back");
        alert.setContentText("Are you sure you want to leave the page? ");

        if(alert.showAndWait().get() == ButtonType.OK){
            stage.setScene(prevScene);
            stage.show();
        }
    }

    private Scene prevScene = null;
    Student student;
    Stage stage = null;

    private ObservableList<InvoiceItem> invoices;
    
    public void initialiseData(Stage stage, Scene scene, Student student){
        this.student = student;
        this.stage = stage;
        this.prevScene = scene;
        nameSname.setText(student.getFirstName() + " " + student.getLastName());

        invoices = FXCollections.observableArrayList();


        colAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));

        tblView.setItems(invoices);

        getInvoices();
    }

    Connection connection = null;
    Statement statement = null;
    ResultSet set = null;


    private void getInvoices(){
        try {
            connectToDatabase();
            //Create a string query for the data the we need
            String query = "SELECT StudentNo, Fee\n" +
                    "From Attend;";

            set = statement.executeQuery(query);

            while(set.next()){
                InvoiceItem item;
                if(student.getUserId().equals(set.getString("StudentNo"))){
                    item = new InvoiceItem(student.getUserId(), set.getDouble("Fee"));
                    invoices.add(item);
                }
            }

            Double sum = 0.0;
            for(InvoiceItem item: invoices)
                sum += item.getAmount();
            lblTotal.setText("Total amount owed: R " + sum);
            closeDataBase();
        }
        catch(SQLException e){
            e.printStackTrace();
        }

    }


    //Attributes used to access the database

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
}
