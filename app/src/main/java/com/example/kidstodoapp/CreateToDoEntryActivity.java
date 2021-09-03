package com.example.kidstodoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CreateToDoEntryActivity extends AppCompatActivity {

    private Handler parentModeTimeOut;
    private Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_to_do_entry);

        Button createEntryButton = findViewById(R.id.create_entry_button);
        Button cancelEntryButton = findViewById(R.id.cancel_entry_button);
        final EditText entryNameEditText = findViewById(R.id.entry_name_txt);
        final EditText entryDescriptionEditText = findViewById(R.id.entry_description_txt);
        final EditText entryPointsEditText = findViewById(R.id.entry_points_txt);

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

        parentModeTimeOut = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                if(Utility.getInParentMode()) {
                    Utility.setInParentMode(false);
                    Toast.makeText(CreateToDoEntryActivity.this,
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

    @Override
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