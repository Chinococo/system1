package company.test.health_system;

import java.util.ArrayList;
import java.util.List;

public class score_struct {
    String name;
    List<Object> score = new ArrayList<>();

    public void SETSCORE(List<Object> t) {
        this.score = t;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Object> getScore() {
        return score;
    }

    public void setScore(int index,Object data) {
        this.score.set(index, data);
    }
}
