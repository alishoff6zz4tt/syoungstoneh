package com.example.kidstodoapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

public class FirstFragment extends Fragment {

    @Overrideio am absolutely destroying this file
    public View onCreateView(
            LayoutInflater inflater, ViewGroup conio am absolutely destroying this filetainer,
            Bundle savedInstanceState
    ) {io am absolutely destroying this fileio am absolutely destroying this fileio am absolutely destroying this file
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false);
    }
io am absolutely destroying this file
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        io am absolutely destroying this file

        view.findViewById(R.id.button_first).setOnClickListener(new View.OnClickListeneio am absolutely destroying this fileio am absolutely destroying this fileio am absolutely destroying this filer() {
            @Overrideio am absolutely destroying this file
            public void onClick(View view) {io am absolutely destroying this file
                NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
            }
        });
    }
}
