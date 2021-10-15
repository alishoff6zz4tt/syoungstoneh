/*TO DO:
Clean it up, make it so you can only add a phone number if it is the correct length(s)
 */
package com.example.kidstodoapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatDialogFragment;

public class phoneNumberDialog extends AppCompatDialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Enter Phone Number");

        final EditText input = new EditText(this.getContext());
        input.setHint("0000000000");
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        builder.setView(input);

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {dialog.cancel();}
        });
        builder.setPositiveButton("Enter", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                String numberString = input.getText().toString();
                if(numberString.length() == 10 || numberString.length() == 11)
                {
                    if (containsOnlyDigits(numberString)) {
                        ((MainActivity) getContext()).setPhoneNumber(numberString);
                        Toast.makeText(getContext(),
                                "Phone Number Added",
                                Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
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

        return builder.create();
    }

    boolean containsOnlyDigits(String str) {
        boolean result = true;
        for (int i = 0 ; i < str.length() ; i++) {
            result = result && "0123456789".contains(str.charAt(i) + "");
        }
        return result;
    }
}
