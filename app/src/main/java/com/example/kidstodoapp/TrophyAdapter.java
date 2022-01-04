package com.example.kidstodoapp;

import android.content.Context;
import android.graphics.Color;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

// Uses example from https://guides.codepath.com/android/using-the-recyclerview

public class TrophyAdapter extends RecyclerView.Adapter<TrophyAdapter.ViewHolder> {

    private List<Trophy> Trophies;
    private OnEntryListener OnEntryListener;

    public static final int ITEM_TYPE_NO_EDIT = 0;
    public static final int ITEM_TYPE_EDIT = 1;
    private int VIEW_TYPE = 0;

    public TrophyAdapter(List<Trophy> trophies, OnEntryListener onEntryListener) {
        this.Trophies = trophies;
        this.OnEntryListener = onEntryListener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView nameTextView;
        public TextView descriptionTextView;
        public OnEntryListener onEntryListener;
        public ImageButton editEntryButton;

        public ViewHolder(View view, OnEntryListener onEntryListener) {
            super(view);

            nameTextView = itemView.findViewById(R.id.entry_name);
            descriptionTextView = itemView.findViewById(R.id.entry_description);
            editEntryButton = itemView.findViewById(R.id.edit_entry_button);

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
        int layout;
        if (VIEW_TYPE == ITEM_TYPE_NO_EDIT) {
            layout = R.layout.item_to_do_entry_no_edit;
        }
        else {
            layout = R.layout.item_to_do_entry_edit;
        }
        View contactView = inflater.inflate(layout, parent, false);
        return new ViewHolder(contactView, OnEntryListener);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        Trophy entry = Trophies.get(position);
        TextView nameTextView = viewHolder.nameTextView;
        TextView descriptionTextView = viewHolder.descriptionTextView;
        ImageButton editEntryButton = viewHolder.editEntryButton;

        nameTextView.setText(entry.getName());
        descriptionTextView.setText(entry.getDescription());
        editEntryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        //String[] colors = {"#ECCCC5", "#D2ECC5", "#C5E5EC", "#E0C5EC"};
        //viewHolder.itemView.setBackgroundColor(Color.parseColor(colors[position % colors.length]));
    }

    @Override
    public int getItemCount() {
        return Trophies.size();
    }

    public void setVIEW_TYPE(int viewType) {
        VIEW_TYPE = viewType;
        notifyDataSetChanged();
    }

    public interface OnEntryListener {
        void onEntryClick(int position);
    }

}
