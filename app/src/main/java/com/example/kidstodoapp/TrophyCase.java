package com.example.kidstodoapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Telephony;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

public class TrophyCase extends AppCompatActivity implements TrophyAdapter.OnEntryListener  {

    private static ArrayList<Trophy> existingTrophies = new ArrayList<>();
    private static ArrayList<Trophy> archivedTrophies = new ArrayList<>();

    private static int pointsEarned = 0;
    private TrophyAdapter adapter;
    private RecyclerView recyclerView;

    private final int NEW_ENTRY_REQUEST = 1;
    private final int VIEW_ENTRY_REQUEST = 2;
    private final int EDIT_ENTRY_REQUEST = 3;

    private ImageButton trophy;
    private TextView pointsDisplay;
    private Button createNewTrophy;

    private final int VIEW = 1;
    private final int NEW = 2;
    private final int EDIT = 3;
    private final int MY_PERMISSIONS_REQUEST_SEND_SMS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trophy_case);

        //Points Display
        pointsDisplay = findViewById(R.id.points_display);
        setPointsDisplay();

        //Set up recycler view
        adapter = new TrophyAdapter(existingTrophies, this);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        createNewTrophy = findViewById(R.id.add_trophy_button);
        createNewTrophy.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), CreateTrophyActivity.class);
                startActivityForResult(intent, NEW_ENTRY_REQUEST);
            }
        });

    }

    public void setPointsDisplay() {
        pointsDisplay.setText("Total points" + String.format(Locale.US, "$%d", pointsEarned));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent result) {
        super.onActivityResult(requestCode, resultCode, result);
        if (requestCode == NEW_ENTRY_REQUEST) {
            if (resultCode == RESULT_OK) {
                Bundle extras = result.getExtras();
                Trophy newTrophy = (Trophy) extras.getSerializable("Trophy");
                existingTrophies.add(newTrophy);
                //Collections.sort(existingTrophies);
                recyclerView.setAdapter(adapter);
            }
        }
        if (requestCode == EDIT_ENTRY_REQUEST) {
            if (resultCode == RESULT_OK) {
                Bundle extras = result.getExtras();
                int position = extras.getInt("position");
                if (extras.getBoolean("Deleted")) {
                    existingTrophies.remove(position);
                    adapter.notifyItemRemoved(position);
                } else {
                    Trophy editedTrophy = (Trophy) extras.getSerializable("Trophy");
                    existingTrophies.set(position, editedTrophy);
                    //Collections.sort(existingTrophies);
                }
                recyclerView.setAdapter(adapter);
            }
        }
        if (requestCode == VIEW_ENTRY_REQUEST) {
            if (resultCode == RESULT_OK) {
                Bundle extras = result.getExtras();
                int position = extras.getInt("position");
                Trophy trophy = existingTrophies.remove(position);
                adapter.notifyItemRemoved(position);
                trophy.setRedeemed(true);
                archivedTrophies.add(trophy);
                pointsEarned += trophy.getPoints();
                setPointsDisplay();
            }
        }
    }

    @Override
    public void onEntryClick(int position) {
        Intent intent = new Intent(this, TrophyActivity.class);
        intent.putExtra("Trophy", existingTrophies.get(position));
        intent.putExtra("position", position);
        startActivityForResult(intent, VIEW_ENTRY_REQUEST);
    }

    @Override
    public void onEditClick(int position) {
        Intent intent = new Intent(this, CreateToDoEntryActivity.class);
        intent.putExtra("Trophy", existingTrophies.get(position));
        intent.putExtra("position", position);
        startActivityForResult(intent, EDIT_ENTRY_REQUEST);
    }


   /* @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent result) {
        super.onActivityResult(requestCode, resultCode, result);
        if (requestCode == NEW) {
            if (resultCode == RESULT_OK) {
                Bundle extras = result.getExtras();
                Trophy newTemp = (Trophy) extras.getSerializable("Trophy");
                existingTrophy.add(newTemp);
            }
        }
        if (requestCode == VIEW) {
            if (resultCode == RESULT_OK) {
                Bundle extras = result.getExtras();
                int position = extras.getInt("position");
                Trophy trophy = existingTrophy.remove(position);
                adapter.notifyItemRemoved(position);
                trophy.setCompleted(true);
            }
        }
    }

    @Override
    public void onEntryClick(int position) {
        Intent intent = new Intent(this, ToDoEntryActivity.class);
        intent.putExtra("Trophy", existingTrophy.get(position));
        intent.putExtra("position", position);
        startActivityForResult(intent, VIEW);
    }

   @SuppressLint("IntentReset")
    private void sendSms(AlertDialog dialog,final String title) {               //Tries to send an sms
        String message = "Automated message from KidsToDoApp:" + "\n" + "I redeemed the" + title + "trophy!";
        boolean sent = true;
        try {
            SmsManager.getDefault().sendTextMessage(Utility.getPhoneNumber(), null, message, null, null);
        }
        catch(Exception e) {
            Toast.makeText(TrophyActivity.this,
                    "Unable to Send Message",
                    Toast.LENGTH_SHORT).show();
            sent = false;
        }
        if(sent) {
            Toast.makeText(TrophyActivity.this,
                    "Message Sent",
                    Toast.LENGTH_SHORT).show();
        }
        dialog.dismiss();
    }
    //Checks to see if the user has given SMS permissions
    private void smsPermissions(AlertDialog dialog, final String title) {       //If they have given permissions, tries to send an SMS
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {sendSms(dialog, title);}
        else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, MY_PERMISSIONS_REQUEST_SEND_SMS);
            if(ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {sendSms(dialog, title);}
            else {dialog.dismiss();}
        }
    }

    private void smsDialog(final String title) {                                //Dialog confirming that the user would like to send an SMS to their parent
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
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
                smsPermissions(dialog, title);   //Checks for permissions
            }
        });

        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }*/
}




