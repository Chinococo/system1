package com.example.health_system;

public class score_struct {
    private String name;
    private int[] score=new int[5];

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int[] getScore() {
        return score;
    }

    public void setScore(int index,int data) {
        this.score[index]=data;
    }


}
