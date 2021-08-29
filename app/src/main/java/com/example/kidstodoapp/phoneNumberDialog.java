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

public class phoneNumberDialog extends AppCompatDialogFragment {
    private EditText input;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Enter Phone Number");

        input = new EditText(this.getContext());
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_PHONE);
        input.setHint("1231234123");
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
                    String numberString = input.getText().toString();
                    if(numberString.length() == 10 || numberString.length() == 11)
                    {
                        if (containsOnlyDigits(numberString)) {
                            ((MainActivity) getContext()).setPhoneNumber(numberString);
                            Toast.makeText(getContext(),
                                    "Phone Number Added",
                                    Toast.LENGTH_SHORT).show();
                            ad.dismiss();
                        }
                        else {
                            Toast.makeText(getContext(),
                                    "Not a Valid Phone Number, Please Try Again",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        Toast.makeText(getContext(),
                                "Incorrect Length, Please Try Again",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    boolean containsOnlyDigits(String str) {
        boolean result = true;
        for (int i = 0 ; i < str.length() ; i++) {
            result = result && "0123456789".contains(str.charAt(i) + "");
        }
        return result;
    }
}
