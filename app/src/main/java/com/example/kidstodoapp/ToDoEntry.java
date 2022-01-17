package com.example.kidstodoapp;

import java.io.Serializable;
import java.util.Date;

public class ToDoEntry implements Serializable {

    private String entryName;
    private String description;
    private int pointValue;
    private boolean completed;
    private Date dateTimeDue;

    ToDoEntry(String entryName, String description, int pointValue) {
        this.entryName = entryName;
        this.description = description;
        this.pointValue = pointValue;
        completed = false;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEntryName() {
        return entryName;
    }

    public void setEntryName(String entryName) {
        this.entryName = entryName;
    }

    public int getPointValue() {
        return pointValue;
    }

    public void setPointValue(int pointValue) {
        this.pointValue = pointValue;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

}
