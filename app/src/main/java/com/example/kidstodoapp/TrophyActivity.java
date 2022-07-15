package com.example.kidstodoapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class TrophyActivity extends AppCompatActivity {

    //Talk about redeemed vs purchases
    //Fix images
    //Fix format of linearlayout

    private DataModel model;
    private Trophy mTrophy;
    private int position;
    private Button cancelButton;
    private Button buyButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trophy);
        model = DataModel.getInstance();

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        position = extras.getInt("position");
        mTrophy = model.getExistingTrophies().get(position);

        final TextView nameTextView = findViewById(R.id.trophy_name_textview);
        TextView descriptionTextView = findViewById(R.id.trophy_description_textview);
        TextView pointsTextView = findViewById(R.id.trophy_points_textview);
        ImageView iconImageView = findViewById(R.id.icon_view);

        buyButton = findViewById(R.id.buy_button);
        buyButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if(model.getPointsEarned() - mTrophy.getPointValue() < 0) {
                    Toast toast = Toast.makeText(TrophyActivity.this,
                            "You don't have enough money to buy this trophy.\nSorry!",
                            Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else {
                    model.redeemTrophy(position);
                }
                Intent result = new Intent();
                setResult(RESULT_OK, result);
                finish();

            }
        });

        cancelButton = findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                cancel();
            }
        });


        nameTextView.setText(mTrophy.getName());
        //descriptionTextView.setText(mTrophy.getDescription());
        iconImageView.setImageResource(R.drawable.trophy2);
        String points = "for" + mTrophy.getPointValue() + "points";
        pointsTextView.setText(points);
        //redeemed.setChecked(mTrophy.isRedeemed());

        if (getSupportActionBar() != null) {
            ActionBar actionBar = getSupportActionBar();
            actionBar.setDisplayHomeAsUpEnabled(false);
        }

    }

    private void cancel() {
        Intent result = new Intent();
        setResult(RESULT_CANCELED, result);
        finish();
    }
}