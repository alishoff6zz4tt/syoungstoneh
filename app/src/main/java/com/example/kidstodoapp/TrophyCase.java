package com.example.kidstodoapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Telephony;
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

    private ImageButton trophy;
    private Button createNewTrophy;

    private final int VIEW = 1;
    private final int NEW = 2;
    private final int EDIT = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trophy_case);

        // set up the RecyclerView
        recyclerView = findViewById(R.id.recycler_view);
        int numberOfColumns = 3;
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //adapter = new MyRecyclerViewAdapter(this, data);
        //adapter = new TrophyAdapter(existingTrophy, this);
        //adapter.setClickListener(this);

        createNewTrophy = findViewById(R.id.AddTrophy);
        createNewTrophy.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), AddTrophy.class);
                startActivityForResult(intent, NEW);
            }
        });
        //createNewTrophy.setVisibility(View.GONE); // unless in parent mode
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
        if (requestCode == VIEW) {
            if (resultCode == RESULT_OK) {
                Bundle extras = result.getExtras();
                int position = extras.getInt("position");
                Trophy trophy = existingTrophy.remove(position);
                adapter.notifyItemRemoved(position);
                trophy.setCompleted(true);
            }
        }
        }

    @Override
    public void onEntryClick(int position) {
        Intent intent = new Intent(this, ToDoEntryActivity.class);
        intent.putExtra("Trophy", existingTrophy.get(position));
        intent.putExtra("position", position);
        startActivityForResult(intent, VIEW);
    }
}

