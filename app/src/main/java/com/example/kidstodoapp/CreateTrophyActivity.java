package com.example.kidstodoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;


public class CreateTrophyActivity extends AppCompatActivity {

    private Handler parentModeTimeOut;
    private Runnable runnable;
    String iconPath = "@drawable/trophy1";

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_trophy);

        Button createTrophyButton = findViewById(R.id.create_trophy_button);
        Button cancelTrophyButton = findViewById(R.id.cancel_trophy_button);
        Button deleteTrophyButton = findViewById(R.id.delete_trophy_button);

        final ImageButton icon1 = findViewById(R.id.icon1);
        final ImageButton icon2 = findViewById(R.id.icon2);
        final ImageButton icon3 = findViewById(R.id.icon3);


        final ImageView border1 = findViewById(R.id.border1);
        final ImageView border2 = findViewById(R.id.border2);
        final ImageView border3 = findViewById(R.id.border3);

        border1.setVisibility(View.INVISIBLE);
        border2.setVisibility(View.INVISIBLE);
        border3.setVisibility(View.INVISIBLE);
        deleteTrophyButton.setVisibility(View.INVISIBLE);

        final EditText trophyNameEditText = findViewById(R.id.trophy_name_txt);
        //final EditText trophyDescriptionEditText = findViewById(R.id.trophy_description_txt);
        final EditText trophyPointsEditText = findViewById(R.id.trophy_points_txt);
        final ImageView imageView = findViewById(R.id.icon_view);

        icon1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                iconPath = "R.drawable.trophy1";
                border1.setVisibility(View.INVISIBLE);
                border2.setVisibility(View.VISIBLE);
                border3.setVisibility(View.INVISIBLE);
                //imageView.setImageResource(R.drawable.trophy1);
            }
        });

        icon2.setOnTouchListener(new ButtonHighlight(icon2));
        icon2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                iconPath = "R.drawable.trophy2";
                border1.setVisibility(View.VISIBLE);
                border2.setVisibility(View.INVISIBLE);
                border3.setVisibility(View.INVISIBLE);
                //imageView.setImageResource(R.drawable.trophy2);
            }
        });

        icon3.setOnTouchListener(new ButtonHighlight(icon3));
        icon3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                iconPath = "R.drawable.trophy3";
                border1.setVisibility(View.INVISIBLE);
                border2.setVisibility(View.INVISIBLE);
                border3.setVisibility(View.VISIBLE);
                //imageView.setImageResource(R.drawable.trophy3);
            }
        });

        Intent intent = getIntent();
        if(intent.hasExtra("Trophy")) {
            Trophy trophy = (Trophy) intent.getExtras().getSerializable("Trophy");
            createTrophyButton.setText("Save");
            trophyNameEditText.setText(trophy.getName());
            //trophyDescriptionEditText.setText(trophy.getDescription());
            //int id = getResources().getIdentifier(trophy.getImage(), "drawable", getPackageName());

            trophyPointsEditText.setText(String.valueOf(trophy.getPoints()));

            /*icon1.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    imageView.setImageResource(R.drawable.trophy1);
                }
            });

            icon2.setOnTouchListener(new ButtonHighlight(icon2));
            icon2.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    imageView.setImageResource(R.drawable.trophy2);
                }
            });

            icon3.setOnTouchListener(new ButtonHighlight(icon3));
            icon3.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    imageView.setImageResource(R.drawable.trophy3);
                }
            });*/

            deleteTrophyButton.setVisibility(View.VISIBLE);
        }

        createTrophyButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (TextUtils.isEmpty(trophyNameEditText.getText().toString())) {
                        Toast.makeText(CreateTrophyActivity.this,
                                "Please give this trophy a name",
                                Toast.LENGTH_LONG).show();
                }
                else {
                    int points;
                    if (TextUtils.isEmpty(trophyPointsEditText.getText().toString())) {
                        points = 0;
                    }
                    else {
                        points = Integer.parseInt(trophyPointsEditText.getText().toString());
                    }
                    Trophy newEntry = new Trophy(
                            trophyNameEditText.getText().toString(),
                            //trophyDescriptionEditText.getText().toString(),
                            points
                    );
                    Intent result = new Intent();
                    result.putExtra("Trophy", newEntry);
                    result.putExtra("Deleted", false);
                    setResult(RESULT_OK, result);
                    finish();
                }
            }
        });

        deleteTrophyButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent result = new Intent();
                result.putExtra("Deleted", true);
                setResult(RESULT_OK, result);
                finish();
            }
        });

        cancelTrophyButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                cancel();
            }
        });

        parentModeTimeOut = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                if(Utility.isInParentMode()) {
                    Utility.setInParentMode(false);
                    Toast.makeText(CreateTrophyActivity.this,
                            "Logged Out Due to Inactivity",
                            Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        };
        Utility.startHandler(parentModeTimeOut, runnable);
    }

    private void cancel() {
        Intent result = new Intent();
        setResult(RESULT_CANCELED, result);
        finish();
    }

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
    }
}