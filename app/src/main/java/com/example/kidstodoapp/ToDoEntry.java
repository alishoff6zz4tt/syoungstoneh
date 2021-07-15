package com.example.kidstodoapp;

public class ToDoEntry {

    String entryName;
    String description;

    ToDoEntry(String entryName, String description) {
        this.entryName = entryName;
        this.description = description;
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
}
