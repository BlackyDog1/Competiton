package utility;

public class Ranking {

    private String score;

    private String name;

    public void setScore(String score) {
        this.score = score;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getScore() {
        return this.score;
    }

    public String getName() {
        return this.name;
    }

    @Override
    public String toString() {
        return "Ranking{" +
                "score='" + score + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
