package com.example.health_system;

import java.util.ArrayList;
import java.util.List;

public class score_struct {
     String name;
    List<Integer> score=new ArrayList<>();
    public void SETSCORE(List<Integer> t)
    {
        this.score=t;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Integer> getScore() {
        return score;
    }

    public void setScore(int index,int data) {
        this.score.set(index,data);
    }
}
