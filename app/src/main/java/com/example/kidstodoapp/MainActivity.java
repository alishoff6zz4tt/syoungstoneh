package com.example.kidstodoapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialogFragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

// This comment was made by Sean Youngstone
public class MainActivity extends AppCompatActivity {
    private String password = "1234Qwerty";     //Temporary
    public Boolean notFirstTime = null;         //Used to determine if the parent needs to set up a password
    public Boolean inParentMode = false;        //Used to determine if the parent is in parent mode or not
    private Button parentMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        parentMode = (Button) findViewById(R.id.pM);
        parentMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!inParentMode) {openDialog(password, notFirstTime);}
                else {inParentMode = false;}
            }
        }
        );
    }

    public void openDialog(String p, Boolean nft)
    {
        /*if(nft==null)
        {


        }*/

        parentModeDialog parentMode = new parentModeDialog();
        parentMode.show(getSupportFragmentManager(), "Enter Password");
    }

/*    public static class parentModeDialog extends AppCompatDialogFragment {
        MainActivity ma = new MainActivity();

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Enter Password");
//Can't have both static class and MainActivity.this (context), but need both
            final EditText input = new EditText(MainActivity.this);
            input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            builder.setView(input);

            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int i) {dialog.cancel();}
            });
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int i) {
                    if(ma.getPassword()==input.getText().toString())
                    {
                        ma.setInParentMode(true);
                        dialog.cancel();
                    }
                    else {}
                }
            });

            return builder.create();
        }
    }*/

    public String getPassword() {return password;}
    public void setPassword(String password) {this.password = password;}
    public Boolean getNotFirstTime() {return notFirstTime;}
    public void setNotFirstTime(Boolean notFirstTime) {this.notFirstTime = notFirstTime;}
    public Boolean getInParentMode() {return inParentMode;}
    public void setInParentMode(Boolean inParentMode) {this.inParentMode = inParentMode;}
}