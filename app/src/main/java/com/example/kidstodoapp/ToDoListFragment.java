package com.example.kidstodoapp;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;

public class ToDoListFragment extends Fragment implements ToDoAdapter.OnEntryListener{
    private static ArrayList<ToDoEntry> toDoEntries = new ArrayList<>();
    private static ArrayList<ToDoEntry> completedEntries = new ArrayList<>();
    private static long pointsEarned = 0;
    private static String uid;

    private final int NEW_ENTRY_REQUEST = 1;
    private final int VIEW_ENTRY_REQUEST = 2;
    private final int EDIT_ENTRY_REQUEST = 3;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    private ToDoAdapter adapter;
    private RecyclerView recyclerView;
    private TextView pointsDisplay;
    private Button addEntryButton;

    private ListenerRegistration registration;

    private DocumentReference documentReference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MainActivity)getActivity()).tabVisibility(true);
        View view = inflater.inflate(R.layout.fragment_to_do_list, container, false);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        uid = mAuth.getCurrentUser().getUid();

        pointsDisplay = view.findViewById(R.id.points_display);
        adapter = new ToDoAdapter(toDoEntries, this);

        documentReference = db.collection("users").document(uid);
        registration = documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException error) {
                toDoEntries = buildToDoEntries((ArrayList<HashMap<String, Object>>) snapshot.get("toDoEntries"));
                completedEntries = buildToDoEntries((ArrayList<HashMap<String, Object>>) snapshot.get("completedEntries"));
                pointsEarned = (Long) snapshot.get("pointsEarned");
                Utility.setPhoneNumber(snapshot.getString("phoneNumber"));
                adapter = new ToDoAdapter(toDoEntries, ToDoListFragment.this);

                if(Utility.isInParentMode()) {adapter.setVIEW_TYPE(ToDoAdapter.ITEM_TYPE_EDIT);}        //Had to add this because this listener was being called every time the fragment came back into view
                else {adapter.setVIEW_TYPE(ToDoAdapter.ITEM_TYPE_NO_EDIT);}

                recyclerView.setAdapter(adapter);
                setPointsDisplay();
            }
        });

        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(ToDoListFragment.this.getContext()));

        setPointsDisplay();

        addEntryButton = view.findViewById(R.id.add_entry_button);
        if(Utility.isInParentMode()) {addEntryButton.setVisibility(View.VISIBLE);}
        else {addEntryButton.setVisibility(View.GONE);}

        addEntryButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putInt("requestCode", NEW_ENTRY_REQUEST);
                CreateToDoEntryFragment createToDoEntryFragment = new CreateToDoEntryFragment();
                createToDoEntryFragment.setArguments(bundle);
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
        if(Utility.inParentModeSet()){
            Utility.setInParentModeObserver(false);
            ((MainActivity) getActivity()).onParentModeChanged();
        }
        if(Utility.returnBundleSet()) {
            Utility.setReturnBundleObserver(false);
            Bundle bundle = Utility.getReturnBundle();
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
