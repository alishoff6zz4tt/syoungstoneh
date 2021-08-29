package com.example.kidstodoapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialogFragment;


public class parentModeDialog extends AppCompatDialogFragment {
    private EditText input;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Enter Password");

        input = new EditText(this.getContext());
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        input.setHint("Password");
        builder.setView(input);

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                //Overridden in onResume
            }
        });
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
        if(ad!=null) {
            Button negativeButton = ad.getButton(Dialog.BUTTON_NEGATIVE);
            negativeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ad.cancel();
                }
            });
            Button positiveButton = ad.getButton(Dialog.BUTTON_POSITIVE);
            positiveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(input.getText().toString().equals(((MainActivity) getContext()).getPassword()))
                    {
                        ((MainActivity) getContext()).setInParentMode(true, 1);
                        Toast.makeText(getContext(),
                                "Login Successful",
                                Toast.LENGTH_SHORT).show();
                        ad.dismiss();
                    }
                    else
                    {
                        Toast.makeText(getContext(),
                                "Login Unsuccessful, Please Try Again",
                                Toast.LENGTH_SHORT).show();
                    }
                }
        });
        }
    }
}