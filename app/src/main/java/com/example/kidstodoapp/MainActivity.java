package com.example.kidstodoapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
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

public class MainActivity extends AppCompatActivity implements ToDoAdapter.OnEntryListener, NavigationView.OnNavigationItemSelectedListener {

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
    private Button addEntryButton;

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;

    private Handler parentModeTimeOut;
    private Runnable runnable;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("KidsToDoApp");
        setSupportActionBar(toolbar);

        navigationView.bringToFront();
        ActionBarDrawerToggle toggleDrawer = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggleDrawer);
        toggleDrawer.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        navigationView.setCheckedItem(R.id.home);
        navigationView.getMenu().findItem(R.id.login).setTitle("Parent Mode");
        navigationView.getMenu().findItem(R.id.phone).setVisible(false);

        TabLayout tabLayout = findViewById(R.id.tab_layout);
        ViewPager viewPager = findViewById(R.id.view_pager);
        tabLayout.setupWithViewPager(viewPager);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        uid = mAuth.getCurrentUser().getUid();

        pointsDisplay = findViewById(R.id.points_display);
        adapter = new ToDoAdapter(toDoEntries, this);

        final DocumentReference documentReference = db.collection("users").document(uid);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException error) {
                toDoEntries = buildToDoEntries((ArrayList<HashMap<String, Object>>) snapshot.get("toDoEntries"));
                completedEntries = buildToDoEntries((ArrayList<HashMap<String, Object>>) snapshot.get("completedEntries"));
                pointsEarned = (Long) snapshot.get("pointsEarned");
                ParentModeUtility.setPhoneNumber(snapshot.getString("phoneNumber"));
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

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        break;
                    case 1:
                        Intent intent = new Intent(MainActivity.this, TrophyCase.class);
                        startActivity(intent);
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        addEntryButton.setVisibility(View.GONE);

        parentModeTimeOut = new Handler();
        runnable = new Runnable() {                               //This is what is done every x milliseconds unless the user
            @Override                                             //interacts with the screen
            public void run() {
                if (ParentModeUtility.isInParentMode() && !ParentModeUtility.isParentDevice()) {
                    ParentModeUtility.setInParentMode(false);
                    onParentModeChanged();
                    Toast.makeText(MainActivity.this,
                            "Exiting parent mode due to inactivity",
                            Toast.LENGTH_SHORT).show();
                }
            }
        };
        ParentModeUtility.startHandler(parentModeTimeOut, runnable);         //Starts the countdown to running the runnable
    }

    @Override
    public void onUserInteraction() {
        super.onUserInteraction();                                 //Whenever the user interacts with the screen, it resets the handler
        ParentModeUtility.stopHandler(parentModeTimeOut, runnable);
        ParentModeUtility.startHandler(parentModeTimeOut, runnable);
    }

    @Override
    public void onPause() {
        super.onPause();                                           //Whenever the user leaves MainActivity, it stops the handler
        ParentModeUtility.stopHandler(parentModeTimeOut, runnable);
    }

    @Override
    public void onResume() {
        super.onResume();                                          //When the user returns to MainActivity, resumes the handler
        ParentModeUtility.startHandler(parentModeTimeOut, runnable);
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
                documentReference.update("completedEntries", completedEntries);
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

    public void onParentModeChanged() {                              //When parent mode is changed
        if (ParentModeUtility.isInParentMode()) {                               //Set the visibility and views accordingly
            addEntryButton.setVisibility(View.VISIBLE);
            adapter.setVIEW_TYPE(ToDoAdapter.ITEM_TYPE_EDIT);
            navigationView.getMenu().findItem(R.id.login).setTitle("Child Mode");
            navigationView.getMenu().findItem(R.id.phone).setVisible(true);
        } else {
            addEntryButton.setVisibility(View.GONE);
            adapter.setVIEW_TYPE(ToDoAdapter.ITEM_TYPE_NO_EDIT);
            navigationView.getMenu().findItem(R.id.login).setTitle("Parent Mode");
            navigationView.getMenu().findItem(R.id.phone).setVisible(false);
        }
        recyclerView.setAdapter(adapter);
    }

    public ArrayList<ToDoEntry> buildToDoEntries(ArrayList<HashMap<String, Object>> list) {
        ArrayList<ToDoEntry> arrayList = new ArrayList<>();
        for (HashMap<String, Object> map : list) {
            arrayList.add(ToDoEntry.buildToDoEntry(map));
        }
        return arrayList;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:
                break;
            case R.id.login:
                if (ParentModeUtility.isInParentMode()) {               //If the user is in parent mode, logs out and makes the appropriate changes
                    ParentModeUtility.setInParentMode(false);
                    onParentModeChanged();
                    Toast.makeText(MainActivity.this,
                            "Exiting parent mode",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(this, ConfirmPassword.class);
                    startActivity(intent);
                }
                break;
            case R.id.phone:
                Intent intent = new Intent(this, PhoneNumber.class);
                startActivity(intent);
                break;
            case R.id.faq:
                Intent intent1 = new Intent(this, FAQ.class);
                startActivity(intent1);
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}