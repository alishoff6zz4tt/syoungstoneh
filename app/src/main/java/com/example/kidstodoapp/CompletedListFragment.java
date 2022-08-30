package com.example.kidstodoapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Observable;

public class CompletedListFragment extends Fragment implements java.util.Observer, ToDoAdapter.OnEntryListener{

    private DataModel model;

    private ToDoAdapter adapter;
    private RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_completed_list, container, false);

        model = DataModel.getInstance();
        model.addObserver(this);

        adapter = new ToDoAdapter(model.getCompletedEntries(), this);
        recyclerView = view.findViewById(R.id.recycler_view_completed);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(CompletedListFragment.this.getContext()));

        return view;
    }

    @Override
    public void update(Observable observable, Object o) {
        if (observable instanceof DataModel) {
            adapter = new ToDoAdapter(model.getCompletedEntries(), this);
            recyclerView.setAdapter(adapter);
        }
    }

    @Override
    public void onEntryClick(int position) {
        Bundle bundle = new Bundle();
        bundle.putInt("position", position);
        bundle.putBoolean("completed", true);
        ToDoEntryFragment toDoEntryFragment = new ToDoEntryFragment();
        toDoEntryFragment.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, toDoEntryFragment, "TO_DO_ENTRY")
                .addToBackStack("TO_DO_ENTRY")
                .commit();
    }

    @Override
    public void onEditClick(int position) {}

    @Override
    public void onStop() {
        super.onStop();
        model.deleteObserver(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).setCheckedItem(R.id.ConfirmCompleted);
    }
}
