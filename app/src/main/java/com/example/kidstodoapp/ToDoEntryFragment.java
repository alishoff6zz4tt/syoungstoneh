package com.example.kidstodoapp;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class ToDoEntryFragment extends Fragment {

    private DataModel model;

    private int position;
    private boolean completed;

    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_to_do_entry, container, false);

        model = DataModel.getInstance();

        Bundle bundle = getArguments();

        position = (bundle != null ? bundle.getInt("position") : 0);
        completed = (bundle != null && bundle.getBoolean("completed"));

        ToDoEntry mToDoEntry;
        if (completed) {
            mToDoEntry = model.getCompletedEntries().get(position);
        } else {
            mToDoEntry = model.getToDoEntries().get(position);
        }

        TextView categoryTextView = view.findViewById(R.id.category_textview);
        final TextView nameTextView = view.findViewById(R.id.entry_name_textview);
        TextView descriptionTextView = view.findViewById(R.id.entry_description_textview);
        TextView dateTimeTextView = view.findViewById(R.id.entry_datetime_textview);
        TextView pointsTextView = view.findViewById(R.id.entry_points_textview);
        final CheckBox completionCheckBox = view.findViewById(R.id.completion_check_box);
        ImageButton sms = view.findViewById(R.id.sms);
        Button confirmCompletedButton = view.findViewById(R.id.confirm_completed_button);

        categoryTextView.setText(mToDoEntry.getCategory());
        nameTextView.setText(mToDoEntry.getEntryName());
        descriptionTextView.setText(mToDoEntry.getDescription());
        dateTimeTextView.setText(mToDoEntry.getDateTimeString());
        String points = "$" + mToDoEntry.getPointValue();
        pointsTextView.setText(points);
        completionCheckBox.setChecked(completed);

        if (completed) {
            sms.setVisibility(View.GONE);
            confirmCompletedButton.setVisibility(View.VISIBLE);
        }
        else if (ParentModeUtility.isInParentMode()) {
            sms.setVisibility(View.GONE);
        }

        completionCheckBox.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (!completed && completionCheckBox.isChecked()) {
                    model.completeToDoEntry(position);
                }
                else if (completed && !completionCheckBox.isChecked()) {
                    model.uncompleteToDoEntry(position);
                }
                exit();
            }
        });

        sms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {                                    //For sending messages to the parent asking for help
                if(!model.getPhoneNumber().equals("")) {smsDialog(nameTextView.getText().toString());}
                else {                                                          //If a phone number hasn't been set
                    Toast.makeText(ToDoEntryFragment.this.getContext(),
                            "A Phone Number Has Not Been Added",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        confirmCompletedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                model.confirmCompleted(position);
                exit();
            }
        });

        return view;
    }

    @SuppressLint("IntentReset")
    private void sendSms(AlertDialog dialog,final String title) {               //Tries to send an sms
        String message = "Automated message from KidsToDoApp:" + "\n" + "I need help with " + title + "!";
        boolean sent = true;
        try {
            SmsManager.getDefault().sendTextMessage(model.getPhoneNumber(), null, message, null, null);
        }
        catch(Exception e) {
            Toast.makeText(ToDoEntryFragment.this.getContext(),
                    "Unable to Send Message",
                    Toast.LENGTH_SHORT).show();
            sent = false;
        }
        if(sent) {
            Toast.makeText(ToDoEntryFragment.this.getContext(),
                    "Message Sent",
                    Toast.LENGTH_SHORT).show();
        }
        dialog.dismiss();
    }
                                                                                //Checks to see if the user has given SMS permissions
    private void smsPermissions(AlertDialog dialog, final String title) {       //If they have given permissions, tries to send an SMS
        if (ActivityCompat.checkSelfPermission(ToDoEntryFragment.this.getContext(), Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {sendSms(dialog, title);}
        else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.SEND_SMS}, MY_PERMISSIONS_REQUEST_SEND_SMS);
            if(ActivityCompat.checkSelfPermission(ToDoEntryFragment.this.getContext(), Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {sendSms(dialog, title);}
            else {dialog.dismiss();}
        }
    }

    private void smsDialog(final String title) {                                //Dialog confirming that the user would like to send an SMS to their parent
        AlertDialog.Builder builder = new AlertDialog.Builder(ToDoEntryFragment.this.getContext());
        builder.setTitle("Send SMS");
        builder.setMessage("Would you like to send an SMS to your parent asking for help?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //Later Overridden
                //If there were cases where the dialog should NOT close onClick, then this same tactic would be used
                //But in this case, doing this mainly to change it from an AlertDialog.Builder to an AlertDialog
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //Later Overridden
            }
        });

        final AlertDialog dialog = builder.create();
        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                smsPermissions(dialog, title);                                 //Checks for permissions
            }
        });

        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    private void exit() {
        getActivity().getSupportFragmentManager().beginTransaction().remove(ToDoEntryFragment.this).commit();
        getActivity().getSupportFragmentManager().popBackStack();
    }
}