package ManageModules;

import java.sql.*;
import java.util.ArrayList;

public class Module {
    private String moduleCode;
    private String moduleName;

    public Module(String moduleCode, String moduleName) {
        this.moduleCode = moduleCode;
        this.moduleName = moduleName;
    }

    public Boolean equals(String moduleCode){
        return this.moduleCode.equals(moduleCode);
    }

    public boolean addToDatabase() throws SQLException {
        connectToDatabase();
        ArrayList<Module> modules = new ArrayList<>();
        //Sql query to get all the modules in the database
        String query = "Select * From Module";
        set = statement.executeQuery(query);

        boolean duplicate = false;
        while(set.next() && !duplicate){
            Module cur = new Module(set.getString("ModuleCode"), set.getString("ModuleName"));
            if(cur.moduleCode.equals(this.moduleCode))
                duplicate = true;
        }
        closeDataBase();
        connectToDatabase();
        if(!duplicate){ //No same module found in the database
            String addQuery = "Insert Into Module(ModuleCode, ModuleName) " +
                    "Values('" + this.moduleCode + "','" + this.moduleName + "')";
            statement.executeUpdate(addQuery);
            System.out.println("New module added to the database: " + moduleCode + ", " + moduleName);
        }
        closeDataBase();

        if(duplicate)
            return false;
        return true;
    }
   public Boolean deleteFromDatabase() throws SQLException {
       connectToDatabase();
       String query = "Select * From Module";
       set = statement.executeQuery(query);

       boolean found = false;
       while(set.next()&& !found)
       {
           Module cur = new Module(set.getString("ModuleCode"), set.getString("ModuleName"));
           if(cur.moduleCode.equals(this.moduleCode))
               found = true;
       }
       closeDataBase();
       connectToDatabase();
       if(found)
       {
           String deletequery ="Delete From Module Where Module.ModuleCode='"+this.moduleCode+"'";
           statement.executeUpdate(deletequery);
           System.out.println("module deleted");

       }
       closeDataBase();
       if(!found)
           return false;
           return true;
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
}
