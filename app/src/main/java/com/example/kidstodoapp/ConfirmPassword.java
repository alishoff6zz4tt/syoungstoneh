package com.example.kidstodoapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

public class ConfirmPassword extends Fragment {
    private Button enterPassword;
    private EditText passwordInput;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_confirm_password, container, false);

        enterPassword = view.findViewById(R.id.enter_button_confirm_password);
        passwordInput = view.findViewById(R.id.password_confirm);
        passwordInput.setHint("Password");

        enterPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Utility.isCorrectPassword(passwordInput.getText().toString())) {
                    FragmentViewModel.setInParentMode(true);
                    Toast.makeText(ConfirmPassword.this.getContext(),
                            "Welcome",
                            Toast.LENGTH_SHORT).show();
                    getActivity().getSupportFragmentManager().beginTransaction().remove(ConfirmPassword.this).commit();
                    getActivity().getSupportFragmentManager().popBackStack();
                }
                else {
                    Toast.makeText(ConfirmPassword.this.getContext(),
                            "Incorrect Password, Please Try Again",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }
}
