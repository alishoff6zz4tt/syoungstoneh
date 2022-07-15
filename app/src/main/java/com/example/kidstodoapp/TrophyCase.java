package com.example.kidstodoapp;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.OnLifecycleEvent;
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

import java.util.Locale;
import java.util.Observable;

public class TrophyCase extends AppCompatActivity implements java.util.Observer, TrophyAdapter.OnEntryListener  {

    private DataModel model;

    private TrophyAdapter adapter;
    private RecyclerView recyclerView;

    private ImageButton trophy;
    private TextView pointsDisplay;
    private Button createNewTrophy;

    private Toolbar toolbar;

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
                    startActivity(intent);

        }});

    }

    public void setPointsDisplay() {
        pointsDisplay.setText(String.format(Locale.US, "Total Points: $%d", model.getPointsEarned()));
    }

    public void onParentModeChanged() {
        if(ParentModeUtility.isInParentMode()) {
            createNewTrophy.setVisibility(View.VISIBLE);
        }
        else {
            adapter.setVIEW_TYPE(TrophyAdapter.ITEM_TYPE_NO_EDIT);
        }
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent result) {
        super.onActivityResult(requestCode, resultCode, result);
        adapter = new TrophyAdapter(model.getExistingTrophies(), this);
        recyclerView.setAdapter(adapter);
        setPointsDisplay();
    }

    @Override
    public void onEntryClick(int position) {
        Intent intent = new Intent(this, TrophyActivity.class);
        intent.putExtra("position", position);
        startActivityForResult(intent, 0);
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




