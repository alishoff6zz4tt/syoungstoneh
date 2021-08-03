package com.example.kidstodoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class CreateToDoEntryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_to_do_entry);

        Button createEntryButton = (Button) findViewById(R.id.create_entry_button);
        final EditText entryNameEditText = (EditText) findViewById(R.id.entry_name_txt);
        final EditText entryDescriptionEditText = (EditText) findViewById(R.id.entry_description_txt);

        createEntryButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                ToDoEntry newEntry = new ToDoEntry(
                        entryNameEditText.getText().toString(),
                        entryDescriptionEditText.getText().toString()
                );
                //TODO: Add new ToDoEntry to list and navigate to MainActivity
                Intent intent = new Intent(view.getContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }
}