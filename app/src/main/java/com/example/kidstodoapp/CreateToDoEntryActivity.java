package com.example.kidstodoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class CreateToDoEntryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_to_do_entry);

        Button createEntryButton = findViewById(R.id.create_entry_button);
        Button cancelEntryButton = findViewById(R.id.cancel_entry_button);
        final EditText entryNameEditText = findViewById(R.id.entry_name_txt);
        final EditText entryDescriptionEditText = findViewById(R.id.entry_description_txt);
        final EditText entryPointsEditText = findViewById(R.id.entry_points_txt);

        Intent intent = getIntent();
        if(intent.hasExtra("ToDoEntry")) {
            ToDoEntry toDoEntry = (ToDoEntry) intent.getExtras().getSerializable("ToDoEntry");
            createEntryButton.setText("Save");
            entryNameEditText.setText(toDoEntry.getEntryName());
            entryDescriptionEditText.setText(toDoEntry.getDescription());
            entryPointsEditText.setText(String.valueOf(toDoEntry.getPointValue()));
        }

        createEntryButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            if (TextUtils.isEmpty(entryNameEditText.getText().toString())) {
                cancel();
            }
            else {
                int points;
                if (TextUtils.isEmpty(entryPointsEditText.getText().toString())) {
                    points = 0;
                }
                else {
                    points = Integer.parseInt(entryPointsEditText.getText().toString());
                }
                ToDoEntry newEntry = new ToDoEntry(
                    entryNameEditText.getText().toString(),
                    entryDescriptionEditText.getText().toString(),
                    points
                );
                Intent result = new Intent();
                result.putExtra("ToDoEntry", newEntry);
                setResult(RESULT_OK, result);
                finish();
            }
            }
        });

        cancelEntryButton.setOnClickListener(new View.OnClickListener() {
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