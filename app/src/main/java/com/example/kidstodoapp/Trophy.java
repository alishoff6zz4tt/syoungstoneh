package com.example.kidstodoapp;

import java.io.Serializable;
import java.util.Date;

public class Trophy implements Serializable {

    private String name;
    private String description;
    private int pointValue;
    private boolean redeemed;

    Trophy(String name, String description, int pointValue) {
        this.name = name;
        this.description = description;
        this.pointValue = pointValue;
        redeemed = false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPoints() {
        return pointValue;
    }

    public void setPoints(int pointValue) {
        this.pointValue = pointValue;
    }

    public boolean isCompleted() {
        return redeemed;
    }

    public void setCompleted(boolean redeemed) {
        this.redeemed = redeemed;
    }

}
