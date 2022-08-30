package com.example.kidstodoapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Locale;
import java.util.Observable;

public class ToDoListFragment extends Fragment implements java.util.Observer, ToDoAdapter.OnEntryListener{

    private DataModel model;

    private ToDoAdapter adapter;
    private RecyclerView recyclerView;
    private TextView pointsDisplay;
    private Button addEntryButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_to_do_list, container, false);

        model = DataModel.getInstance();
        model.addObserver(this);

        pointsDisplay = view.findViewById(R.id.points_display);
        adapter = new ToDoAdapter(model.getToDoEntries(), this);
        if (ParentModeUtility.isInParentMode()) {
            adapter.setVIEW_TYPE(ToDoAdapter.ITEM_TYPE_EDIT);
        }
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(ToDoListFragment.this.getContext()));

        setPointsDisplay();

        addEntryButton = view.findViewById(R.id.add_entry_button);
        if(ParentModeUtility.isInParentMode()) {addEntryButton.setVisibility(View.VISIBLE);}
        else {addEntryButton.setVisibility(View.GONE);}

        addEntryButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                CreateToDoEntryFragment createToDoEntryFragment = new CreateToDoEntryFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, createToDoEntryFragment, "CREATE_TO_DO_ENTRY")
                        .addToBackStack("CREATE_TO_DO_ENTRY")
                        .commit();
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(ParentModeUtility.inParentModeSet()) {
            ParentModeUtility.setInParentModeObserver(false);
            ((MainActivity) getActivity()).onParentModeChanged();
        }
    }

    @Override
    public void update(Observable observable, Object o) {
        if (observable instanceof DataModel) {
            adapter = new ToDoAdapter(model.getToDoEntries(), this);
            if(ParentModeUtility.isInParentMode()) {adapter.setVIEW_TYPE(ToDoAdapter.ITEM_TYPE_EDIT);}        //Had to add this because this listener was being called every time the fragment came back into view
            else {adapter.setVIEW_TYPE(ToDoAdapter.ITEM_TYPE_NO_EDIT);}
            recyclerView.setAdapter(adapter);
            setPointsDisplay();
        }
    }

    @Override
    public void onEntryClick(int position) {
        Bundle bundle = new Bundle();
        bundle.putInt("position", position);
        bundle.putBoolean("completed", false);
        ToDoEntryFragment toDoEntryFragment = new ToDoEntryFragment();
        toDoEntryFragment.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, toDoEntryFragment, "TO_DO_ENTRY")
                .addToBackStack("TO_DO_ENTRY")
                .commit();
    }

    @Override
    public void onEditClick(int position) {
        Bundle bundle = new Bundle();
        bundle.putInt("position", position);
        CreateToDoEntryFragment createToDoEntryFragment = new CreateToDoEntryFragment();
        createToDoEntryFragment.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, createToDoEntryFragment, "CREATE_TO_DO_ENTRY")
                .addToBackStack("CREATE_TO_DO_ENTRY")
                .commit();
    }

    public void setPointsDisplay() {
        pointsDisplay.setText(String.format(Locale.US, "$%d", model.getPointsEarned()));
    }

    @Override
    public void onStop() {
        super.onStop();
        model.deleteObserver(this);
    }

    public void onParentModeChanged() {                              //When parent mode is changed
        if(ParentModeUtility.isInParentMode()) {                               //Set the visibility and views accordingly
            addEntryButton.setVisibility(View.VISIBLE);
            adapter.setVIEW_TYPE(ToDoAdapter.ITEM_TYPE_EDIT);
        }
        else {
            addEntryButton.setVisibility(View.GONE);
            adapter.setVIEW_TYPE(ToDoAdapter.ITEM_TYPE_NO_EDIT);
        }
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).setCheckedItem(R.id.ToDoListFragment);
    }
}
