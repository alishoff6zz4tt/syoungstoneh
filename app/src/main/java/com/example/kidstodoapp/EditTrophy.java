package com.example.kidstodoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

public class EditTrophy extends AppCompatActivity {

    private Trophy Trophy;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_trophy);

        /*
        Intent intent = getIntent();

        Bundle extras = intent.getExtras();
        this.Trophy = (Trophy) extras.getSerializable("Trophy");
        this.position = extras.getInt("position");

        TextView nameTV = findViewById(R.id.entry_name_textview);
        TextView descriptionTV = findViewById(R.id.entry_description_textview);
        TextView pointsTV = findViewById(R.id.entry_points_textview);
        final CheckBox completionCheckBox = findViewById(R.id.completion_check_box);

        ///add redeemed function

        nameTV.setText(Trophy.getName());
        descriptionTV.setText(Trophy.getDescription());
        int points = Trophy.getPoints();
        pointsTV.setText(points);

        completionCheckBox.setChecked(Trophy.isCompleted()); */
    }

}