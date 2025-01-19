package SessionReportPage;

import java.sql.*;

public class Rating {
    private String tutorName;
    private String comment;
    private int rating;
    private String sessionType; //individual or group
    private String sessionID;
    private String studentNumber = "Unknown";
    private Date date;
    private Time time;


    public Rating(String sessionID, String sessionType, String tutorName) {
        this.tutorName = tutorName;
        this.sessionID = sessionID;
        this.sessionType = sessionType;
    }

    public String getComment() {
        return comment;
    }

    public int getRating() {
        return rating;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setTime(Time time) {
        this.time = time;
    }
}
