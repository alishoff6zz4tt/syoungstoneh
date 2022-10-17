package com.example.kidstodoapp;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class ToDoEntryFragment extends Fragment {

    private DataModel model;

    private int position;
    private boolean completed;
    private ToDoEntry mToDoEntry;

    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_to_do_entry, container, false);

        model = DataModel.getInstance();

        Bundle bundle = getArguments();

        position = (bundle != null ? bundle.getInt("position") : 0);
        completed = (bundle != null && bundle.getBoolean("completed"));

        if (completed) {
            mToDoEntry = model.getCompletedEntries().get(position);
        } else {
            mToDoEntry = model.getToDoEntries().get(position);
        }

        TextView categoryTextView = view.findViewById(R.id.category_textview);
        final TextView nameTextView = view.findViewById(R.id.entry_name_textview);
        TextView descriptionTextView = view.findViewById(R.id.entry_description_textview);
        TextView dateTimeTextView = view.findViewById(R.id.entry_datetime_textview);
        TextView pointsTextView = view.findViewById(R.id.entry_points_textview);
        final CheckBox completionCheckBox = view.findViewById(R.id.completion_check_box);
        ImageButton sms = view.findViewById(R.id.sms);
        Button confirmCompletedButton = view.findViewById(R.id.confirm_completed_button);

        categoryTextView.setText(mToDoEntry.getCategory());
        nameTextView.setText(mToDoEntry.getEntryName());
        descriptionTextView.setText(mToDoEntry.getDescription());
        dateTimeTextView.setText(mToDoEntry.getDateTimeString());
        String points = "$" + mToDoEntry.getPointValue();
        pointsTextView.setText(points);
        completionCheckBox.setChecked(completed);

        if (completed) {
            sms.setVisibility(View.GONE);
            confirmCompletedButton.setVisibility(View.VISIBLE);
        }
        else if (ParentModeUtility.getInstance().isInParentMode()) {
            sms.setVisibility(View.GONE);
        }

        completionCheckBox.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (!completed && completionCheckBox.isChecked()) {
                    model.completeToDoEntry(position);
                    if (checkSmsPermissions()) {
                        sendSmsCompleted();
                    }
                }
                else if (completed && !completionCheckBox.isChecked()) {
                    model.uncompleteToDoEntry(position);
                }
                exit();
            }
        });

        sms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {                                    //For sending messages to the parent asking for help
                if(!model.getPhoneNumber().isEmpty()) {smsDialog();}
                else {                                                          //If a phone number hasn't been set
                    Toast.makeText(ToDoEntryFragment.this.getContext(),
                            "A Phone Number Has Not Been Added",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        confirmCompletedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                model.confirmCompleted(position);
                exit();
            }
        });

        return view;
    }

    private void sendSmsNeedHelp() {
        if (checkSmsPermissions()) {
            String message = "Automated message from BackPack app:" + "\n" +
                    "I need help with " + mToDoEntry.getEntryName() + "!";
            boolean sent = true;
            try {
                SmsManager.getDefault().sendTextMessage(
                        model.getPhoneNumber(), null, message, null, null);
            } catch (Exception e) {
                Toast.makeText(ToDoEntryFragment.this.getContext(),
                        "Unable to Send Message",
                        Toast.LENGTH_SHORT).show();
                sent = false;
            }
            if (sent) {
                Toast.makeText(ToDoEntryFragment.this.getContext(),
                        "Message Sent",
                        Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(ToDoEntryFragment.this.getContext(),
                    "Can't send message without permissions",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void sendSmsCompleted() {
        if (checkSmsPermissions() && !ParentModeUtility.getInstance().isInParentMode()) {
            String message = "Automated message from BackPack app:" + "\n" +
                    "Your child just completed \"" + mToDoEntry.getEntryName() + "\".\n" +
                    "Go to BackPack app to confirm task completion.";
            try {
                SmsManager.getDefault().sendTextMessage(
                        model.getPhoneNumber(), null, message, null, null);
            } catch (Exception e) {
                Toast.makeText(ToDoEntryFragment.this.getContext(),
                        "Text error, parent not notified",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean checkSmsPermissions() {
        if (ActivityCompat.checkSelfPermission(
                ToDoEntryFragment.this.requireContext(),
                Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        else {
            ActivityCompat.requestPermissions(
                    requireActivity(),
                    new String[]{Manifest.permission.SEND_SMS}, MY_PERMISSIONS_REQUEST_SEND_SMS);
            return ActivityCompat.checkSelfPermission(
                    ToDoEntryFragment.this.requireContext(),
                    Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED;
        }
    }

    private void smsDialog() {                                //Dialog confirming that the user would like to send an SMS to their parent
        AlertDialog.Builder builder =
                new AlertDialog.Builder(ToDoEntryFragment.this.requireContext());
        builder.setTitle("Send SMS");
        builder.setMessage("Would you like to send an SMS to your parent asking for help?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //Later Overridden
                //If there were cases where the dialog should NOT close onClick, then this same tactic would be used
                //But in this case, doing this mainly to change it from an AlertDialog.Builder to an AlertDialog
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //Later Overridden
            }
        });

        final AlertDialog dialog = builder.create();
        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendSmsNeedHelp();
                dialog.dismiss();
            }
        });

        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    private void exit() {
        requireActivity().getSupportFragmentManager().beginTransaction().remove(ToDoEntryFragment.this).commit();
        requireActivity().getSupportFragmentManager().popBackStack();
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) requireActivity()).setCheckedItem(R.id.ToDoListFragment);
    }
}