package com.example.kidstodoapp;
import androidx.appcompat.app.AlertDialog;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Telephony;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

public class TrophyCase extends AppCompatActivity implements TrophyAdapter.OnEntryListener  {

    private static ArrayList<Trophy> existingTrophies = new ArrayList<>();
    private static ArrayList<Trophy> archivedTrophies = new ArrayList<>();

    private static int pointsEarned = 0;
    private TrophyAdapter adapter;
    private RecyclerView recyclerView;

    private final int NEW_ENTRY_REQUEST = 1;
    private final int VIEW_ENTRY_REQUEST = 2;
    private final int EDIT_ENTRY_REQUEST = 3;

    private ImageButton trophy;
    private TextView pointsDisplay;
    private Button createNewTrophy;
    private Button parentModeButton;

    private final int VIEW = 1;
    private final int NEW = 2;
    private final int EDIT = 3;
    private final int MY_PERMISSIONS_REQUEST_SEND_SMS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trophy_case);

        //Points Display
        pointsDisplay = findViewById(R.id.points_display);
        setPointsDisplay();

        //Recycler View Setup
        adapter = new TrophyAdapter(existingTrophies, this);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //Button to create new trophy
        createNewTrophy = findViewById(R.id.add_trophy_button);
        createNewTrophy.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), CreateTrophyActivity.class);
                startActivityForResult(intent, NEW_ENTRY_REQUEST);
            }
        });

        parentModeButton = findViewById(R.id.parentModeTC);
        parentModeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Utility.isInParentMode()) {
                    Utility.setInParentMode(false);
                    onParentModeChanged();
                    Toast.makeText(TrophyCase.this,
                            "Exiting parent mode",
                            Toast.LENGTH_SHORT).show();
                }
                else {
                    Intent intent = new Intent(view.getContext(), ConfirmPassword.class);
                    startActivity(intent);
                }
            }
        });

    }

    public void setPointsDisplay() {
        pointsDisplay.setText(String.format(Locale.US, "Total Points: $%d", pointsEarned));
    }

    public void onParentModeChanged() {
        if(Utility.isInParentMode()) {
            createNewTrophy.setVisibility(View.VISIBLE);
            adapter.setVIEW_TYPE(TrophyAdapter.ITEM_TYPE_EDIT);
            parentModeButton.setText(getResources().getString(R.string.child));
        }
        else {
            adapter.setVIEW_TYPE(TrophyAdapter.ITEM_TYPE_NO_EDIT);
            parentModeButton.setText(getResources().getString(R.string.parent));
        }
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent result) {
        super.onActivityResult(requestCode, resultCode, result);
        if (requestCode == NEW_ENTRY_REQUEST) {
            if (resultCode == RESULT_OK) {
                Bundle extras = result.getExtras();
                Trophy newTrophy = (Trophy) extras.getSerializable("Trophy");
                existingTrophies.add(newTrophy);
                //Collections.sort(existingTrophies);
                recyclerView.setAdapter(adapter);
            }
        }
        if (requestCode == EDIT_ENTRY_REQUEST) {
            if (resultCode == RESULT_OK) {
                Bundle extras = result.getExtras();
                int position = extras.getInt("position");
                if (extras.getBoolean("Deleted")) {
                    existingTrophies.remove(position);
                    adapter.notifyItemRemoved(position);
                } else {
                    Trophy editedTrophy = (Trophy) extras.getSerializable("Trophy");
                    existingTrophies.set(position, editedTrophy);
                    //Collections.sort(existingTrophies);
                }
                recyclerView.setAdapter(adapter);
            }
        }
        if (requestCode == VIEW_ENTRY_REQUEST) {
            if (resultCode == RESULT_OK) {
                Bundle extras = result.getExtras();
                int position = extras.getInt("position");
                Trophy trophy = existingTrophies.remove(position);
                adapter.notifyItemRemoved(position);
                trophy.setRedeemed(true);
                archivedTrophies.add(trophy);
                pointsEarned += trophy.getPoints();
                setPointsDisplay();
            }
        }
    }

    @Override
    public void onEntryClick(int position) {
        Intent intent = new Intent(this, TrophyActivity.class);
        intent.putExtra("Trophy", existingTrophies.get(position));
        intent.putExtra("position", position);
        startActivityForResult(intent, VIEW_ENTRY_REQUEST);
    }

    @Override
    public void onEditClick(int position) {
        Intent intent = new Intent(this, CreateToDoEntryActivity.class);
        intent.putExtra("Trophy", existingTrophies.get(position));
        intent.putExtra("position", position);
        startActivityForResult(intent, EDIT_ENTRY_REQUEST);
    }
}




