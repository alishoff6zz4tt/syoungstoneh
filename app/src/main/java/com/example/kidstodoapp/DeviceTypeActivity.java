package com.example.kidstodoapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

public class DeviceTypeActivity extends Activity {

    private Boolean parentDevice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_type);

        Button saveButton = findViewById(R.id.save_user_button);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (parentDevice != null) {
                    if (parentDevice) {
                        ParentModeUtility.getInstance().setParentDevice();
                    } else {
                        ParentModeUtility.getInstance().initializeTimeout();
                    }
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finish();
                } else {
                    Toast.makeText(DeviceTypeActivity.this,
                            "Please make a selection",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        parentDevice = view.getId() == R.id.radio_parent && checked;
    }

    @Override
    public void onBackPressed() {}
}