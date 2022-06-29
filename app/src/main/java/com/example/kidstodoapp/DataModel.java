package com.example.kidstodoapp;

import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Observable;

public class DataModel extends Observable {

    private static DataModel INSTANCE = null;

    private long sessionIdentifier;

    private ArrayList<ToDoEntry> toDoEntries = new ArrayList<>();
    private ArrayList<ToDoEntry> completedEntries = new ArrayList<>();
    private long pointsEarned = 0;
    private String phoneNumber = "";

    private DocumentReference documentReference;

    private DataModel() {
        sessionIdentifier = (long) (Math.random() * Long.MAX_VALUE);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String uid = (mAuth.getCurrentUser() != null ? mAuth.getCurrentUser().getUid() : "");

        documentReference = db.collection("users").document(uid);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException error) {
                if (snapshot != null) {
                    if (!snapshot.contains("sessionIdentifierLastChanged") ||
                            !(snapshot.get("sessionIdentifierLastChanged")).equals(sessionIdentifier)) {
                        updateModel(snapshot);
                        setChanged();
                        notifyObservers();
                    }
                }
            }
        });
    }

    public static DataModel getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new DataModel();
        }
        return INSTANCE;
    }

    public ArrayList<ToDoEntry> getToDoEntries() { return toDoEntries; }
    public ArrayList<ToDoEntry> getCompletedEntries() { return completedEntries; }
    public long getPointsEarned() { return pointsEarned; }
    public String getPhoneNumber() { return phoneNumber; }

    public void addToDoEntry(ToDoEntry entry) {
        toDoEntries.add(entry);
        Collections.sort(toDoEntries);
        setChanged();
        notifyObservers();
        updateFirebase();
    }

    public void deleteToDoEntry(int position) {
        toDoEntries.remove(position);
        setChanged();
        notifyObservers();
        updateFirebase();
    }

    public void editToDoEntry(int position, ToDoEntry entry) {
        toDoEntries.set(position, entry);
        setChanged();
        notifyObservers();
        updateFirebase();
    }

    public void completeToDoEntry(int position) {
        ToDoEntry entry = toDoEntries.remove(position);
        entry.setCompleted(true);
        completedEntries.add(entry);
        setChanged();
        notifyObservers();
        updateFirebase();
    }

    public void uncompleteToDoEntry(int position) {
        ToDoEntry entry = completedEntries.remove(position);
        entry.setCompleted(false);
        addToDoEntry(entry);
    }

    public void confirmCompleted(int position) {
        ToDoEntry entry = completedEntries.remove(position);
        pointsEarned += entry.getPointValue();
        setChanged();
        notifyObservers();
        updateFirebase();
    }

    public void updatePhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        updateFirebase();
    }

    private void updateFirebase() {
        documentReference.update("toDoEntries", toDoEntries);
        documentReference.update("completedEntries", completedEntries);
        documentReference.update("phoneNumber", phoneNumber);
        documentReference.update("pointsEarned", pointsEarned);
        documentReference.update("sessionIdentifierLastChanged", sessionIdentifier);
    }

    private void updateModel(DocumentSnapshot snapshot) {
        toDoEntries = buildToDoEntries(
                (ArrayList<HashMap<String, Object>>) snapshot.get("toDoEntries"));
        completedEntries = buildToDoEntries(
                (ArrayList<HashMap<String, Object>>) snapshot.get("completedEntries"));
        pointsEarned =  (Long) snapshot.get("pointsEarned");
        phoneNumber = snapshot.getString("phoneNumber");
    }

    private static ArrayList<ToDoEntry> buildToDoEntries(ArrayList<HashMap<String, Object>> list) {
        ArrayList<ToDoEntry> arrayList = new ArrayList<>();
        for (HashMap<String, Object> map : list) {
            arrayList.add(ToDoEntry.buildToDoEntry(map));
        }
        return arrayList;
    }

}
