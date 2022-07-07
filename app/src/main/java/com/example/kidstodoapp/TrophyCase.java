package com.example.kidstodoapp;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Observable;

public class TrophyCase extends AppCompatActivity implements java.util.Observer, TrophyAdapter.OnEntryListener  {

    private DataModel model;

    private TrophyAdapter adapter;
    private RecyclerView recyclerView;

    private final int NEW_ENTRY_REQUEST = 1;
    private final int VIEW_ENTRY_REQUEST = 2;
    private final int EDIT_ENTRY_REQUEST = 3;

    private ImageButton trophy;
    private TextView pointsDisplay;
    private Button createNewTrophy;
    private Button parentModeButton;

    private Toolbar toolbar;

    private final int VIEW = 1;
    private final int NEW = 2;
    private final int EDIT = 3;
    private final int MY_PERMISSIONS_REQUEST_SEND_SMS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trophy_case);

        model = DataModel.getInstance();

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Points Display
        pointsDisplay = findViewById(R.id.points_display);
        setPointsDisplay();

        //Recycler View Setup
        adapter = new TrophyAdapter(model.getExistingTrophies(), this);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //Button to create new trophy
        createNewTrophy = findViewById(R.id.add_trophy_button);
        createNewTrophy.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), CreateTrophyActivity.class);
                    startActivityForResult(intent, NEW_ENTRY_REQUEST);

        }});

        parentModeButton = findViewById(R.id.parentModeTC);
        parentModeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ParentModeUtility.isInParentMode()) {
                    ParentModeUtility.setInParentMode(false);
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
        pointsDisplay.setText(String.format(Locale.US, "Total Points: $%d", model.getPointsEarned()));
    }

    public void onParentModeChanged() {
        if(ParentModeUtility.isInParentMode()) {
            createNewTrophy.setVisibility(View.VISIBLE);
            parentModeButton.setText(getResources().getString(R.string.child));
        }
        else {
            adapter.setVIEW_TYPE(TrophyAdapter.ITEM_TYPE_NO_EDIT);
            parentModeButton.setText(getResources().getString(R.string.parent));
        }
        recyclerView.setAdapter(adapter);
    }

/*    @Override TO DELETE
    protected void onActivityResult(int requestCode, int resultCode, Intent result) {
        super.onActivityResult(requestCode, resultCode, result);

        if (requestCode == VIEW_ENTRY_REQUEST) {
            if (resultCode == RESULT_OK) {
                Bundle extras = result.getExtras();
                int position = extras.getInt("position");
                Trophy trophy = existingTrophies.get(position);
               if(model.getPointsEarned() - trophy.getPoints() < 0) {
                    Toast toast = Toast.makeText(TrophyCase.this,
                            "You don't have enough money to buy this trophy.\nSorry!",
                            Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else {
                   model.redeemTrophy(trophy); // to create
                   *//* trophy = existingTrophies.remove(position);
                    adapter.notifyItemRemoved(position);
                    trophy.setRedeemed(true);
                    archivedTrophies.add(trophy);
                    //pointsEarned -= trophy.getPoints(); //Create a method in DataModel*//*
                }
                setPointsDisplay();
            }
        }
    }*/

    @Override
    public void onEntryClick(int position) {
        Intent intent = new Intent(this, TrophyActivity.class);
        intent.putExtra("position", position);
        startActivityForResult(intent, VIEW_ENTRY_REQUEST);
    }

    @Override
    public void update(Observable observable, Object arg) {
        if (observable instanceof DataModel) {
            adapter = new TrophyAdapter(model.getExistingTrophies(), this);
            recyclerView.setAdapter(adapter);
            setPointsDisplay();
        }
    }
}




