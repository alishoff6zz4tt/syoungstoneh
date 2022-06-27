package com.example.kidstodoapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

public class PhoneNumber extends Fragment {
    private EditText phoneNumberInput;

    private DataModel model;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_phone_number, container, false);

        phoneNumberInput = view.findViewById(R.id.phoneNumberInput);
        Button enterPhoneNumber = view.findViewById(R.id.enterPhoneNumber);

        model = DataModel.getInstance();

        phoneNumberInput.setText(model.getPhoneNumber());

        enterPhoneNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String numberString = phoneNumberInput.getText().toString().replaceAll("\\D+","");
                if (numberString.length() == 10 || numberString.length() == 11) {
                    model.updatePhoneNumber(numberString);
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