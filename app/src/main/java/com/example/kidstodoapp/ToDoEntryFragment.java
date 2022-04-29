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
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import static android.app.Activity.RESULT_OK;

public class ToDoEntryFragment extends Fragment {

    private ToDoEntry mToDoEntry;
    private int position;
    private Bundle bundle;

    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MainActivity)getActivity()).tabVisibility(true);
        View view = inflater.inflate(R.layout.fragment_to_do_entry, container, false);

        bundle = getArguments();

        this.mToDoEntry = (ToDoEntry) bundle.getSerializable("ToDoEntry");
        this.position = bundle.getInt("position");

        TextView categoryTextView = view.findViewById(R.id.category_textview);
        final TextView nameTextView = view.findViewById(R.id.entry_name_textview);
        TextView descriptionTextView = view.findViewById(R.id.entry_description_textview);
        TextView dateTimeTextView = view.findViewById(R.id.entry_datetime_textview);
        TextView pointsTextView = view.findViewById(R.id.entry_points_textview);
        final CheckBox completionCheckBox = view.findViewById(R.id.completion_check_box);
        ImageButton sms = view.findViewById(R.id.sms);

        completionCheckBox.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                mToDoEntry.setCompleted(completionCheckBox.isChecked());
                bundle.putInt("position", position);
                bundle.putInt("resultCode", RESULT_OK);
                Utility.setReturnBundle(bundle);
                getActivity().getSupportFragmentManager().beginTransaction().remove(ToDoEntryFragment.this).commit();
                getActivity().getSupportFragmentManager().popBackStack();

            }
        });

        categoryTextView.setText(mToDoEntry.getCategory());

        sms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {                                    //For sending messages to the parent asking for help
                if(Utility.isPhoneNumberSet()) {smsDialog(nameTextView.getText().toString());}
                else {                                                          //If a phone number hasn't been set
                    Toast.makeText(ToDoEntryFragment.this.getContext(),
                            "A Phone Number Has Not Been Added",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        nameTextView.setText(mToDoEntry.getEntryName());
        descriptionTextView.setText(mToDoEntry.getDescription());
        dateTimeTextView.setText(mToDoEntry.getDateTimeString());
        String points = "$" + mToDoEntry.getPointValue();
        pointsTextView.setText(points);

        completionCheckBox.setChecked(mToDoEntry.isCompleted());

        return view;
    }

    @SuppressLint("IntentReset")
    private void sendSms(AlertDialog dialog,final String title) {               //Tries to send an sms
        String message = "Automated message from KidsToDoApp:" + "\n" + "I need help with " + title + "!";
        boolean sent = true;
        try {
            SmsManager.getDefault().sendTextMessage(Utility.getPhoneNumber(), null, message, null, null);
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
}