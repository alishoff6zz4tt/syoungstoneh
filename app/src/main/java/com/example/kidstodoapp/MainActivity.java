package com.example.kidstodoapp;

import androidx.annotation.Nullable;
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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements ToDoAdapter.OnEntryListener {

    private static ArrayList<ToDoEntry> toDoEntries = new ArrayList<>();
    private static ArrayList<ToDoEntry> completedEntries = new ArrayList<>();
    private static long pointsEarned = 0;
    private static String uid;

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

    private Handler parentModeTimeOut;
    private Runnable runnable;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        uid = mAuth.getCurrentUser().getUid();

        pointsDisplay = findViewById(R.id.points_display);
        adapter = new ToDoAdapter(toDoEntries, this);

        final DocumentReference documentReference = db.collection("users").document(uid);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException error) {
                toDoEntries = buildToDoEntries((ArrayList<HashMap<String,Object>>)snapshot.get("toDoEntries"));
                completedEntries = buildToDoEntries((ArrayList<HashMap<String,Object>>)snapshot.get("completedEntries"));
                pointsEarned = (Long)snapshot.get("pointsEarned");
                Utility.setPhoneNumber(snapshot.getString("phoneNumber"));
                adapter = new ToDoAdapter(toDoEntries, MainActivity.this);
                recyclerView.setAdapter(adapter);
                setPointsDisplay();
            }
        });

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

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

        addEntryButton.setVisibility(View.GONE);
        setPhoneNumberButton.setVisibility(View.GONE);

        parentModeButton.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  if(Utility.isInParentMode()) {
                      Utility.setInParentMode(false);
                      onParentModeChanged();
                      Toast.makeText(MainActivity.this,
                              "Exiting parent mode",
                              Toast.LENGTH_SHORT).show();
                  }
                  else {
                      Intent intent = new Intent(view.getContext(), ConfirmPassword.class);
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
                if(Utility.isInParentMode()) {
                    Utility.setInParentMode(false);
                    onParentModeChanged();
                    Toast.makeText(MainActivity.this,
                            "Exiting parent mode due to inactivity",
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
        onParentModeChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent result) {
        final DocumentReference documentReference = db.collection("users").document(uid);
        super.onActivityResult(requestCode, resultCode, result);
        if (requestCode == NEW_ENTRY_REQUEST) {
            if (resultCode == RESULT_OK) {
                Bundle extras = result.getExtras();
                ToDoEntry newToDoEntry = (ToDoEntry) extras.getSerializable("ToDoEntry");
                toDoEntries.add(newToDoEntry);
                Collections.sort(toDoEntries);
                recyclerView.setAdapter(adapter);
                documentReference.update("toDoEntries", toDoEntries);
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
                documentReference.update("toDoEntries", toDoEntries);
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
                documentReference.update("toDoEntries", toDoEntries);
                documentReference.update("pointsEarned", pointsEarned);
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

    public void onParentModeChanged() {
        if(Utility.isInParentMode()) {
            setPhoneNumberButton.setVisibility(View.VISIBLE);
            addEntryButton.setVisibility(View.VISIBLE);
            adapter.setVIEW_TYPE(ToDoAdapter.ITEM_TYPE_EDIT);
            parentModeButton.setText(getResources().getString(R.string.child));
        }
        else {
            setPhoneNumberButton.setVisibility(View.GONE);
            addEntryButton.setVisibility(View.GONE);
            adapter.setVIEW_TYPE(ToDoAdapter.ITEM_TYPE_NO_EDIT);
            parentModeButton.setText(getResources().getString(R.string.parent));
        }
        recyclerView.setAdapter(adapter);
    }

    public ArrayList<ToDoEntry> buildToDoEntries(ArrayList<HashMap<String,Object>> list) {
        ArrayList<ToDoEntry> arrayList = new ArrayList<>();
        for (HashMap<String,Object> map : list) {
            arrayList.add(ToDoEntry.buildToDoEntry(map));
        }
        return arrayList;
    }

    @Override
    public void onBackPressed() {}
}