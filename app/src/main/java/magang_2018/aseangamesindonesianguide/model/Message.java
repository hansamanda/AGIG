package magang_2018.aseangamesindonesianguide.model;

public class Message {
    private String message, from, date, type;

    public Message() {
    }

    public Message(String message, String from, String date, String type) {
        this.message = message;
        this.from = from;
        this.date = date;
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
