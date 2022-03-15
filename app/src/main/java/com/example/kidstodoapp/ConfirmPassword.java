package com.example.kidstodoapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ConfirmPassword extends Activity {
    private Button enterPassword;
    private EditText passwordInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_password);

        enterPassword = findViewById(R.id.enter_button_confirm_password);
        passwordInput = findViewById(R.id.password_confirm);
        passwordInput.setHint("Password");

        enterPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Utility.isCorrectPassword(passwordInput.getText().toString())) {
                    Utility.setInParentMode(true);
                    Toast.makeText(ConfirmPassword.this,
                            "Welcome",
                            Toast.LENGTH_SHORT).show();
                    finish();
                }
                else {
                    Toast.makeText(ConfirmPassword.this,
                            "Incorrect Password, Please Try Again",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
