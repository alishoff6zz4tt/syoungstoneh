package com.example.kidstodoapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

// This comment was made by Sean Youngstone
public class MainActivity extends AppCompatActivity implements ToDoAdapter.OnEntryListener {

    private ToDoAdapter adapter = new ToDoAdapter(toDoEntries, this);

    private static TextView pointsDisplay;

    private static ArrayList<ToDoEntry> toDoEntries = new ArrayList<>();
    private static ArrayList<ToDoEntry> completedEntries = new ArrayList<>();
    private static int pointsEarned = 0;

    private final int NEW_ENTRY_REQUEST = 1;
    private final int VIEW_ENTRY_REQUEST = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView mRecyclerView = findViewById(R.id.recycler_view);

        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        pointsDisplay = findViewById(R.id.points_display);
        setPointsDisplay();

        Button addEntryButton = findViewById(R.id.add_entry_button);
        addEntryButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), CreateToDoEntryActivity.class);
                startActivityForResult(intent, NEW_ENTRY_REQUEST);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent result) {
        super.onActivityResult(requestCode, resultCode, result);
        if (requestCode == NEW_ENTRY_REQUEST) {
            if (resultCode == RESULT_OK) {
                Bundle extras = result.getExtras();
                ToDoEntry newToDoEntry = (ToDoEntry) extras.getSerializable("ToDoEntry");
                toDoEntries.add(newToDoEntry);
            }
        }
        if (requestCode == VIEW_ENTRY_REQUEST) {
            if (resultCode == RESULT_OK) {
                Bundle extras = result.getExtras();
                int position = extras.getInt("position");
                ToDoEntry entry = toDoEntries.remove(position);
                adapter.notifyItemRemoved(position);
                entry.setCompleted(true);
                completedEntries.add(entry);
                pointsEarned += entry.getPointValue();
                setPointsDisplay();
            }
        }
    }

    @Override
    public void onEntryClick(int position) {
        Intent intent = new Intent(this, ToDoEntryActivity.class);
        intent.putExtra("ToDoEntry", toDoEntries.get(position));
        intent.putExtra("position", position);
        startActivityForResult(intent, VIEW_ENTRY_REQUEST);
    }

    public void setPointsDisplay() {
        pointsDisplay.setText(String.format(Locale.US, "$%d", pointsEarned));
    }
}
