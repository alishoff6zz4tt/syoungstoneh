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

public class TrophyCase extends AppCompatActivity implements TrophyAdapter.OnEntryListener  {

    private static ArrayList<Trophy> existingTrophy = new ArrayList<>();
    private static ArrayList<Trophy> archivedTrophy = new ArrayList<>();

    private TrophyAdapter adapter;
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

        adapter = new TrophyAdapter(existingTrophy, this);
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
        //addTrophy.setVisibility(View.GONE); // unless in parent mode

        trophy = findViewById(R.id.trophyButton);
        trophy.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), EditTrophy.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent result) {
        super.onActivityResult(requestCode, resultCode, result);
        if (requestCode == NEW) {
            if (resultCode == RESULT_OK) {
                Bundle extras = result.getExtras();
                Trophy newTemp = (Trophy) extras.getSerializable("Trophy");
                existingTrophy.add(newTemp);
            }
        }
        /* if (requestCode == VIEW) {
            if (resultCode == RESULT_OK) {
                Bundle extras = result.getExtras();
                int position = extras.getInt("position");
                Trophy temp = existingTrophy.remove(position);
                adapter.notifyItemRemoved(position);
                temp.setCompleted(true);
                archivedTrophy.add(temp);
            } */
        }


    @Override
    public void onEntryClick(int position) {
        Intent intent = new Intent(this, ToDoEntryActivity.class);
        intent.putExtra("Trophy", existingTrophy.get(position));
        intent.putExtra("position", position);
        startActivityForResult(intent, VIEW);
    }
}

