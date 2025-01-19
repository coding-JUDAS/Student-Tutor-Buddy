package SessionReportPage;

import java.sql.*;

public class SessionReport {

    private String sessionId;
    private String moduleCode;
    private String moduleName;
    private String sessionType;

    private String tutorID;
    private String tutorName;

    private Date date;
    private Time time;


    public SessionReport(String sessionId, String tutorID, String moduleCode, Date date, Time time, String sessionType) throws SQLException {
        this.sessionId = sessionId;
        this.moduleCode = moduleCode;
        this.tutorID = tutorID;
        this.date = date;
        this.time = time;
        this.sessionType = sessionType; //single or group
        tutorName(tutorID);
        moduleName(moduleCode);
    }

    public String getSessionId(){
        return sessionId;
    }


    public String getTutorName(){
        return tutorName;
    }

    public String getModuleCode() {
        return moduleCode;
    }

    public String getModuleName() {
        return moduleName;
    }

    public String getTutorID() {
        return tutorID;
    }

    public Date getDate() {
        return date;
    }

    public Time getTime() {
        return time;
    }

    private void tutorName(String tutorID) throws SQLException {
        connectToDatabase();
        String query = "Select Tutor.TutorId, User.FirstName, User.LastName\n" +
                "From User Inner Join Tutor On User.UserId = Tutor.TutorId;";
        set = statement.executeQuery(query);
        System.out.println("Query has worked");
        Boolean found = false;
        String name = "Unknown Tutor";
        while(!found && set.next()){
            if(tutorID.equals(set.getString("TutorId"))){
                name = set.getString("FirstName") + " " + set.getString("LastName");
                found = true;
            }
        }

        tutorName = name;
        closeDataBase();
    }

    private void moduleName(String moduleCode){
        try{
            connectToDatabase();
            String query = "Select ModuleCode, ModuleName\n" +
                    "From Module;";
            set = statement.executeQuery(query);
            Boolean found = false;
            String modName = "Unknown Module";
            while(!found && set.next()){
                if(moduleCode.equals(set.getString("ModuleCode"))){
                    modName = set.getString("ModuleName");
                    found = true;
                }
            }

            this.moduleName = modName;
            closeDataBase();
        }
        catch(SQLException exception){
            this.moduleName = "No Module Name";
        }
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

    public String getSessionType(){
        return sessionType;
    }
}
