package com.example.kidstodoapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatDialogFragment;

public class parentModeSetPassword extends AppCompatDialogFragment{
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Create Password");
        final EditText input1 = new EditText(this.getContext());
        input1.setHint("Password");
        input1.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        final EditText input2 = new EditText(this.getContext());
        input2.setHint("Confirm Password");
        input2.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

        LinearLayout linLay  = new LinearLayout(this.getContext());
        linLay.setOrientation(LinearLayout.VERTICAL);
        linLay.addView(input1);
        linLay.addView(input2);
        builder.setView(linLay);

        builder.setPositiveButton("Enter", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                if(input1.getText().toString().isEmpty() || input2.getText().toString().isEmpty()) {
                    Toast.makeText(getContext(),
                            "Empty Field(s)",
                            Toast.LENGTH_SHORT).show();
                }
                else if(input1.getText().toString().equals(input2.getText().toString()))
                {
                    ((MainActivity) getContext()).setPassword(input1.getText().toString());
                    ((MainActivity) getContext()).setNotFirstTime(true);
                    ((MainActivity) getContext()).setInParentMode(true);

                    Toast.makeText(getContext(),
                            "Password Created",
                            Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
                else if(input1.getText().toString()!=input2.getText().toString())
                {
                    Toast.makeText(getContext(),
                            "Passwords Do Not Match",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        return builder.create();
    }
}