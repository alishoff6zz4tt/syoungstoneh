package com.example.kidstodoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class PickImage extends AppCompatActivity {

    private ImageButton image1;
    private ImageButton image2;
    private ImageButton image3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_image);

        image1 = findViewById(R.id.imageButton);
        image1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                //set image to this one;
                Intent intent = new Intent(view.getContext(), CreateTrophyActivity.class);
                startActivity(intent);
            }
        });

        image2 = findViewById(R.id.imageButton2);
        image2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                //set image to this one;

            }
        });

        image3 = findViewById(R.id.imageButton3);
        image3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                //set image to this one;
            }
        });
    }
}