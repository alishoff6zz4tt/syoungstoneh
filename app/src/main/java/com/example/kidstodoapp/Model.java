package com.example.kidstodoapp;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class Model {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    public Model() {
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
    }

    public boolean isLoggedIn() {
        return mAuth.getCurrentUser() == null;
    }

}
