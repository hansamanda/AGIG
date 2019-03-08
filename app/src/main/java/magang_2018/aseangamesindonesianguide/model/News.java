package magang_2018.aseangamesindonesianguide.model;

public class News {

    private String id, title, author, body, source, time, image;

    public News(String title, String time, String image, String body, String source) {
        this.title = title;
        this.time = time;
        this.image = image;
        this.body = body;
        this.source = source;
    }

    public News(String id, String title, String author, String body, String source, String time, String image) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.body = body;
        this.source = source;
        this.time = time;
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
