/*TO DO:
I think that I am always being met with the first time dialog because of the initialized variables "resetting" themselves
However, if I change to accommodate this I get a null pointer exception.
Need to find a way to get it to work correctly without initializing the variables.

Make it so the dialogs do not close if there is something wrong.
 */

package com.example.kidstodoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

// This comment was made by Sean Youngstone
public class MainActivity extends AppCompatActivity {
    private String password;
    private Boolean notFirstTime = null;         //Used to determine if the parent needs to set up a password
    private Boolean inParentMode = false;        //Used to determine if the parent is in parent mode or not
    private Button parentMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        parentMode = findViewById(R.id.pM);
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
    }

    public void openDialog(Boolean nft)
    {
        if(nft==null)
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
}