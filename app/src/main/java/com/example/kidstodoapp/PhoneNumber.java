package com.example.kidstodoapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class PhoneNumber extends Fragment {
    private EditText phoneNumberInput;
    private Button enterPhoneNumber;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MainActivity)getActivity()).tabVisibility(false);
        View view = inflater.inflate(R.layout.fragment_phone_number, container, false);

        phoneNumberInput = view.findViewById(R.id.phoneNumberInput);
        enterPhoneNumber = view.findViewById(R.id.enterPhoneNumber);
        phoneNumberInput.setHint("111-222-3333");

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        if (Utility.isPhoneNumberSet()) {
            phoneNumberInput.setText(Utility.getPhoneNumber());
        }

        enterPhoneNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String numberString = phoneNumberInput.getText().toString().replaceAll("\\D+","");
                if (numberString.length() == 10 || numberString.length() == 11) {
                    String uid = mAuth.getCurrentUser().getUid();
                    DocumentReference documentReference = db.collection("users").document(uid);
                    documentReference.update("phoneNumber", numberString);
                    Toast.makeText(PhoneNumber.this.getContext(),
                            "Phone Number Updated",
                            Toast.LENGTH_SHORT).show();
                    getActivity().getSupportFragmentManager().beginTransaction().remove(PhoneNumber.this).commit();
                    getActivity().getSupportFragmentManager().popBackStack();
                } else {
                    Toast.makeText(PhoneNumber.this.getContext(),
                            "Phone number must contain 10 or 11 digits",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }
}