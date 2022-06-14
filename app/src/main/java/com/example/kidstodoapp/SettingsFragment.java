package com.example.kidstodoapp;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

public class SettingsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        final Button darkMode = view.findViewById(R.id.darkMode);

        int inDarkMode = getActivity().getResources().getConfiguration().uiMode &
                Configuration.UI_MODE_NIGHT_MASK;
        switch(inDarkMode) {
            case Configuration.UI_MODE_NIGHT_YES:
                darkMode.setText("ON");
                break;
            case Configuration.UI_MODE_NIGHT_NO:
            case Configuration.UI_MODE_NIGHT_UNDEFINED:
                darkMode.setText("OFF");
                break;
        }

        darkMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(darkMode.getText().equals("OFF")) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    darkMode.setText("ON");
                }
                else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    darkMode.setText("OFF");
                }
                getActivity().getSupportFragmentManager().beginTransaction().remove(SettingsFragment.this).commit();
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
        return view;
    }
}
