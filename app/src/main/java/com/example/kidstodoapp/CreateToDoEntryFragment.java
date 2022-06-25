//Need to pass the bundle to ToDoListFragment
//So instead of just calling onBackPressed
//Need to also commit ToDoListFragment

package com.example.kidstodoapp;

import androidx.fragment.app.Fragment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class CreateToDoEntryFragment extends Fragment {

    private DatePickerDialog.OnDateSetListener onDateSetListener;
    private TimePickerDialog.OnTimeSetListener onTimeSetListener;

    private Calendar cal = Calendar.getInstance();
    private boolean dateSet = false;
    private boolean timeSet = false;

    private Bundle bundle;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_create_to_do_entry, container, false);

        Button createEntryButton = view.findViewById(R.id.create_entry_button);
        Button cancelEntryButton = view.findViewById(R.id.cancel_entry_button);
        Button deleteEntryButton = view.findViewById(R.id.delete_entry_button);

        deleteEntryButton.setVisibility(View.INVISIBLE);

        final Spinner categorySpinner = view.findViewById(R.id.category_spinner);
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<String>(
                getContext(), android.R.layout.simple_spinner_item, ToDoEntry.getCategories());
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(categoryAdapter);

        final TextView entryDateTextView = view.findViewById(R.id.edit_date_textview);
        final TextView entryTimeTextView = view.findViewById(R.id.edit_time_textview);

        final EditText entryNameEditText = view.findViewById(R.id.entry_name_txt);
        final EditText entryDescriptionEditText = view.findViewById(R.id.entry_description_txt);
        final EditText entryPointsEditText = view.findViewById(R.id.entry_points_txt);

        entryDateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        CreateToDoEntryFragment.this.getContext(),
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
                        CreateToDoEntryFragment.this.getContext(),
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

        bundle = getArguments();
        if(bundle.getSerializable("ToDoEntry") != null) {
            ToDoEntry toDoEntry = (ToDoEntry) bundle.getSerializable("ToDoEntry");
            dateSet = true;
            timeSet = true;
            cal.setTimeInMillis(toDoEntry.getDateTimeMillis());
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
                    Toast.makeText(CreateToDoEntryFragment.this.getContext(),
                            "Please give this task a name",
                            Toast.LENGTH_SHORT).show();
                }
                else if (!dateSet || !timeSet) {
                    Toast.makeText(CreateToDoEntryFragment.this.getContext(),
                            "Please select a date and time",
                            Toast.LENGTH_SHORT).show();
                }
                else {
                    int points;
                    if (TextUtils.isEmpty(entryPointsEditText.getText().toString())) {
                        points = 0;
                    }
                    else {
                        try {
                            points = Integer.parseInt(entryPointsEditText.getText().toString());
                        } catch (NumberFormatException e) {
                            points = 0;
                        }
                    }
                    ToDoEntry newEntry = new ToDoEntry(
                            entryNameEditText.getText().toString(),
                            entryDescriptionEditText.getText().toString(),
                            points, cal.getTimeInMillis(),
                            getDateTimeString(cal),
                            categorySpinner.getSelectedItem().toString()
                    );
                    bundle.putSerializable("ToDoEntry", newEntry);
                    bundle.putBoolean("Deleted", false);
                    bundle.putInt("resultCode", RESULT_OK);
                    FragmentViewModel.setReturnBundle(bundle);
                    getActivity().getSupportFragmentManager().beginTransaction().remove(CreateToDoEntryFragment.this).commit();
                    getActivity().getSupportFragmentManager().popBackStack();
                }
            }
        });

        deleteEntryButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                bundle.putBoolean("Deleted", true);
                bundle.putInt("resultCode", RESULT_OK);
                FragmentViewModel.setReturnBundle(bundle);
                getActivity().getSupportFragmentManager().beginTransaction().remove(CreateToDoEntryFragment.this).commit();
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        cancelEntryButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                cancel();
            }
        });
        return view;
    }

    private String getDateString(Calendar cal) {
        SimpleDateFormat formatter = new SimpleDateFormat("EEE, MMM d");
        return formatter.format(cal.getTime());
    }

    private String getTimeString(Calendar cal) {
        SimpleDateFormat formatter = new SimpleDateFormat("h:mm a");
        return formatter.format(cal.getTime());
    }

    private String getDateTimeString(Calendar cal) {
        SimpleDateFormat formatter = new SimpleDateFormat("EEEE, MMMM d, h:mm a");
        return formatter.format(cal.getTime());
    }

    private void cancel() {
        bundle.putInt("resultCode", RESULT_CANCELED);
        FragmentViewModel.setReturnBundle(bundle);
        getActivity().getSupportFragmentManager().beginTransaction().remove(CreateToDoEntryFragment.this).commit();
        getActivity().getSupportFragmentManager().popBackStack();
    }
}