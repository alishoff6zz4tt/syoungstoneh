package com.example.kidstodoapp;

import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;


public class CreateTrophyFragment extends Fragment {

    private long imageLocation;

    private DataModel model;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_create_trophy, container, false);

        model = DataModel.getInstance();

        Button createTrophyButton = view.findViewById(R.id.create_trophy_button);
        Button cancelTrophyButton = view.findViewById(R.id.cancel_trophy_button);

        final ImageButton icon1 = view.findViewById(R.id.icon1);
        final ImageButton icon2 = view.findViewById(R.id.icon2);
        final ImageButton icon3 = view.findViewById(R.id.icon3);

        final ImageView border1 = view.findViewById(R.id.border1);
        final ImageView border2 = view.findViewById(R.id.border2);
        final ImageView border3 = view.findViewById(R.id.border3);

        border1.setVisibility(View.INVISIBLE);
        border2.setVisibility(View.INVISIBLE);
        border3.setVisibility(View.INVISIBLE);

        final EditText trophyNameEditText = view.findViewById(R.id.trophy_name_txt);
        final EditText trophyPointsEditText = view.findViewById(R.id.trophy_points_txt);

        imageLocation = R.drawable.trophy1;

        icon1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                border1.setVisibility(View.INVISIBLE);
                border2.setVisibility(View.VISIBLE);
                border3.setVisibility(View.INVISIBLE);
                imageLocation = R.drawable.trophy1;
            }
        });

        icon2.setOnTouchListener(new ButtonHighlight(icon2));
        icon2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                border1.setVisibility(View.VISIBLE);
                border2.setVisibility(View.INVISIBLE);
                border3.setVisibility(View.INVISIBLE);
                imageLocation = R.drawable.trophy2;
            }
        });

        icon3.setOnTouchListener(new ButtonHighlight(icon3));
        icon3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                border1.setVisibility(View.INVISIBLE);
                border2.setVisibility(View.INVISIBLE);
                border3.setVisibility(View.VISIBLE);
                imageLocation = R.drawable.trophy3;
            }
        });

        createTrophyButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (TextUtils.isEmpty(trophyNameEditText.getText().toString())) {
                        Toast.makeText(CreateTrophyFragment.this.getContext(),
                                "Please give this trophy a name.",
                                Toast.LENGTH_LONG).show();
                } else if (TextUtils.isEmpty(trophyPointsEditText.getText().toString())) {
                    Toast.makeText(CreateTrophyFragment.this.getContext(),
                            "Please add a point value to this trophy.",
                            Toast.LENGTH_LONG).show();
                }
                else {
                    int points;
                    if (TextUtils.isEmpty(trophyPointsEditText.getText().toString())) {
                        points = 0;
                    }
                    else {
                        points = Integer.parseInt(trophyPointsEditText.getText().toString());
                    }
                    Trophy newTrophy = new Trophy(
                            trophyNameEditText.getText().toString(),
                            points,
                            imageLocation
                    );
                    model.addTrophy(newTrophy);
                    exit();
                }
            }
        });

        cancelTrophyButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                exit();
            }
        });

        return view;
    }

    private void exit() {
        requireActivity().getSupportFragmentManager().beginTransaction().remove(CreateTrophyFragment.this).commit();
        requireActivity().getSupportFragmentManager().popBackStack();
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) requireActivity()).setCheckedItem(R.id.TrophyCase);
    }

}