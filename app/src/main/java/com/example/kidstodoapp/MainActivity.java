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
import java.util.Collections;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements ToDoAdapter.OnEntryListener {

    private static ArrayList<ToDoEntry> toDoEntries = new ArrayList<>();
    private static ArrayList<ToDoEntry> completedEntries = new ArrayList<>();
    private static int pointsEarned = 0;

    private final int NEW_ENTRY_REQUEST = 1;
    private final int VIEW_ENTRY_REQUEST = 2;
    private final int EDIT_ENTRY_REQUEST = 3;

    private ToDoAdapter adapter;
    private RecyclerView recyclerView;
    private TextView pointsDisplay;
    private Button parentModeButton;
    private Button addEntryButton;

    private Button trophyCaseButton;
    private ImageButton setPhoneNumberButton;
    private ImageButton faqButton;

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

        trophyCaseButton = findViewById(R.id.TrophyCase);
        trophyCaseButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), TrophyCase.class);
                startActivity(intent);
            }
        });

        parentModeButton = findViewById(R.id.pM);
        setPhoneNumberButton = findViewById(R.id.phone);
        //faqButton = findViewById(R.id.faq);

        addEntryButton.setVisibility(View.GONE);
        setPhoneNumberButton.setVisibility(View.GONE);

        parentModeButton.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  if(Utility.isInParentMode()) {               //If the user is in parent mode, logs out and makes the appropriate changes
                      Utility.setInParentMode(false);
                      onParentModeChanged();
                      Toast.makeText(MainActivity.this,
                              "Logged Out",
                              Toast.LENGTH_SHORT).show();
                  }
                  else if(Utility.isPasswordSet() == null) {  //If it is the parents first time, creates a password
                      Intent intent = new Intent(view.getContext(), ParentModeFirstTime.class);
                      startActivity(intent);
                  }
                  else {                                        //Else, sends to log in screen
                      Intent intent = new Intent(view.getContext(), ParentMode.class);
                      intent.putExtra(Utility.getPassword(), Utility.getPassword());
                      startActivity(intent);
                  }
              }
        });

        setPhoneNumberButton.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {                   //Sets the phone number for the parent
                  Intent intent = new Intent(view.getContext(), PhoneNumber.class);
                  startActivity(intent);
              }
        });

//        faqButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(view.getContext(), FAQ.class);
//                startActivity(intent);
//            }
//        });

        parentModeTimeOut = new Handler();
        runnable = new Runnable() {                               //This is what is done every x milliseconds unless the user
            @Override                                             //interacts with the screen
            public void run() {
                if(Utility.isInParentMode()) {
                    Utility.setInParentMode(false);
                    onParentModeChanged();
                    Toast.makeText(MainActivity.this,
                            "Logged Out Due to Inactivity",
                            Toast.LENGTH_SHORT).show();
                }
            }
        };
        Utility.startHandler(parentModeTimeOut, runnable);         //Starts the countdown to running the runnable
    }

    @Override
    public void onUserInteraction() {
        super.onUserInteraction();                                 //Whenever the user interacts with the screen, it resets the handler
        Utility.stopHandler(parentModeTimeOut, runnable);
        Utility.startHandler(parentModeTimeOut, runnable);
    }

    @Override
    public void onPause() {
        super.onPause();                                           //Whenever the user leaves MainActivity, it stops the handler
        Utility.stopHandler(parentModeTimeOut, runnable);
    }

    @Override
    public void onResume() {
        super.onResume();                                          //When the user returns to MainActivity, resumes the handler
        Utility.startHandler(parentModeTimeOut, runnable);
        onParentModeChanged();
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

    public void onParentModeChanged() {                              //When parent mode is changed
        if(Utility.isInParentMode()) {                               //Set the visibility and views accordingly
            setPhoneNumberButton.setVisibility(View.VISIBLE);
            addEntryButton.setVisibility(View.VISIBLE);
            adapter.setVIEW_TYPE(ToDoAdapter.ITEM_TYPE_EDIT);
            parentModeButton.setText(getResources().getString(R.string.logout));
        }
        else {
            setPhoneNumberButton.setVisibility(View.GONE);
            addEntryButton.setVisibility(View.GONE);
            adapter.setVIEW_TYPE(ToDoAdapter.ITEM_TYPE_NO_EDIT);
            parentModeButton.setText(getResources().getString(R.string.login));
        }
        recyclerView.setAdapter(adapter);
    }
}