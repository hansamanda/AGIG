package magang_2018.aseangamesindonesianguide.model;

public class Answer {
    private String user_key, answer_key, answer, time;

    public Answer() {
    }

    public Answer(String user_key, String answer_key, String answer, String time) {
        this.user_key = user_key;
        this.answer_key = answer_key;
        this.answer = answer;
        this.time = time;
    }

    public String getAnswer_key() {
        return answer_key;
    }

    public void setAnswer_key(String answer_key) {
        this.answer_key = answer_key;
    }

    public String getUser_key() {
        return user_key;
    }

    public void setUser_key(String user_key) {
        this.user_key = user_key;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
