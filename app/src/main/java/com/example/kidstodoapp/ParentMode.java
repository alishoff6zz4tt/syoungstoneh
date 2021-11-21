package com.example.kidstodoapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ParentMode extends Activity {
    private Button enterPassword;
    private EditText passwordInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_mode);

        enterPassword = findViewById(R.id.enterPassword);
        passwordInput = findViewById(R.id.passwordInput);
        passwordInput.setHint("Password");

        enterPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(passwordInput.getText().toString().equals(Utility.getPassword())) {  //If the input equals the password that has been set
                    Utility.setInParentMode(true);                                      //Set it in parent mode
                    Toast.makeText(ParentMode.this,
                            "Login Successful",
                            Toast.LENGTH_SHORT).show();
                    finish();
                }
                else {                                                                  //Otherwise show the user a toast
                    Toast.makeText(ParentMode.this,
                            "Login Unsuccessful, Please Try Again",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
