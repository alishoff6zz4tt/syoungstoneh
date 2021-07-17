package com.example.kidstodoapp;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

// Uses example from https://guides.codepath.com/android/using-the-recyclerview

public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.ToDoViewHolder> {

    public static class ToDoViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTextView;
        public TextView descriptionTextView;

        public ToDoViewHolder(View view) {
            super(view);

            nameTextView = (TextView) itemView.findViewById(R.id.entry_name);
            descriptionTextView = (TextView) itemView.findViewById(R.id.entry_description);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //
                }
            });
        }
    }

    private List<ToDoEntry> entries;

    public ToDoAdapter(List<ToDoEntry> entries) {
        this.entries = entries;
    }

    @Override
    public ToDoAdapter.ToDoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView = inflater.inflate(R.layout.item_to_do_entry, parent, false);
        ToDoViewHolder toDoViewHolder = new ToDoViewHolder(contactView);
        return toDoViewHolder;
    }

    @Override
    public void onBindViewHolder(ToDoAdapter.ToDoViewHolder viewHolder, int position) {
        ToDoEntry entry = entries.get(position);
        TextView nameTextView = viewHolder.nameTextView;
        TextView descriptionTextView = viewHolder.descriptionTextView;
        nameTextView.setText(entry.getEntryName());
        descriptionTextView.setText(entry.getDescription());

        String[] colors = {"#ECCCC5", "#D2ECC5", "#C5E5EC", "#E0C5EC"};
        viewHolder.itemView.setBackgroundColor(Color.parseColor(colors[position % colors.length]));
    }

    @Override
    public int getItemCount() {
        return entries.size();
    }

}
