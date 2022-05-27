/*
package com.example.kidstodoapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class TrophyGridFragment extends Fragment implements TrophyAdapter.OnEntryListener{
    private static ArrayList<Trophy> existingTrophies = new ArrayList<>();
    private static ArrayList<Trophy> archivedTrophies = new ArrayList<>();

    private static int pointsEarned = 0;
    private TrophyAdapter adapter;
    private RecyclerView recyclerView;

    private final int NEW_ENTRY_REQUEST = 1;
    private final int VIEW_ENTRY_REQUEST = 2;
    private final int EDIT_ENTRY_REQUEST = 3;

    private ImageButton trophy;
    private TextView pointsDisplay;
    private Button createNewTrophy;
    private Button parentModeButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_to_do_list, container, false);

        pointsDisplay = view.findViewById(R.id.points_display);
        adapter = new TrophyAdapter(existingTrophies, this);

        //Recycler View Setup
        adapter = new TrophyAdapter(existingTrophies, this);

        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //Button to create new trophy
        createNewTrophy = view.findViewById(R.id.add_trophy_button);
        createNewTrophy.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), CreateTrophyActivity.class);
                startActivityForResult(intent, NEW_ENTRY_REQUEST);
            }
        });

        parentModeButton = view.findViewById(R.id.parentModeTC);
        parentModeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Utility.isInParentMode()) {
                    Utility.setInParentMode(false);
                    onParentModeChanged();
                    Toast.makeText(TrophyCase.this,
                            "Exiting parent mode",
                            Toast.LENGTH_SHORT).show();
                }
                else {
                    Intent intent = new Intent(view.getContext(), ConfirmPassword.class);
                    startActivity(intent);
                    //createNewTrophy.setVisibility(View.VISIBLE);
                }
            }
        });

        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this.getContext(), ));

        setPointsDisplay();


    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FragmentViewModel.isInParentMode().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(FragmentViewModel.inParentModeSet()){                    //This is to prevent the code below from being called every time the fragment comes back into view
                    FragmentViewModel.setInParentModeObserver(false);       //Read something about an event wrapper being the proper means of subverting this but I can't find anything not in Kotlin gibberish
                    ((MainActivity) getActivity()).onParentModeChanged();   //https://medium.com/androiddevelopers/livedata-with-snackbar-navigation-and-other-events-the-singleliveevent-case-ac2622673150
                }
            }
        });
        FragmentViewModel.getReturnBundle().observe(getViewLifecycleOwner(), new Observer<Bundle>() {
            @Override
            public void onChanged(Bundle bundle) {
                if(FragmentViewModel.returnBundleSet()) {
                    FragmentViewModel.setReturnBundleObserver(false);
                    if(bundle != null) {
                        int requestCode = bundle.getInt("requestCode");
                        int resultCode = bundle.getInt("resultCode");

                        if (requestCode == NEW_ENTRY_REQUEST) {
                            if (resultCode == Activity.RESULT_OK) {
                                ToDoEntry newToDoEntry = (ToDoEntry) bundle.getSerializable("ToDoEntry");
                                toDoEntries.add(newToDoEntry);
                                Collections.sort(toDoEntries);
                                recyclerView.setAdapter(adapter);
                                documentReference.update("toDoEntries", toDoEntries);
                            }
                        }
                        if (requestCode == EDIT_ENTRY_REQUEST) {
                            if (resultCode == Activity.RESULT_OK) {
                                int position = bundle.getInt("position");
                                if (bundle.getBoolean("Deleted")) {
                                    toDoEntries.remove(position);
                                    adapter.notifyItemRemoved(position);
                                }
                                else {
                                    ToDoEntry changedToDoEntry = (ToDoEntry) bundle.getSerializable("ToDoEntry");
                                    toDoEntries.set(position, changedToDoEntry);
                                    Collections.sort(toDoEntries);
                                }
                                recyclerView.setAdapter(adapter);
                                documentReference.update("toDoEntries", toDoEntries);
                            }
                        }
                        if (requestCode == VIEW_ENTRY_REQUEST) {
                            if (resultCode == Activity.RESULT_OK) {
                                int position = bundle.getInt("position");
                                ToDoEntry entry = toDoEntries.remove(position);
                                adapter.notifyItemRemoved(position);
                                entry.setCompleted(true);
                                completedEntries.add(entry);
                                pointsEarned += entry.getPointValue();
                                setPointsDisplay();
                                documentReference.update("toDoEntries", toDoEntries);
                                documentReference.update("pointsEarned", pointsEarned);
                            }
                        }
                    }
                }
            }
        });
    }

    @Override
    public void onEntryClick(int position) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("requestCode", VIEW_ENTRY_REQUEST);
        bundle.putSerializable("ToDoEntry", toDoEntries.get(position));
        bundle.putInt("position", position);
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
        bundle.putSerializable("requestCode", EDIT_ENTRY_REQUEST);
        bundle.putSerializable("ToDoEntry", toDoEntries.get(position));
        bundle.putInt("position", position);
        CreateToDoEntryFragment createToDoEntryFragment = new CreateToDoEntryFragment();
        createToDoEntryFragment.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, createToDoEntryFragment, "CREATE_TO_DO_ENTRY")
                .addToBackStack("CREATE_TO_DO_ENTRY")
                .commit();
    }

    public void setPointsDisplay() {
        pointsDisplay.setText(String.format(Locale.US, "$%d", pointsEarned));
    }

    public ArrayList<ToDoEntry> buildToDoEntries(ArrayList<HashMap<String, Object>> list) {
        ArrayList<ToDoEntry> arrayList = new ArrayList<>();
        for (HashMap<String, Object> map : list) {
            arrayList.add(ToDoEntry.buildToDoEntry(map));
        }
        return arrayList;
    }

    @Override
    public void onStop() {
        super.onStop();
        registration.remove();
    }

    public void onParentModeChanged() {                              //When parent mode is changed
        if(Utility.isInParentMode()) {                               //Set the visibility and views accordingly
            addEntryButton.setVisibility(View.VISIBLE);
            adapter.setVIEW_TYPE(ToDoAdapter.ITEM_TYPE_EDIT);
        }
        else {
            addEntryButton.setVisibility(View.GONE);
            adapter.setVIEW_TYPE(ToDoAdapter.ITEM_TYPE_NO_EDIT);
        }
        recyclerView.setAdapter(adapter);
    }
}
}
*/
