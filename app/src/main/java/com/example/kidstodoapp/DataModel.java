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
import java.util.Map;
import java.util.Objects;
import java.util.Observable;

public class DataModel extends Observable {

    private static final String USERS = "users";
    private static final String TO_DO_ENTRIES = "toDoEntries";
    private static final String COMPLETED_ENTRIES = "completedEntries";
    private static final String PHONE_NUMBER = "phoneNumber";
    private static final String POINTS_EARNED = "pointsEarned";
    private static final String SESSION_IDENTIFIER_LAST_CHANGED = "sessionIdentifierLastChanged";
    private static final String EXISTING_TROPHIES = "existingTrophies";
    private static final String ARCHIVED_TROPHIES = "archivedTrophies";

    private static DataModel INSTANCE = null;

    private long sessionIdentifier;

    private ArrayList<Trophy> existingTrophies = new ArrayList<>();
    private ArrayList<Trophy> archivedTrophies = new ArrayList<>();

    private ArrayList<ToDoEntry> toDoEntries = new ArrayList<>();
    private ArrayList<ToDoEntry> completedEntries = new ArrayList<>();
    private long pointsEarned;
    private String phoneNumber;

    private DocumentReference documentReference;

    private DataModel() {
        sessionIdentifier = (long) (Math.random() * Long.MAX_VALUE);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String uid = (mAuth.getCurrentUser() != null ? mAuth.getCurrentUser().getUid() : "");

        documentReference = db.collection(USERS).document(uid);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException error) {
                if (snapshot != null) {
                    if (!snapshot.contains(SESSION_IDENTIFIER_LAST_CHANGED) ||
                            !Objects.equals(snapshot.get(SESSION_IDENTIFIER_LAST_CHANGED),
                                    sessionIdentifier)) {
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

    public ArrayList<Trophy> getExistingTrophies() { return existingTrophies; }
    public ArrayList<Trophy> getArchivedTrophies() { return archivedTrophies; }

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
        completedEntries.add(entry);
        setChanged();
        notifyObservers();
        updateFirebase();
    }

    public void uncompleteToDoEntry(int position) {
        ToDoEntry entry = completedEntries.remove(position);
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

    public void addTrophy(Trophy trophy) {
        existingTrophies.add(trophy);
        setChanged();
        notifyObservers();
        updateFirebase();
    }

    public void deleteTrophy(int position) {
        existingTrophies.remove(position);
        setChanged();
        notifyObservers();
        updateFirebase();
    }

    public void redeemTrophy(int position) {
        Trophy trophy = existingTrophies.remove(position);
        archivedTrophies.add(trophy);
        pointsEarned -= trophy.getPointValue();
        setChanged();
        notifyObservers();
        updateFirebase();
    }

    private void updateFirebase() {
        documentReference.update(TO_DO_ENTRIES, toDoEntries);
        documentReference.update(COMPLETED_ENTRIES, completedEntries);
        documentReference.update(PHONE_NUMBER, phoneNumber);
        documentReference.update(POINTS_EARNED, pointsEarned);
        documentReference.update(SESSION_IDENTIFIER_LAST_CHANGED, sessionIdentifier);
        documentReference.update(EXISTING_TROPHIES, existingTrophies);
        documentReference.update(ARCHIVED_TROPHIES, archivedTrophies);
    }

    private void updateModel(DocumentSnapshot snapshot) {
        toDoEntries = buildToDoEntries(
                (ArrayList<HashMap<String, Object>>) snapshot.get(TO_DO_ENTRIES));
        completedEntries = buildToDoEntries(
                (ArrayList<HashMap<String, Object>>) snapshot.get(COMPLETED_ENTRIES));
        pointsEarned =  (Long) snapshot.get(POINTS_EARNED);
        phoneNumber = snapshot.getString(PHONE_NUMBER);

        //don't need to update existing firebase entries, so no null pointer exception
        if (snapshot.contains(EXISTING_TROPHIES) && snapshot.contains(ARCHIVED_TROPHIES)) {
            existingTrophies = buildTrophies(
                    (ArrayList<HashMap<String, Object>>) snapshot.get(EXISTING_TROPHIES));
            archivedTrophies = buildTrophies(
                    (ArrayList<HashMap<String, Object>>) snapshot.get(ARCHIVED_TROPHIES));
        }
    }

    private static ArrayList<ToDoEntry> buildToDoEntries(ArrayList<HashMap<String, Object>> list) {
        ArrayList<ToDoEntry> arrayList = new ArrayList<>();
        for (HashMap<String, Object> map : list) {
            arrayList.add(ToDoEntry.buildToDoEntry(map));
        }
        return arrayList;
    }

    private static ArrayList<Trophy> buildTrophies(ArrayList<HashMap<String, Object>> list) {
        ArrayList<Trophy> arrayList = new ArrayList<>();
        for (HashMap<String, Object> map : list) {
            arrayList.add(Trophy.buildTrophy(map));
        }
        return arrayList;
    }

    public static void createDbEntry(String phone) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String uid = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
        DocumentReference documentReference = db.collection(USERS).document(uid);
        Map<String,Object> user = new HashMap<>();
        user.put(TO_DO_ENTRIES, new ArrayList<ToDoEntry>());
        user.put(COMPLETED_ENTRIES, new ArrayList<ToDoEntry>());
        user.put(PHONE_NUMBER, phone);
        user.put(POINTS_EARNED, 0);
        user.put(SESSION_IDENTIFIER_LAST_CHANGED, 0);
        user.put(EXISTING_TROPHIES, new ArrayList<Trophy>());
        user.put(ARCHIVED_TROPHIES, new ArrayList<Trophy>());
        documentReference.set(user);
    }

    public static void executeLogout() {
        FirebaseAuth.getInstance().signOut();
        INSTANCE = null;
    }

}
