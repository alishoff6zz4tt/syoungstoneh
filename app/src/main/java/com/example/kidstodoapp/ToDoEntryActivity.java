package com.example.kidstodoapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

public class ToDoEntryActivity extends AppCompatActivity {

    private ToDoEntry mToDoEntry;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do_entry);

        Intent intent = getIntent();

        Bundle extras = intent.getExtras();
        this.mToDoEntry = (ToDoEntry) extras.getSerializable("ToDoEntry");
        this.position = extras.getInt("position");

        TextView nameTextView = findViewById(R.id.entry_name_textview);
        TextView descriptionTextView = findViewById(R.id.entry_description_textview);
        TextView pointsTextView = findViewById(R.id.entry_points_textview);
        final CheckBox completionCheckBox = findViewById(R.id.completion_check_box);

        completionCheckBox.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                mToDoEntry.setCompleted(completionCheckBox.isChecked());
                Intent result = new Intent();
                result.putExtra("position", position);
                setResult(RESULT_OK, result);
                finish();
            }
        });

        nameTextView.setText(mToDoEntry.getEntryName());
        descriptionTextView.setText(mToDoEntry.getDescription());
        String points = "$" + mToDoEntry.getPointValue();
        pointsTextView.setText(points);

        completionCheckBox.setChecked(mToDoEntry.isCompleted());

        if (getSupportActionBar() != null) {
            ActionBar actionBar = getSupportActionBar();
            actionBar.setDisplayHomeAsUpEnabled(false);
        }
    }
}