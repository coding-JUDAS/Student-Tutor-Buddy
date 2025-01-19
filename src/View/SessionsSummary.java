package View;

import java.sql.Time;
import java.util.Date;

public class SessionsSummary {
    public String moduleCode;
    public Date date;
    public Time time;
    public String tutor;
    public String stNum;

    public String getModuleCode() {
        return moduleCode;
    }

    public Date getDate() {
        return date;
    }

    public String getStNum() {
        return stNum;
    }

    public Time getTime() {
        return time;
    }

    public String getTutor() {
        return tutor;
    }

    public SessionsSummary(String modCod, Date date, Time time, String tutor, String stNum )
    {
        this.moduleCode = modCod;
        this.date = date;
        this.time = time;
        this.tutor = tutor;
        this.stNum = stNum;
    }

}
