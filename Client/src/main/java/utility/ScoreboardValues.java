package utility;

public class ScoreboardValues {

    private String username;

    private String stage;

    private String score;

    public void setUsername(String username) {
        this.username = username;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public void setStage(String stage) {
        this.stage = stage;
    }

    public String getUsername() { return this.username; }

    public String getScore() { return this.score; }

    public String getStage() { return this.stage; }

    @Override
    public String toString() {
        return "ScoreboardValues{" +
                "username='" + username + '\'' +
                ", stage='" + stage + '\'' +
                ", score='" + score + '\'' +
                '}';
    }
}
