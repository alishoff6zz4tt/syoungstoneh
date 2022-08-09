package com.example.kidstodoapp;

import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class TrophyFragment extends Fragment {

    private DataModel model;
    private Trophy mTrophy;
    private int position;
    private Button cancelButton;
    private Button buyButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_trophy, container, false);

        model = DataModel.getInstance();

        Bundle bundle = getArguments();

        position = (bundle != null ? bundle.getInt("position") : 0);
        mTrophy = model.getExistingTrophies().get(position);

        final TextView nameTextView = view.findViewById(R.id.trophy_name_textview);
        TextView pointsTextView = view.findViewById(R.id.trophy_points_textview);
        ImageView iconImageView = view.findViewById(R.id.icon_view);

        buyButton = view.findViewById(R.id.buy_button);
        buyButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if(model.getPointsEarned() - mTrophy.getPointValue() < 0) {
                    Toast toast = Toast.makeText(TrophyFragment.this.getContext(),
                            "You don't have enough money to buy this trophy.\nSorry!",
                            Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else {
                    model.redeemTrophy(position);
                }
                exit();

            }
        });

        cancelButton = view.findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                exit();
            }
        });

        nameTextView.setText(mTrophy.getName());
        iconImageView.setImageResource(((Long)mTrophy.getImageLocation()).intValue());
        String points = "for " + mTrophy.getPointValue() + " points";
        pointsTextView.setText(points);

        return view;
    }

    private void exit() {
        getActivity().getSupportFragmentManager().beginTransaction().remove(TrophyFragment.this).commit();
        getActivity().getSupportFragmentManager().popBackStack();
    }
}