/*TO DO:
Make it so the dialogs do not close if there is something wrong.
 */

package com.example.kidstodoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

// This comment was made by Sean Youngstone
public class MainActivity extends AppCompatActivity {
    private String password;
    private int phoneNumber;
    private Boolean notFirstTime = null;         //Used to determine if the parent needs to set up a password
    private Boolean inParentMode = false;        //Used to determine if the parent is in parent mode or not
    private Button parentMode;
    private ImageButton setPhoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        parentMode = findViewById(R.id.pM);
        setPhoneNumber = findViewById(R.id.phone);

        parentMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!inParentMode) {openDialog(notFirstTime);}
                else
                    {
                        inParentMode = false;
                        Toast.makeText(MainActivity.this,
                                "Logged Out",
                                Toast.LENGTH_SHORT).show();
                    }
            }
        }
        );

        setPhoneNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(inParentMode)
                {
                    phoneNumberDialog phoneDialog = new phoneNumberDialog();
                    phoneDialog.show(getSupportFragmentManager(), "Set Phone Number");
                }
                else {
                    Toast.makeText(MainActivity.this,
                            "Please Enter Parent Mode to Add a Phone Number",
                            Toast.LENGTH_SHORT).show();
                }
            }
        }
        );
    }

    public void openDialog(Boolean notFirstTime)
    {
        if(notFirstTime==null)
        {
            parentModeSetPassword parentSetPass = new parentModeSetPassword();
            parentSetPass.show(getSupportFragmentManager(), "Create Password");
        }
        else
        {
            parentModeDialog parentMode = new parentModeDialog();
            parentMode.show(getSupportFragmentManager(), "Enter Password");
        }
    }

    public String getPassword() {return password;}
    public void setPassword(String password) {this.password = password;}
    public Boolean getNotFirstTime() {return notFirstTime;}
    public void setNotFirstTime(Boolean notFirstTime) {this.notFirstTime = notFirstTime;}
    public Boolean getInParentMode() {return inParentMode;}
    public void setInParentMode(Boolean inParentMode) {this.inParentMode = inParentMode;}
    public int getPhoneNumber() {return phoneNumber;}
    public void setPhoneNumber(int phoneNumber) {this.phoneNumber = phoneNumber;}
}