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

// This comment was made by Sean Youngstone
public class MainActivity extends AppCompatActivity implements ToDoAdapter.OnEntryListener {

    private ToDoAdapter adapter = new ToDoAdapter(toDoEntries, this);

    private TextView pointsDisplay;

    private static ArrayList<ToDoEntry> toDoEntries = new ArrayList<>();
    private static ArrayList<ToDoEntry> completedEntries = new ArrayList<>();
    private static int pointsEarned = 0;

    private final int NEW_ENTRY_REQUEST = 1;
    private final int VIEW_ENTRY_REQUEST = 2;

    private String password;
    private String phoneNumber;
    private Boolean notFirstTime = null;
    private static Boolean inParentMode = false;
    private Button parentModeButton;
    private Button addEntryButton;
    private ImageButton setPhoneNumberButton;

    private Handler parentModeTimeOut;
    private Runnable runnable;

    private phoneNumberDialog phoneDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView mRecyclerView = findViewById(R.id.recycler_view);

        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

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

        parentModeButton.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  if(!inParentMode) {openDialog(notFirstTime);}
                  else
                  {
                      inParentMode = false;
                      Toast.makeText(MainActivity.this,
                              "Logged Out",
                              Toast.LENGTH_SHORT).show();
                      addEntryButton.setVisibility(View.GONE);
                      setPhoneNumberButton.setVisibility(View.GONE);
                  }
              }
        });

        setPhoneNumberButton.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  if(inParentMode)
                  {
                      phoneDialog = new phoneNumberDialog();
                      phoneDialog.show(getSupportFragmentManager(), "Set Phone Number");
                  }
                  else {
                      Toast.makeText(MainActivity.this,
                              "Please Enter Parent Mode to Add a Phone Number",
                              Toast.LENGTH_SHORT).show();
                  }
              }
        });

        parentModeTimeOut = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                if(inParentMode) {
                    inParentMode = false;
                    Toast.makeText(MainActivity.this,
                            "Logged Out Due to Inactivity",
                            Toast.LENGTH_SHORT).show();
                    addEntryButton.setVisibility(View.GONE);
                    setPhoneNumberButton.setVisibility(View.GONE);
                    if(phoneDialog.getIsShown()) {phoneDialog.dismiss();}
                }
            }
        };
        startHandler(parentModeTimeOut, runnable);
    }

    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
        stopHandler(parentModeTimeOut, runnable);
        startHandler(parentModeTimeOut, runnable);
    }

    @Override
    public void onPause() {
        super.onPause();
        stopHandler(parentModeTimeOut, runnable);
    }

    @Override
    public void onResume() {
        super.onResume();
        startHandler(parentModeTimeOut, runnable);

        if(inParentMode) {
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

    public void openDialog(Boolean passwordAlreadySet)
    {
        if(passwordAlreadySet==null)
        {
            parentModeSetPassword parentSetPass = new parentModeSetPassword();
            parentSetPass.show(getSupportFragmentManager(), "Create Password");
        }
        else
        {
            parentModeDialog parentMode = new parentModeDialog();
            parentMode.show(getSupportFragmentManager(), "Enter Password");
        }
    }

    public static void stopHandler(Handler handler, Runnable runnable) {handler.removeCallbacks(runnable);}
    public static void startHandler(Handler handler, Runnable runnable) {handler.postDelayed(runnable, 10000);}

    public String getPassword() {return password;}
    public void setPassword(String password) {this.password = password;}
    public Boolean getNotFirstTime() {return notFirstTime;}
    public void setNotFirstTime(Boolean notFirstTime) {this.notFirstTime = notFirstTime;}
    public static Boolean getInParentMode() {return inParentMode;}
    public static void setInParentMode(Boolean inParentMode) {MainActivity.inParentMode = inParentMode;}
    public void setInParentMode(Boolean inParentMode, int i) {
        MainActivity.inParentMode = inParentMode;
        if(inParentMode) {
            addEntryButton.setVisibility(View.VISIBLE);
            setPhoneNumberButton.setVisibility(View.VISIBLE);
        }
        else {
            addEntryButton.setVisibility(View.GONE);
            setPhoneNumberButton.setVisibility(View.GONE);
        }
    }
    public String getPhoneNumber() {return phoneNumber;}
    public void setPhoneNumber(String phoneNumber) {this.phoneNumber = phoneNumber;}
}