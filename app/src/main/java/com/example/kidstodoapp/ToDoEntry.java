package com.example.kidstodoapp;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ToDoEntry implements Serializable {

    private String entryName;
    private String description;
    private int pointValue;
    private boolean completed;
    private Calendar dateTimeDue;

    ToDoEntry(String entryName, String description, int pointValue, Calendar dateTimeDue) {
        this.entryName = entryName;
        this.description = description;
        this.pointValue = pointValue;
        completed = false;
        this.dateTimeDue = dateTimeDue;
    }

    public String getDescription() {
        return description;
    }

    public String getEntryName() {
        return entryName;
    }

    public int getPointValue() {
        return pointValue;
    }

    public boolean isCompleted() {
        return completed;
    }

    public Calendar getDateTimeDue() {
        return dateTimeDue;
    }

    public String getDateTimeString() {
        SimpleDateFormat formatter = new SimpleDateFormat("EEEE, MMMM d, h:mm a");
        return formatter.format(dateTimeDue.getTime());
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }


}
