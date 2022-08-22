package com.example.kidstodoapp;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

public class SettingsFragment extends Fragment implements View.OnClickListener {
    private EditText phoneNumberInput;
    private ConstraintLayout themeView;
    private ConstraintLayout phoneNumberView;

    private DataModel model;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        final Switch darkModeSwitch = view.findViewById(R.id.dark_mode_switch);

        int inDarkMode = getActivity().getResources().getConfiguration().uiMode &
                Configuration.UI_MODE_NIGHT_MASK;
        switch(inDarkMode) {
            case Configuration.UI_MODE_NIGHT_YES:
                darkModeSwitch.setChecked(true);
                break;
            case Configuration.UI_MODE_NIGHT_NO:
            case Configuration.UI_MODE_NIGHT_UNDEFINED:
                darkModeSwitch.setChecked(false);
                break;
        }

        phoneNumberInput = view.findViewById(R.id.phoneNumberInput);
        Button enterPhoneNumber = view.findViewById(R.id.enterPhoneNumber);

        themeView = view.findViewById(R.id.themeView);
        themeView.setVisibility(View.GONE);
        phoneNumberView = view.findViewById(R.id.phoneNumberView);
        phoneNumberView.setVisibility(View.GONE);

        model = DataModel.getInstance();

        phoneNumberInput.setText(model.getPhoneNumber());

        darkModeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isChecked()) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    NightMode.updatePreference(true);
                }
                else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    NightMode.updatePreference(false);
                }
                getActivity().getSupportFragmentManager().beginTransaction().remove(SettingsFragment.this).commit();
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        enterPhoneNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String numberString = phoneNumberInput.getText().toString().replaceAll("\\D+","");
                if (numberString.length() == 10 || numberString.length() == 11) {
                    model.updatePhoneNumber(numberString);
                    Toast.makeText(SettingsFragment.this.getContext(),
                            "Phone Number Updated",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SettingsFragment.this.getContext(),
                            "Phone number must contain 10 or 11 digits",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }

    @Override
    public void onClick(View view){
        toggleVisibility(view);
    }

    public void toggleVisibility(View view) {
        switch (view.getId()) {
            case R.id.themes:
                if (themeView.isShown()) {
                    themeView.setVisibility(View.GONE);
//                    AnimationsUtility.toggleClose(getContext(), themeView);
                }
                else {
                    themeView.setVisibility(View.VISIBLE);
                    AnimationsUtility.toggleOpen(getContext(), themeView);
                }
                break;
            case R.id.phoneNumber:
                if (phoneNumberView.isShown()) {
                    phoneNumberView.setVisibility(View.GONE);
//                    AnimationsUtility.toggleClose(getContext(), phoneNumberView);
                    }
                else {
                    if(ParentModeUtility.getInstance().isInParentMode()) {
                        phoneNumberView.setVisibility(View.VISIBLE);
                        AnimationsUtility.toggleOpen(getContext(), phoneNumberView);
                    }
                    else {
                        Toast.makeText(SettingsFragment.this.getContext(),
                                "Please enter Parent Mode to edit phone number",
                                Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }
}
