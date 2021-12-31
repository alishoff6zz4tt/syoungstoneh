package com.example.kidstodoapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

public class TrophyCase extends AppCompatActivity {

    private static ArrayList<ToDoEntry> TrophyCase = new ArrayList<>();
    private static ArrayList<ToDoEntry> archivedTrophy = new ArrayList<>();

    private ToDoAdapter adapter;
    private RecyclerView recyclerView;

 /*  Add total points count on
    private TextView pointsDisplay;
    private Button parentModeButton;
    private Button addEntryButton;*/

    private ImageButton trophy;
    private Button addTrophy;

    private final int VIEW = 1;
    private final int NEW = 2;
    private final int EDIT = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trophy_case);

        adapter = new TrophyAdapter(TrophyCase, this);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        addTrophy = findViewById(R.id.AddTrophy);
        addTrophy.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), CreateToDoEntryActivity.class);
                startActivityForResult(intent, NEW);
            }
        });

        trophy = findViewById(R.id.trophyButton);
        trophy.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), EditTrophy.class);
                startActivity(intent);
            }
        });

       /* addTrophy = findViewById(R.id.AddTrophy);
        addTrophy.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), AddTrophy.class);
                startActivity(intent);
            }
        });

*/



    }
}

