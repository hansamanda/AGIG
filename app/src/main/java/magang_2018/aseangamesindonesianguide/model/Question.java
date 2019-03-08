package magang_2018.aseangamesindonesianguide.model;

public class Question {
    private String user_key, question_key, category_key, question, time;

    public Question() {
    }

    public Question(String user_key, String question_key, String category_key, String question, String time) {
        this.user_key = user_key;
        this.question_key = question_key;
        this.category_key = category_key;
        this.question = question;
        this.time = time;
    }

    public String getUser_key() {
        return user_key;
    }

    public void setUser_key(String user_key) {
        this.user_key = user_key;
    }

    public String getQuestion_key() {
        return question_key;
    }

    public void setQuestion_key(String question_key) {
        this.question_key = question_key;
    }

    public String getCategory_key() {
        return category_key;
    }

    public void setCategory_key(String category_key) {
        this.category_key = category_key;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
