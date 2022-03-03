package com.example.kidstodoapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

public class TrophyActivity extends AppCompatActivity {

    //Talk about redeemed vs purchases
    //Fix images
    //Fix format of linearlayout
    private Trophy mTrophy;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trophy);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        this.mTrophy = (Trophy) extras.getSerializable("Trophy");
        this.position = extras.getInt("position");

        final TextView nameTextView = findViewById(R.id.trophy_name_textview);
        TextView descriptionTextView = findViewById(R.id.trophy_description_textview);
        TextView pointsTextView = findViewById(R.id.trophy_points_textview);
        final CheckBox redeemed = findViewById(R.id.redemption_check_box);

        redeemed.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                mTrophy.setRedeemed(redeemed.isChecked());
                Intent result = new Intent();
                result.putExtra("position", position);
                setResult(RESULT_OK, result);
                finish();
            }
        });

        nameTextView.setText(mTrophy.getName());
        descriptionTextView.setText(mTrophy.getDescription());
        String points = "Redeem for $" + mTrophy.getPoints();
        pointsTextView.setText(points);
        redeemed.setChecked(mTrophy.isRedeemed());

        if (getSupportActionBar() != null) {
            ActionBar actionBar = getSupportActionBar();
            actionBar.setDisplayHomeAsUpEnabled(false);
        }

    }
}