package com.example.health_system;

public class score_struct {
    String name;
    int score[]=new int[5];

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int[] getScore() {
        return score;
    }

    public void setScore(int[] score) {
        this.score = score;
    }

}
