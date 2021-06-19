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
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_PHONE);
        builder.setView(input);

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {dialog.cancel();}
        });
        builder.setPositiveButton("Enter", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                if(input.getText().toString().length() == 10 || input.getText().toString().length() == 11)
                {
                    ((MainActivity) getContext()).setPhoneNumber(Integer.parseInt(input.getText().toString()));
                    Toast.makeText(getContext(),
                            "Phone Number Added",
                            Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
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
}
