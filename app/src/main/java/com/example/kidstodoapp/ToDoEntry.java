package com.example.kidstodoapp;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class ToDoEntry implements Serializable, Comparable<ToDoEntry> {

    public static List<String> categories = Arrays.asList(
            "School", "Home", "Family", "Athletics", "Creative");

    private String entryName;
    private String description;
    private long pointValue;
    private long dateTimeMillis;
    private String dateTimeString;
    private String category;

    ToDoEntry(String entryName,
              String description,
              long pointValue,
              long dateTimeMillis,
              String dateTimeString,
              String category) {
        this.entryName = entryName;
        this.description = description;
        this.pointValue = pointValue;
        this.dateTimeMillis = dateTimeMillis;
        this.dateTimeString = dateTimeString;
        this.category = category;
    }

    @Override
    public int compareTo(ToDoEntry toDoEntry) {
        return Long.compare(this.dateTimeMillis, toDoEntry.getDateTimeMillis());
    }

    public String getDescription() {
        return description;
    }

    public String getEntryName() {
        return entryName;
    }

    public long getPointValue() {
        return pointValue;
    }

    public long getDateTimeMillis() {
        return dateTimeMillis;
    }

    public String getDateTimeString() {
        return dateTimeString;
    }

    public String getCategory() {
        return category;
    }

    public static List<String> getCategories() { return categories; }

    public static ToDoEntry buildToDoEntry(HashMap<String,Object> map) {
        return new ToDoEntry(
                (String)map.get("entryName"),
                (String)map.get("description"),
                (Long)map.get("pointValue"),
                (Long)map.get("dateTimeMillis"),
                (String)map.get("dateTimeString"),
                (String)map.get("category")
                );
    }

}
