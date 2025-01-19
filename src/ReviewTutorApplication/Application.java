package ReviewTutorApplication;

import net.ucanaccess.complex.Attachment;

import java.sql.*;

public class Application {
    public int appId;
    public String stNum;
    public String module;
  public int mark;
    Connection connection = null;
    Statement statement = null;
    ResultSet set = null;


    public Application(int appId, String stNum, String module, int mark) {

        this.module = module;
        this.stNum = stNum;
        this.mark = mark;
        this.appId= appId;
    }

    public String getModule() {
        return module;
    }

    public String getStNum() {
        return stNum;
    }



    public int getMark() {
        return mark;
    }
    public void approveTutor() throws SQLException {
         connectToDatabase();
         Boolean found = false;
         String check = "Select * From Tutor";
         set= statement.executeQuery(check);
         while(set.next()&&!found)
         {
             if(set.getString("TutorID").equals(this.stNum))
             {
                 found = true;
             }
         }
         if(!found)
         {
             String query = "Insert Into Tutor(TutorID) Values('" + this.stNum +"')";
             statement.executeUpdate(query);
             query = "Insert Into TutorModule Values('" + this.stNum +"', '" + this.module +"','" + this.mark +"')";
             statement.executeUpdate(query);
         }
         closeDataBase();
         connectToDatabase();
         String deleteQuery = "Delete From Application Where ID ='" + this.appId+"'";
         statement.executeUpdate(deleteQuery);
        System.out.println("deleting.......");
         closeDataBase();
         return;

     }
     public void rejectTutor() throws SQLException {
         connectToDatabase();
         String deleteQuery = "Delete From Application Where ID ='" + this.appId+"'";
         statement.executeUpdate(deleteQuery);
         System.out.println("deleting........");
         closeDataBase();
         return;
     }
    private void connectToDatabase() throws SQLException {
        //Connect to the database
        try {
            //We only need the user tables to find if we do have that user in the database
            //Establish a connection to the database
            connection = DriverManager.getConnection("jdbc:ucanaccess://resources//database//TutorBuddy.accdb");
            //Create the statement
            statement = connection.createStatement();
            System.out.println("Successfully connected to the database");
        } catch (SQLException e) {
            System.out.println("failed to connect to database");
        }
    }

    private void closeDataBase() {
        //Method is used to close the database connection
        try {
            if (connection != null)
                connection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }


}
