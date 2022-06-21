package com.example.kidstodoapp;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class PhoneNumber extends Activity {
    private EditText phoneNumberInput;
    private Button enterPhoneNumber;

    private Handler parentModeTimeOut;
    private Runnable runnable;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    public void onCreate(Bundle SavedInstanceState) {
        super.onCreate(SavedInstanceState);
        setContentView(R.layout.activity_phone_number);

        phoneNumberInput = findViewById(R.id.phoneNumberInput);
        enterPhoneNumber = findViewById(R.id.enterPhoneNumber);
        phoneNumberInput.setHint("111-222-3333");

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        if (ParentModeUtility.isPhoneNumberSet()) {
            phoneNumberInput.setText(ParentModeUtility.getPhoneNumber());
        }

        enterPhoneNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String numberString = phoneNumberInput.getText().toString().replaceAll("\\D+","");
                if (numberString.length() == 10 || numberString.length() == 11) {
                    String uid = mAuth.getCurrentUser().getUid();
                    DocumentReference documentReference = db.collection("users").document(uid);
                    documentReference.update("phoneNumber", numberString);
                    Toast.makeText(PhoneNumber.this,
                            "Phone Number Updated",
                            Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(PhoneNumber.this,
                            "Phone number must contain 10 or 11 digits",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        parentModeTimeOut = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {                                                         //This runnable is here so that if the user is timed out while in the class
                if (ParentModeUtility.isInParentMode()  && !ParentModeUtility.isParentDevice()) {                                         //It will return to MainActivity
                    Toast.makeText(PhoneNumber.this,
                            "Logged Out Due to Inactivity",
                            Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        };
        ParentModeUtility.startHandler(parentModeTimeOut, runnable);
    }

    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
        ParentModeUtility.stopHandler(parentModeTimeOut, runnable);
        ParentModeUtility.startHandler(parentModeTimeOut, runnable);
    }

    @Override
    public void onPause() {
        super.onPause();
        ParentModeUtility.stopHandler(parentModeTimeOut, runnable);
    }

    @Override
    public void onResume() {
        super.onResume();
        ParentModeUtility.startHandler(parentModeTimeOut, runnable);
    }
}