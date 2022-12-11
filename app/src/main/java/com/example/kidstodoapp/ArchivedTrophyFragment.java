package com.example.kidstodoapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Observable;

public class ArchivedTrophyFragment extends Fragment
    implements java.util.Observer, TrophyAdapter.OnEntryListener {

  private DataModel model;

  private TrophyAdapter adapter;
  private RecyclerView recyclerView;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    View view = inflater.inflate(R.layout.fragment_archived_trophy_case, container, false);

    model = DataModel.getInstance();
    model.addObserver(this);

    adapter = new TrophyAdapter(model.getArchivedTrophies(), this);
    recyclerView = view.findViewById(R.id.recycler_view_completed);
    recyclerView.setAdapter(adapter);
    recyclerView.setLayoutManager(new LinearLayoutManager(ArchivedTrophyFragment.this.getContext()));

    return view;
  }

  @Override
  public void update(Observable observable, Object o) {
    if (observable instanceof DataModel) {
      adapter = new TrophyAdapter(model.getArchivedTrophies(), this);
      recyclerView.setAdapter(adapter);
    }
  }

  @Override
  public void onEntryClick(int position) {
    Bundle bundle = new Bundle();
    bundle.putInt("position", position);
    bundle.putBoolean("completed", true);
    TrophyFragment trophyFragment = new TrophyFragment();
    trophyFragment.setArguments(bundle);
    requireActivity().getSupportFragmentManager().beginTransaction()
        .replace(R.id.fragment_container, trophyFragment, "TROPHY")
        .addToBackStack("TROPHY")
        .commit();
  }

  @Override
  public void onStop() {
    super.onStop();
    model.deleteObserver(this);
  }

  @Override
  public void onResume() {
    super.onResume();
    ((MainActivity) requireActivity()).setCheckedItem(R.id.ConfirmCompleted);
  }
}
