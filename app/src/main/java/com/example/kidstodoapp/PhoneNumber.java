package com.example.kidstodoapp;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class PhoneNumber extends Activity {
    private EditText phoneNumberInput;
    private Button enterPhoneNumber;

    private Handler parentModeTimeOut;
    private Runnable runnable;

    @Override
    public void onCreate(Bundle SavedInstanceState) {
        super.onCreate(SavedInstanceState);
        setContentView(R.layout.activity_phone_number);

        phoneNumberInput = findViewById(R.id.phoneNumberInput);
        enterPhoneNumber = findViewById(R.id.enterPhoneNumber);
        phoneNumberInput.setHint("111222233");

        if (Utility.isPhoneNumberSet()) {
            phoneNumberInput.setText(Utility.getPhoneNumber());
        }

        enterPhoneNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String numberString = phoneNumberInput.getText().toString();
                if (numberString.length() == 10 || numberString.length() == 11) {
                    if (containsOnlyDigits(numberString)) {
                        Utility.setPhoneNumber(numberString);
                        Utility.setPhoneNumberSet(true);
                        Toast.makeText(PhoneNumber.this,
                                "Phone Number Added",
                                Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(PhoneNumber.this,
                                "Not a Valid Phone Number, Please Try Again",
                                Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(PhoneNumber.this,
                            "Incorrect Length, Please Try Again",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        parentModeTimeOut = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                if (Utility.isInParentMode()) {
                    Utility.setInParentMode(false);
                    Toast.makeText(PhoneNumber.this,
                            "Logged Out Due to Inactivity",
                            Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        };
        Utility.startHandler(parentModeTimeOut, runnable);
    }

    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
        Utility.stopHandler(parentModeTimeOut, runnable);
        Utility.startHandler(parentModeTimeOut, runnable);
    }

    @Override
    public void onPause() {
        super.onPause();
        Utility.stopHandler(parentModeTimeOut, runnable);
    }

    @Override
    public void onResume() {
        super.onResume();
        Utility.startHandler(parentModeTimeOut, runnable);
    }

    boolean containsOnlyDigits(String str) {
        boolean result = true;
        for (int i = 0; i < str.length(); i++) {
            result = result && "0123456789".contains(str.charAt(i) + "");
        }
        return result;
    }
}