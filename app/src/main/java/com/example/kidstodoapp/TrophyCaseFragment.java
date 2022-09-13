package com.example.kidstodoapp;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.Locale;
import java.util.Observable;

public class TrophyCaseFragment extends Fragment implements java.util.Observer, TrophyAdapter.OnEntryListener  {

    private DataModel model;
    private ParentModeUtility parentModeUtility;

    private TrophyAdapter adapter;
    private RecyclerView recyclerView;

    private TextView pointsDisplay;
    private Button createNewTrophy;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_trophy_case, container, false);

        model = DataModel.getInstance();
        model.addObserver(this);

        parentModeUtility = ParentModeUtility.getInstance();
        parentModeUtility.addObserver(this);

        //Points Display
        pointsDisplay = view.findViewById(R.id.points_display);
        setPointsDisplay();

        //Recycler View Setup
        adapter = new TrophyAdapter(model.getExistingTrophies(), this);

        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(TrophyCaseFragment.this.getContext()));

        //Button to create new trophy
        createNewTrophy = view.findViewById(R.id.add_trophy_button);
        createNewTrophy.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                CreateTrophyFragment createTrophyFragment = new CreateTrophyFragment();
                requireActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, createTrophyFragment, "CREATE_TROPHY")
                        .addToBackStack("CREATE_TROPHY")
                        .commit();

        }});
        if (parentModeUtility.isInParentMode()) {
            createNewTrophy.setVisibility(View.VISIBLE);
        }
        else {
            createNewTrophy.setVisibility(View.GONE);
        }
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        onParentModeChanged();
    }

    public void setPointsDisplay() {
        pointsDisplay.setText(String.format(Locale.US, "$%d", model.getPointsEarned()));
    }

    @Override
    public void onEntryClick(int position) {
        Bundle bundle = new Bundle();
        bundle.putInt("position", position);
        TrophyFragment trophyFragment = new TrophyFragment();
        trophyFragment.setArguments(bundle);
        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, trophyFragment, "TROPHY")
                .addToBackStack("TROPHY")
                .commit();
    }

    @Override
    public void update(Observable observable, Object arg) {
        if (observable instanceof DataModel) {
            adapter = new TrophyAdapter(model.getExistingTrophies(), this);
            recyclerView.setAdapter(adapter);
            setPointsDisplay();
        } else if (observable instanceof ParentModeUtility) {
            onParentModeChanged();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        model.deleteObserver(this);
        parentModeUtility.deleteObserver(this);
    }

    public void onParentModeChanged() {
        if(parentModeUtility.isInParentMode()) {
            createNewTrophy.setVisibility(View.VISIBLE);
        }
        else {
            createNewTrophy.setVisibility(View.GONE);
        }
        recyclerView.setAdapter(adapter);
    }
}




