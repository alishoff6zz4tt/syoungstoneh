package com.example.kidstodoapp;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

// Uses example from https://guides.codepath.com/android/using-the-recyclerview

public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.ViewHolder> {

    private List<ToDoEntry> mToDoEntries;
    private OnEntryListener mOnEntryListener;

    public ToDoAdapter(List<ToDoEntry> entries, OnEntryListener onEntryListener) {
        this.mToDoEntries = entries;
        this.mOnEntryListener = onEntryListener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView nameTextView;
        public TextView descriptionTextView;
        public OnEntryListener onEntryListener;

        public ViewHolder(View view, OnEntryListener onEntryListener) {
            super(view);

            nameTextView = itemView.findViewById(R.id.entry_name);
            descriptionTextView = itemView.findViewById(R.id.entry_description);
            this.onEntryListener = onEntryListener;

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onEntryListener.onEntryClick(getAdapterPosition());
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView = inflater.inflate(R.layout.item_to_do_entry, parent, false);
        return new ViewHolder(contactView, mOnEntryListener);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        ToDoEntry entry = mToDoEntries.get(position);
        TextView nameTextView = viewHolder.nameTextView;
        TextView descriptionTextView = viewHolder.descriptionTextView;
        nameTextView.setText(entry.getEntryName());
        descriptionTextView.setText(entry.getDescription());

        String[] colors = {"#ECCCC5", "#D2ECC5", "#C5E5EC", "#E0C5EC"};
        viewHolder.itemView.setBackgroundColor(Color.parseColor(colors[position % colors.length]));
    }

    @Override
    public int getItemCount() {
        return mToDoEntries.size();
    }

    public interface OnEntryListener {
        void onEntryClick(int position);
    }

}
