package com.example.kidstodoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import java.util.ArrayList;

public class TrophyCase extends AppCompatActivity {

    private static ArrayList<ToDoEntry> TrophyCase = new ArrayList<>();
    private static ArrayList<ToDoEntry> archivedTrophy = new ArrayList<>();

    private ImageButton trophy;
    private Button addTrophy;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trophy_case);

        trophy = findViewById(R.id.trophyButton);
        trophy.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), EditTrophy.class);
                startActivity(intent);
            }
        });

        addTrophy = findViewById(R.id.AddTrophy);
        addTrophy.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), AddTrophy.class);
                startActivity(intent);
            }
        });





    }
}

