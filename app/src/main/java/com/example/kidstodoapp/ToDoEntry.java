package com.example.kidstodoapp;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import com.google.firebase.Timestamp;

public class ToDoEntry implements Serializable, Comparable<ToDoEntry> {

    public static List<String> categories = Arrays.asList(
            "School", "Home", "Family", "Athletics", "Creative");

    private String entryName;
    private String description;
    private int pointValue;
    private boolean completed;
    private Timestamp dateTimeDue;
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
        this.dateTimeDue = new Timestamp(dateTimeDue.getTime());
        this.category = category;
    }

    @Override
    public int compareTo(ToDoEntry toDoEntry) {
        return this.getDateTimeDue().compareTo(toDoEntry.getDateTimeDue());
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
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(dateTimeDue.getSeconds() * 1000);
        return cal;
    }

    public String getDateTimeString() {
        SimpleDateFormat formatter = new SimpleDateFormat("EEEE, MMMM d, h:mm a");
        return formatter.format(dateTimeDue);
    }

    public String getCategory() {
        return category;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public static List<String> getCategories() { return categories; }

    public static ToDoEntry buildToDoEntry(HashMap<String,Object> map) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(((Timestamp)map.get("dateTimeDue")).getSeconds() * 1000);
        return new ToDoEntry(
                (String)map.get("entryName"),
                (String)map.get("description"),
                (Integer)map.get("pointValue"),
                cal,
                (String)map.get("category")
                );
    }

}
