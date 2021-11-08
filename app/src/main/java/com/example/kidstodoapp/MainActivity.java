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

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

// This comment was made by Sean Youngstone
public class MainActivity extends AppCompatActivity implements ToDoAdapter.OnEntryListener {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private static ArrayList<ToDoEntry> toDoEntries = new ArrayList<>();
    private static ArrayList<ToDoEntry> completedEntries = new ArrayList<>();
    private static int pointsEarned = 0;
    private static String password;
    private static String phoneNumber;
    private static Boolean passwordAlreadySet = false;
    private static Boolean inParentMode = false;

    private final int NEW_ENTRY_REQUEST = 1;
    private final int VIEW_ENTRY_REQUEST = 2;
    private final int EDIT_ENTRY_REQUEST = 3;

    private ToDoAdapter adapter;
    private RecyclerView recyclerView;
    private TextView pointsDisplay;
    private Button parentModeButton;
    private Button addEntryButton;
    private ImageButton setPhoneNumberButton;

    private Handler parentModeTimeOut;
    private Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        adapter = new ToDoAdapter(toDoEntries, this);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        pointsDisplay = findViewById(R.id.points_display);
        setPointsDisplay();

        addEntryButton = findViewById(R.id.add_entry_button);
        addEntryButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), CreateToDoEntryActivity.class);
                startActivityForResult(intent, NEW_ENTRY_REQUEST);
            }
        });

        parentModeButton = findViewById(R.id.pM);
        setPhoneNumberButton = findViewById(R.id.phone);

        addEntryButton.setVisibility(View.GONE);
        setPhoneNumberButton.setVisibility(View.GONE);

        if(Utility.getInParentMode()) {setPhoneNumberButton.setVisibility(View.GONE);}
        else {setPhoneNumberButton.setVisibility(View.VISIBLE);}

        parentModeButton.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  if(Utility.getInParentMode()) {
                      setInParentMode(false);
                      Toast.makeText(MainActivity.this,
                              "Logged Out",
                              Toast.LENGTH_SHORT).show();
                  }
                  else if(Utility.getNotFirstTime() == null) {
                      Intent intent = new Intent(view.getContext(), ParentModeFirstTime.class);
                      startActivity(intent);
                  }
                  else {
                      Intent intent = new Intent(view.getContext(), ParentMode.class);
                      intent.putExtra(Utility.getPassword(), Utility.getPassword());
                      startActivity(intent);
                  }
              }
        });

        setPhoneNumberButton.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  Intent intent = new Intent(view.getContext(), PhoneNumber.class);
                  startActivity(intent);
              }
        });

        parentModeTimeOut = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                if(Utility.getInParentMode()) {
                    setInParentMode(false);
                    Toast.makeText(MainActivity.this,
                            "Logged Out Due to Inactivity",
                            Toast.LENGTH_SHORT).show();
                }
            }
        };
        Utility.startHandler(parentModeTimeOut, runnable);
    }

    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
        Utility.stopHandler(parentModeTimeOut, runnable);
        Utility.startHandler(parentModeTimeOut, runnable);
    }

    @Override
    public void onPause() {
        super.onPause();
        Utility.stopHandler(parentModeTimeOut, runnable);
    }

    @Override
    public void onResume() {
        super.onResume();
        Utility.startHandler(parentModeTimeOut, runnable);

        if(Utility.getInParentMode()) {
            addEntryButton.setVisibility(View.VISIBLE);
            setPhoneNumberButton.setVisibility(View.VISIBLE);
        }
        else {
            addEntryButton.setVisibility(View.GONE);
            setPhoneNumberButton.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent result) {
        super.onActivityResult(requestCode, resultCode, result);
        if (requestCode == NEW_ENTRY_REQUEST) {
            if (resultCode == RESULT_OK) {
                Bundle extras = result.getExtras();
                ToDoEntry newToDoEntry = (ToDoEntry) extras.getSerializable("ToDoEntry");
                toDoEntries.add(newToDoEntry);
                Collections.sort(toDoEntries);
                recyclerView.setAdapter(adapter);
            }
        }
        if (requestCode == EDIT_ENTRY_REQUEST) {
            if (resultCode == RESULT_OK) {
                Bundle extras = result.getExtras();
                int position = extras.getInt("position");
                if (extras.getBoolean("Deleted")) {
                    toDoEntries.remove(position);
                    adapter.notifyItemRemoved(position);
                } else {
                    ToDoEntry changedToDoEntry = (ToDoEntry) extras.getSerializable("ToDoEntry");
                    toDoEntries.set(position, changedToDoEntry);
                    Collections.sort(toDoEntries);
                }
                recyclerView.setAdapter(adapter);
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

    @Override
    public void onEditClick(int position) {
        Intent intent = new Intent(this, CreateToDoEntryActivity.class);
        intent.putExtra("ToDoEntry", toDoEntries.get(position));
        intent.putExtra("position", position);
        startActivityForResult(intent, EDIT_ENTRY_REQUEST);
    }

    public void setPointsDisplay() {
        pointsDisplay.setText(String.format(Locale.US, "$%d", pointsEarned));
    }

    public void setInParentMode(Boolean inParentMode) {
        Utility.setInParentMode(inParentMode);
        if(inParentMode) {
            setPhoneNumberButton.setVisibility(View.VISIBLE);
            addEntryButton.setVisibility(View.VISIBLE);
        }
        else {
            setPhoneNumberButton.setVisibility(View.GONE);
            addEntryButton.setVisibility(View.GONE);
        }
    }
}