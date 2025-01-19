package StudentLandingPage.Reflection;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.time.LocalDate;

public class Attend {

    private SimpleStringProperty StudentNo, boolStringValue;
    private SimpleStringProperty Comment;
    private SimpleIntegerProperty sessionID, rating;
    private SimpleBooleanProperty attended;

    public Attend(int sessionID, String studentNo, boolean attended, String comment,
                  int rating) {
        StudentNo = new SimpleStringProperty(studentNo);
        Comment = new SimpleStringProperty(comment);
        this.rating = new SimpleIntegerProperty(rating);
        this.sessionID = new SimpleIntegerProperty(sessionID);
        this.attended = new SimpleBooleanProperty(attended);
        setBoolStringValue(attended);
    }

    public Attend(int sessionID, String  studentNo, String comment,
                  int rating) {
        StudentNo = new SimpleStringProperty(studentNo);
        Comment = new SimpleStringProperty(comment);
        this.rating = new SimpleIntegerProperty(rating);
        this.sessionID = new SimpleIntegerProperty(sessionID);
    }
    public void setBoolStringValue(boolean attendance){
        if (attendance){
            boolStringValue = new SimpleStringProperty("YES");
        }
        else{
            boolStringValue = new SimpleStringProperty("NO");
        }
    }

    public String getBoolStringValue() {
        return boolStringValue.get();
    }

    public SimpleStringProperty boolStringValueProperty() {
        return boolStringValue;
    }

    public void setBoolStringValue(String boolStringValue) {
        this.boolStringValue.set(boolStringValue);
    }

    public int getRating() {
        return rating.get();
    }

    public SimpleIntegerProperty ratingProperty() {
        return rating;
    }


    public void setRating(int rating) {
        this.rating.set(rating);
    }

    public boolean isAttended() {
        return attended.get();
    }

    public SimpleBooleanProperty attendedProperty() {
        return attended;
    }

    public void setAttended(boolean attended) {
        this.attended.set(attended);
    }

    public String getStudentNo() {
        return StudentNo.get();
    }

    public SimpleStringProperty studentNoProperty() {
        return StudentNo;
    }

    public void setStudentNo(String studentNo) {
        this.StudentNo.set(studentNo);
    }

    public String getComment() {
        return Comment.get();
    }

    public SimpleStringProperty commentProperty() {
        return Comment;
    }

    public void setComment(String comment) {
        this.Comment.set(comment);
    }


    public int getSessionID() {
        return sessionID.get();
    }

    public SimpleIntegerProperty sessionIDProperty() {
        return sessionID;
    }

    public void setSessionID(int sessionID) {
        this.sessionID.set(sessionID);
    }
}
