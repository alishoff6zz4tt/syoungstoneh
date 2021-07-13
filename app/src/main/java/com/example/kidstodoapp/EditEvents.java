/*package com.example.kidstodoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.kidstodoapp.ui.main.EditEventsFragment;

public class EditEvents extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_events_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, EditEventsFragment.newInstance())
                    .commitNow();
        }
    }
}*/