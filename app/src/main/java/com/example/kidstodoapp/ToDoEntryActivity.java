package com.example.kidstodoapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class ToDoEntryActivity extends AppCompatActivity {

    private ToDoEntry mToDoEntry;
    private int position;

    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do_entry);

        Intent intent = getIntent();

        Bundle extras = intent.getExtras();
        this.mToDoEntry = (ToDoEntry) extras.getSerializable("ToDoEntry");
        this.position = extras.getInt("position");

        TextView categoryTextView = findViewById(R.id.category_textview);
        final TextView nameTextView = findViewById(R.id.entry_name_textview);
        TextView descriptionTextView = findViewById(R.id.entry_description_textview);
        TextView dateTimeTextView = findViewById(R.id.entry_datetime_textview);
        TextView pointsTextView = findViewById(R.id.entry_points_textview);
        final CheckBox completionCheckBox = findViewById(R.id.completion_check_box);
        ImageButton sms = findViewById(R.id.sms);

        completionCheckBox.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                mToDoEntry.setCompleted(completionCheckBox.isChecked());
                Intent result = new Intent();
                result.putExtra("position", position);
                setResult(RESULT_OK, result);
                finish();
            }
        });

        categoryTextView.setText(mToDoEntry.getCategory());

        sms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Utility.isPhoneNumberSet()) {smsDialog(nameTextView.getText().toString());}
                else {
                    Toast.makeText(ToDoEntryActivity.this,
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

        if (getSupportActionBar() != null) {
            ActionBar actionBar = getSupportActionBar();
            actionBar.setDisplayHomeAsUpEnabled(false);
        }
    }

    @SuppressLint("IntentReset")
    private void sendSms(AlertDialog dialog,final String title) {
        dialog.dismiss();
        String message = "I need help with " + title + "!";
        SmsManager.getDefault().sendTextMessage(Utility.getPhoneNumber(), null, message, null, null);
        Toast.makeText(ToDoEntryActivity.this,
                "Message Sent",
                Toast.LENGTH_SHORT).show();
    }

    private void smsPermissions(AlertDialog dialog, final String title) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {sendSms(dialog, title);}
        else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, MY_PERMISSIONS_REQUEST_SEND_SMS);
            if(ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {sendSms(dialog, title);}
            else {dialog.dismiss();}
        }
    }

    private void smsDialog(final String title) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Send SMS");
        builder.setMessage("Would you like to send an SMS to your parent asking for help?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //Later Overridden
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
                smsPermissions(dialog, title);
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