package com.example.kidstodoapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends Activity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    private String uid;

    private Button enterPassword;
    private EditText emailRegistrationInput, passwordInput1, passwordInput2, phoneInput;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        enterPassword = findViewById(R.id.enter_button_sign_up);
        passwordInput1 = findViewById(R.id.password_sign_up_1);
        passwordInput1.setHint("Password");
        passwordInput2 = findViewById(R.id.password_sign_up_2);
        passwordInput2.setHint("Matching Password");
        emailRegistrationInput = findViewById(R.id.email_sign_up);
        emailRegistrationInput.setHint("name@example.com");
        phoneInput = findViewById(R.id.phone_sign_up);
        phoneInput.setHint("111-222-3333");

        progressBar = findViewById(R.id.progressbar_sign_up);
        progressBar.setVisibility(View.GONE);

        enterPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });
    }

    private void registerUser() {
        final String email = emailRegistrationInput.getText().toString();
        final String password = passwordInput1.getText().toString();
        final String passwordConfirm = passwordInput2.getText().toString();
        final String phone = phoneInput.getText().toString().replaceAll("\\D+","");

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailRegistrationInput.setError("Invalid email");
            emailRegistrationInput.requestFocus();
        }
        else if (phone.length() > 11 || phone.length() < 10) {
            phoneInput.setError("Phone number must contain 10 or 11 digits");
            phoneInput.requestFocus();
        }
        else if (password.length() < 8) {
            passwordInput1.setError("Password must be at least 8 characters long");
            passwordInput1.requestFocus();
        }
        else if (!password.equals(passwordConfirm)) {
            passwordInput1.setError("Passwords do not match");
            passwordInput1.requestFocus();
        }
        else {
            progressBar.setVisibility(View.VISIBLE);
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        ParentModeUtility.setPassword(password);
                        createDbEntry(phone);
                        Toast.makeText(SignUpActivity.this,
                                "Registration Successful",
                                Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), DeviceTypeActivity.class));
                        finish();
                    } else {
                        if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                            Toast.makeText(SignUpActivity.this,
                                    "Account already associated with this email address",
                                    Toast.LENGTH_SHORT).show();
                        } else if (task.getException() != null) {
                            Toast.makeText(SignUpActivity.this,
                                    task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                    progressBar.setVisibility(View.GONE);
                }
            });

        }
    }

    private void createDbEntry(String phone) {
        uid = mAuth.getCurrentUser().getUid();
        DocumentReference documentReference = db.collection("users").document(uid);
        Map<String,Object> user = new HashMap<>();
        user.put("phoneNumber", phone);
        user.put("toDoEntries", new ArrayList<ToDoEntry>());
        user.put("completedEntries", new ArrayList<ToDoEntry>());
        user.put("pointsEarned", 0);
        user.put("sessionIdentifierLastChanged", 0);
        documentReference.set(user);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, SignInActivity.class);
        startActivity(intent);
    }
}
