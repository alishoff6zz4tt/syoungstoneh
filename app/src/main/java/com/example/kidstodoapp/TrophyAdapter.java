package com.example.kidstodoapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TrophyAdapter extends RecyclerView.Adapter<TrophyAdapter.ViewHolder> {

  private List<Trophy> mTrophies;
  private OnEntryListener mOnEntryListener;

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
      pointValueTextView = itemView.findViewById(R.id.trophy_points);
      trophyImageView = itemView.findViewById(R.id.trophy_icon);

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

  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    Context context = parent.getContext();
    LayoutInflater inflater = LayoutInflater.from(context);
    int layout = R.layout.item_trophy_entry;
    View contactView = inflater.inflate(layout, parent, false);
    return new ViewHolder(contactView, mOnEntryListener);
  }

  @Override
  public void onBindViewHolder(ViewHolder viewHolder, int position) {
    Trophy trophy = mTrophies.get(position);
    TextView nameTextView = viewHolder.nameTextView;
    TextView pointValueTextView = viewHolder.pointValueTextView;
    ImageView trophyImageView = viewHolder.trophyImageView;

    nameTextView.setText(trophy.getName());
//        trophyImageView.setImageResource(R.drawable.trophy2);
    trophyImageView.setImageResource(((Long) trophy.getImageLocation()).intValue());
    String pointString = "Buy for $" + trophy.getPointValue();
    pointValueTextView.setText(pointString);
  }

  @Override
  public int getItemCount() {
    return mTrophies.size();
  }

  public interface OnEntryListener {
    void onEntryClick(int position);
  }
}

