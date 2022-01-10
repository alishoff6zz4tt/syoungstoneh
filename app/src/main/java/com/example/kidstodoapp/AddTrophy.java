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
import android.widget.ImageView;

public class AddTrophy extends AppCompatActivity {

    ImageView imageView;
    Button button;
    private static final int PICK_IMAGE = 100;
    Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trophy);

        Button createTrophy = findViewById(R.id.create_trophy_button);
        Button cancelCreation = findViewById(R.id.cancel_creation_button);
        final EditText addName = findViewById(R.id.input_name);
        final EditText addDescription = findViewById(R.id.input_description);
        final EditText addPoints = findViewById(R.id.input_points);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trophy);
        imageView = (ImageView)findViewById(R.id.icon);
        button = (Button)findViewById(R.id.pick_icon);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
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
                            addDescription.getText().toString(),
                            points
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

    private void openGallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE){
            imageUri = data.getData();
            imageView.setImageURI(imageUri);
        }
    }

}