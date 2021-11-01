package com.example.kidstodoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CreateToDoEntryActivity extends AppCompatActivity {

    private DatePickerDialog.OnDateSetListener onDateSetListener;
    private TimePickerDialog.OnTimeSetListener onTimeSetListener;

    private Calendar cal = Calendar.getInstance();
    private boolean dateSet = false;
    private boolean timeSet = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_to_do_entry);

        Button createEntryButton = findViewById(R.id.create_entry_button);
        Button cancelEntryButton = findViewById(R.id.cancel_entry_button);
        Button deleteEntryButton = findViewById(R.id.delete_entry_button);

        deleteEntryButton.setVisibility(View.INVISIBLE);

        final Spinner categorySpinner = findViewById(R.id.category_spinner);
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, ToDoEntry.getCategories());
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(categoryAdapter);

        final TextView entryDateTextView = findViewById(R.id.edit_date_textview);
        final TextView entryTimeTextView = findViewById(R.id.edit_time_textview);

        final EditText entryNameEditText = findViewById(R.id.entry_name_txt);
        final EditText entryDescriptionEditText = findViewById(R.id.entry_description_txt);
        final EditText entryPointsEditText = findViewById(R.id.entry_points_txt);

        entryDateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        CreateToDoEntryActivity.this,
                        onDateSetListener, year, month, day);
                datePickerDialog.show();
            }
        });

        entryTimeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int hour = cal.get(Calendar.HOUR_OF_DAY);
                int minutes = cal.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        CreateToDoEntryActivity.this,
                        onTimeSetListener, hour, minutes, false);
                timePickerDialog.show();
            }
        });

        onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                dateSet = true;
                cal.set(year, month, day);
                entryDateTextView.setText(getDateString(cal));
            }
        };

        onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                timeSet = true;
                cal.set(Calendar.HOUR_OF_DAY, hour);
                cal.set(Calendar.MINUTE, minute);
                cal.set(Calendar.SECOND, 0);
                cal.set(Calendar.MILLISECOND, 0);
                entryTimeTextView.setText(getTimeString(cal));
            }
        };

        Intent intent = getIntent();
        if(intent.hasExtra("ToDoEntry")) {
            ToDoEntry toDoEntry = (ToDoEntry) intent.getExtras().getSerializable("ToDoEntry");
            dateSet = true;
            timeSet = true;
            cal = toDoEntry.getDateTimeDue();
            entryDateTextView.setText(getDateString(cal));
            entryTimeTextView.setText(getTimeString(cal));
            categorySpinner.setSelection(ToDoEntry.getCategories().indexOf(toDoEntry.getCategory()));
            createEntryButton.setText("Save");
            entryNameEditText.setText(toDoEntry.getEntryName());
            entryDescriptionEditText.setText(toDoEntry.getDescription());
            entryPointsEditText.setText(String.valueOf(toDoEntry.getPointValue()));
            deleteEntryButton.setVisibility(View.VISIBLE);
        }

        createEntryButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            if (TextUtils.isEmpty(entryNameEditText.getText().toString())) {
                Toast.makeText(CreateToDoEntryActivity.this,
                        "Please give this task a name",
                        Toast.LENGTH_SHORT).show();
            }
            else if (!dateSet || !timeSet) {
                Toast.makeText(CreateToDoEntryActivity.this,
                        "Please select a date and time",
                        Toast.LENGTH_SHORT).show();
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
                        points, cal, categorySpinner.getSelectedItem().toString()
                );
                Intent result = new Intent();
                result.putExtra("ToDoEntry", newEntry);
                result.putExtra("Deleted", false);
                setResult(RESULT_OK, result);
                finish();
            }
            }
        });

        deleteEntryButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent result = new Intent();
                result.putExtra("Deleted", true);
                setResult(RESULT_OK, result);
                finish();
            }
        });

        cancelEntryButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                cancel();
            }
        });
    }

    private String getDateString(Calendar cal) {
        SimpleDateFormat formatter = new SimpleDateFormat("EEE, MMM d");
        return formatter.format(cal.getTime());
    }

    private String getTimeString(Calendar cal) {
        SimpleDateFormat formatter = new SimpleDateFormat("h:mm a");
        return formatter.format(cal.getTime());
    }

    private void cancel() {
        Intent result = new Intent();
        setResult(RESULT_CANCELED, result);
        finish();
    }
}