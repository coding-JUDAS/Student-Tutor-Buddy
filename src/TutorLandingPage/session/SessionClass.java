package TutorLandingPage.session;

import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.sql.Date;
import java.sql.Time;

public class SessionClass {

    private SimpleFloatProperty fee;
    private SimpleStringProperty modCode, sessionType, tutorId, studentId;
    private SimpleIntegerProperty numStds, sessionId;
    private Date date;
    private Time time;

    //Populates from DB
    public SessionClass(int sessionID ,String modCode,
                        Date date, Time time, String sessionType) {

        this.sessionId = new SimpleIntegerProperty(sessionID);
        this.modCode = new SimpleStringProperty(modCode);
        this.date = date;
        this.time = time;
        this.sessionType = new SimpleStringProperty(sessionType);
    }
    public SessionClass(String studentId){

    }

    //Group Session
    public SessionClass(String modCode, String sessionType, Integer numStds, Date date, Time time, String SessionType) {
        this.modCode = new SimpleStringProperty(modCode);
        this.sessionType = new SimpleStringProperty(sessionType);
        this.numStds = new SimpleIntegerProperty(numStds);
        this.date = date;
        this.time = time;
        this.sessionType = new SimpleStringProperty(sessionType);
    }

    public SessionClass() {

    }

    //Individual Session
    public SessionClass(String tutorId, String moduleCode, Date date, Time time, float fee, String sessionType) {
        this.fee = new SimpleFloatProperty(fee);
        this.tutorId = new SimpleStringProperty(tutorId);
        this.time = time;
        this.date = date;
        this.modCode = new SimpleStringProperty(moduleCode);
        this.sessionType = new SimpleStringProperty(sessionType);
    }

    public void setFee(float fee) {
        this.fee.set(fee);
    }

    public float getFee() {
        return fee.get();
    }

    public SimpleFloatProperty feeProperty() {
        return fee;
    }

    public String getModCode() {
        return modCode.get();
    }

    public SimpleStringProperty modCodeProperty() {
        return modCode;
    }

    public void setModCode(String modCde) {
        this.modCode.setValue(modCde);
    }

    public String getSessionType() {
        return sessionType.get();
    }

    public SimpleStringProperty sessionTypeProperty() {
        return sessionType;
    }

    public void setSessionType(String sessionType) {
        this.sessionType.set(sessionType);
    }

    public int getNumStds() {
        return numStds.get();
    }

    public SimpleIntegerProperty numStdsProperty() {
        return numStds;
    }

    public void setNumStds(int numStds) {
        this.numStds.set(numStds);
    }

    public int getSessionId() {
        return sessionId.get();
    }

    public SimpleIntegerProperty sessionIdProperty() {
        return sessionId;
    }

    public void setSessionId(int sessionId) {
        this.sessionId.set(sessionId);
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }

}

