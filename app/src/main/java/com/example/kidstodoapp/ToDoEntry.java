package com.example.kidstodoapp;

import java.io.Serializable;

public class ToDoEntry implements Serializable {

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
