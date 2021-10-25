package com.example.kidstodoapp;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class ToDoEntry implements Serializable {

    public static List<String> categories = Arrays.asList(
            "School", "Home", "Family", "Athletics", "Creative");

    private String entryName;
    private String description;
    private int pointValue;
    private boolean completed;
    private Calendar dateTimeDue;
    private String category;

    ToDoEntry(String entryName,
              String description,
              int pointValue,
              Calendar dateTimeDue,
              String category) {
        this.entryName = entryName;
        this.description = description;
        this.pointValue = pointValue;
        completed = false;
        this.dateTimeDue = dateTimeDue;
        this.category = category;
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

    public String getCategory() {
        return category;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public static List<String> getCategories() { return categories; }

}
