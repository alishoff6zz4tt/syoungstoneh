package com.example.kidstodoapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;

public class LoadingActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        ParentModeUtility.setApplicationContext(this.getApplicationContext());

        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser() != null) {
            if (ParentModeUtility.retrievePasswordHash()) {
                ParentModeUtility.checkDeviceType();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            } else {
                mAuth.signOut();
                startActivity(new Intent(getApplicationContext(),SignInActivity.class));
            }
        } else {
            startActivity(new Intent(getApplicationContext(),SignInActivity.class));
        }
        finish();


    }
}