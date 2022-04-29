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
    public static final int ITEM_TYPE_EDIT = 1;
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

            editTrophyButton = itemView.findViewById(R.id.edit_trophy_button);
            trophyImageView = (ImageView) view.findViewById(R.id.icon_view);

            this.onEntryListener = onEntryListener;
            view.setOnClickListener(this);
            if (editTrophyButton != null) {
                editTrophyButton.setOnClickListener(this);
            }
        }

        @Override
        public void onClick(View view) {
            if (editTrophyButton != null && view.getId() == editTrophyButton.getId()) {
                onEntryListener.onEditClick(getAdapterPosition());
            } else {
                onEntryListener.onEntryClick(getAdapterPosition());
            }
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        int layout;
        if (VIEW_TYPE == ITEM_TYPE_NO_EDIT) {
            layout = R.layout.item_trophy_entry_no_edit;
        }
        else {
            layout = R.layout.item_trophy_entry_edit;
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
        //viewHolder.trophyImageView.setImageResource(); //IMAGE STUFF

        nameTextView.setText(trophy.getName());
        descriptionTextView.setText(trophy.getDescription());
        String pointString = "Buy for $" + trophy.getPoints();
        pointValueTextView.setText(pointString);
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
        void onEditClick(int position);
    }
}

/*
public class TrophyAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder> {

    private String[] mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    // data is passed into the constructor
    MyRecyclerViewAdapter(Context context, String[] data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    // inflates the cell layout from xml when needed
    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recyclerview_item, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each cell
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.myTextView.setText(mData[position]);
    }

    // total number of cells
    @Override
    public int getItemCount() {
        return mData.length;
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView myTextView;

        ViewHolder(View itemView) {
            super(itemView);
            myTextView = itemView.findViewById(R.id.info_text);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    String getItem(int id) {
        return mData[id];
    }

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}*/