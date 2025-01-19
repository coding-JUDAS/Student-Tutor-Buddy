package StudentViewInvoice;

import java.sql.Date;

public class InvoiceItem {
    String studentNumber;

    Date date;
    Double amount;

    String sessionType;

    public InvoiceItem(String studentNumber, Double amount) {
        this.studentNumber = studentNumber;
        this.amount = amount;
    }

    public String getStudentNumber() {
        return studentNumber;
    }


    public Date getDate() {
        return date;
    }

    public Double getAmount() {
        return amount;
    }

    public String getSessionType() {
        return sessionType;
    }
}
