package com.example.kidstodoapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignInActivity extends Activity {

  private FirebaseAuth mAuth;

  private EditText emailInput;
  private EditText passwordInput;

  private ProgressBar progressBar;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_sign_in);

    mAuth = FirebaseAuth.getInstance();

    Button enterPassword = findViewById(R.id.enter_button_sign_in);
    emailInput = findViewById(R.id.email_sign_in);
    emailInput.setHint("name@example.com");
    passwordInput = findViewById(R.id.password_sign_in);
    passwordInput.setHint("Password");
    TextView createAccount = findViewById(R.id.create_account_textview);
    createAccount.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent intent = new Intent(view.getContext(), SignUpActivity.class);
        startActivity(intent);
        finish();
      }
    });

    enterPassword.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        logIn();
      }
    });

    progressBar = findViewById(R.id.progressbar_sign_in);
    progressBar.setVisibility(View.GONE);
  }

  private void logIn() {

    final String email = emailInput.getText().toString();
    final String password = passwordInput.getText().toString();

    if (email.isEmpty() || password.isEmpty()) {
      Toast.makeText(SignInActivity.this,
          "Please fill out all fields",
          Toast.LENGTH_SHORT).show();
    } else {
      progressBar.setVisibility(View.VISIBLE);
      mAuth.signInWithEmailAndPassword(email, password)
          .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
              if (task.isSuccessful()) {
                ParentModeUtility.getInstance().setPassword(password);
                Toast.makeText(SignInActivity.this,
                    "Login Successful",
                    Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), DeviceTypeActivity.class));
                finish();
              } else {
                if (task.getException() != null) {
                  Toast.makeText(SignInActivity.this,
                      task.getException().getMessage(),
                      Toast.LENGTH_SHORT).show();
                }
              }
              progressBar.setVisibility(View.GONE);
            }
          });
    }
  }

  @Override
  public void onBackPressed() {
  }
}
