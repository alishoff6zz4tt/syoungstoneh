package com.example.kidstodoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

public class AddTrophy extends AppCompatActivity {

    ImageView imageView;
    Button button;
    private static final int PICK_IMAGE = 100;
    private ImageButton image1;
    private ImageButton image2;
    private ImageButton image3;
    private String imageLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trophy);

        Button createTrophy = findViewById(R.id.create_trophy_button);
        Button cancelCreation = findViewById(R.id.cancel_creation_button);
        final EditText addName = findViewById(R.id.input_name);
        final EditText addDescription = findViewById(R.id.input_description);
        final EditText addPoints = findViewById(R.id.input_points);

        image1 = findViewById(R.id.imageButton);
        image1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                imageLocation = "@drawable/trophy1";
            }
        });

        image2 = findViewById(R.id.imageButton2);
        image2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                imageLocation = "@drawable/trophy2";
            }
        });

        image3 = findViewById(R.id.imageButton3);
        image3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                imageLocation = "@drawable/trophy3";
            }
        });

        createTrophy.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (TextUtils.isEmpty(addName.getText().toString())) {
                    cancel();
                }
                else {
                    int points;
                    if (TextUtils.isEmpty(addPoints.getText().toString())) {
                        points = 0;
                    }
                    else {
                        points = Integer.parseInt(addPoints.getText().toString());
                    }
                    Trophy newTrophy = new Trophy(
                            addName.getText().toString(),
                            Integer.parseInt(addPoints.getText().toString())
                            //,imageLocation
                    );
                    Intent result = new Intent();
                    result.putExtra("Trophy", newTrophy);
                    setResult(RESULT_OK, result);
                    finish();
                }
            }
        });

        cancelCreation.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                cancel();
            }
        });
    }

    private void cancel() {
        Intent result = new Intent();
        setResult(RESULT_CANCELED, result);
        finish();
    }
}