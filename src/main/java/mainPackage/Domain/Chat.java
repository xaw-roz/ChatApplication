package mainPackage.Domain;

/**
 * Created by Suryaraj on 5/18/2016.
 */
public class Chat {
    int Userid;
    int Receiptid;
    String Message;
    String Date;

    public Chat() {
    }

    public Chat(int userid, int receiptid, String message, String date) {
        Userid = userid;
        Receiptid = receiptid;
        Message = message;
        Date = date;
    }

    public int getUserid() {
        return Userid;
    }

    public void setUserid(int userid) {
        Userid = userid;
    }

    public int getReceiptid() {
        return Receiptid;
    }

    public void setReceiptid(int receiptid) {
        Receiptid = receiptid;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }
}
