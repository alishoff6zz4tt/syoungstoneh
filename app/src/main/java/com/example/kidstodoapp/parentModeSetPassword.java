package com.example.kidstodoapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialogFragment;

public class parentModeSetPassword extends AppCompatDialogFragment{
    private EditText input1;
    private EditText input2;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Create Password");
        input1 = new EditText(this.getContext());
        input1.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        input1.setHint("Password");
        input2 = new EditText(this.getContext());
        input2.setHint("Matching Password");
        input2.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

        LinearLayout linLay  = new LinearLayout(this.getContext());
        linLay.setOrientation(LinearLayout.VERTICAL);
        linLay.addView(input1);
        linLay.addView(input2);
        builder.setView(linLay);

        builder.setPositiveButton("Enter", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                //Overridden in onResume
            }
        });

        return builder.create();
    }

    @Override
    public void onResume() {
        super.onResume();
        final AlertDialog ad = (AlertDialog)getDialog();
        if(ad!=null)
        {
            Button positiveButton = ad.getButton(Dialog.BUTTON_POSITIVE);
            positiveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(input1.getText().toString().isEmpty() || input2.getText().toString().isEmpty()) {
                        Toast.makeText(getContext(),
                                "Empty Field(s)",
                                Toast.LENGTH_SHORT).show();
                    }
                    else if(input1.getText().toString().equals(input2.getText().toString()))
                    {
                        ((MainActivity) getContext()).setPassword(input1.getText().toString());
                        ((MainActivity) getContext()).setNotFirstTime(true);
                        ((MainActivity) getContext()).setInParentMode(true, 1);

                        Toast.makeText(getContext(),
                                "Password Created",
                                Toast.LENGTH_SHORT).show();
                        ad.dismiss();
                    }
                    else if(!input1.getText().toString().equals(input2.getText().toString()))
                    {
                        Toast.makeText(getContext(),
                                "Passwords Do Not Match",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}