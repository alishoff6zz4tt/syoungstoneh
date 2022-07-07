package com.example.kidstodoapp;

import android.content.Context;
import android.graphics.Color;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class TrophyAdapter extends RecyclerView.Adapter<TrophyAdapter.ViewHolder> {

    private List<Trophy> mTrophies;
    private OnEntryListener mOnEntryListener;

    public static final int ITEM_TYPE_NO_EDIT = 0;
    private int VIEW_TYPE = 0;

    public TrophyAdapter(List<Trophy> trophies, OnEntryListener onEntryListener) {
        this.mTrophies = trophies;
        this.mOnEntryListener = onEntryListener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView nameTextView;
        public TextView descriptionTextView;
        public ImageView trophyImageView;
        public TextView pointValueTextView;

        public OnEntryListener onEntryListener;
        public ImageButton editTrophyButton;


        public ViewHolder(View view, OnEntryListener onEntryListener) {
            super(view);

            nameTextView = itemView.findViewById(R.id.trophy_name);
            descriptionTextView = itemView.findViewById(R.id.trophy_description);
            pointValueTextView = itemView.findViewById(R.id.trophy_points);
            trophyImageView = itemView.findViewById(R.id.trophy_icon);

            editTrophyButton = itemView.findViewById(R.id.edit_trophy_button);

            this.onEntryListener = onEntryListener;
            view.setOnClickListener(this);
            if (editTrophyButton != null) {
                editTrophyButton.setOnClickListener(this);
            }

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
        int layout = 0;
        if (VIEW_TYPE == ITEM_TYPE_NO_EDIT) {
            layout = R.layout.item_trophy_entry_no_edit;
        }

        View contactView = inflater.inflate(layout, parent, false);
        return new ViewHolder(contactView, mOnEntryListener);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        Trophy trophy = mTrophies.get(position);
        TextView nameTextView = viewHolder.nameTextView;
        TextView descriptionTextView = viewHolder.descriptionTextView;
        TextView pointValueTextView = viewHolder.pointValueTextView;
        ImageView trophyImageView = viewHolder.trophyImageView;


        nameTextView.setText(trophy.getName());
        //descriptionTextView.setText(trophy.getDescription());
        //trophyImageView.setImageResource(R.drawable.trophy2); //IMAGE STUFF
        //viewHolder.trophyImageView.setImageResource(Integer.parseInt(trophy.getImage())); //IMAGE STUFF
        //String pointString = "Buy for $" + trophy.getPoints();
        //pointValueTextView.setText(trophy.getPoints());
    }

    @Override
    public int getItemCount() {
        return mTrophies.size();
    }

    public void setVIEW_TYPE(int viewType) {
        VIEW_TYPE = viewType;
        notifyDataSetChanged();
    }

    public interface OnEntryListener {
        void onEntryClick(int position);
    }
}

