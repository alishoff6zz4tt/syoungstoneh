package com.example.kidstodoapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ParentModeFirstTime extends Activity {
    private Button enterPassword;
    private EditText passwordInput1, passwordInput2;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_mode_first_time);

        enterPassword = findViewById(R.id.enterPassword);
        passwordInput1 = findViewById(R.id.passwordInput1);
        passwordInput1.setHint("Password");
        passwordInput2 = findViewById(R.id.passwordInput2);
        passwordInput2.setHint("Matching Password");

        enterPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(passwordInput1.getText().toString().isEmpty() || passwordInput2.getText().toString().isEmpty()){
                    Toast.makeText(ParentModeFirstTime.this,
                            "Empty Field(s)",
                            Toast.LENGTH_SHORT).show();
                }
                else if(passwordInput1.getText().toString().equals(passwordInput2.getText().toString())) {
                    Utility.setPassword(passwordInput1.getText().toString());
                    Utility.setInParentMode(true);
                    Toast.makeText(ParentModeFirstTime.this,
                            "Login Successful",
                            Toast.LENGTH_SHORT).show();
                    Utility.setNotFirstTime(true);
                    finish();
                }
                else {
                    Toast.makeText(ParentModeFirstTime.this,
                            "Password Do Not Match",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
