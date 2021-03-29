package com.ops.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ops.R;
import com.ops.models.AvailableTime;

import java.util.List;

public class TimeRecyclerViewAdapter extends RecyclerView.Adapter<TimeRecyclerViewAdapter.ViewHolder> {

    List<AvailableTime> timesList;
    private Context mContext;
    private int currentSelection = 0;
    private OnTimeSelectionListener timeSelectionListener;

    public TimeRecyclerViewAdapter(List<AvailableTime> timesList, Context context) {
        this.timesList = timesList;
        this.mContext = context;
    }

    public void setTimeSelectionListener(OnTimeSelectionListener timeSelectionListener) {
        this.timeSelectionListener = timeSelectionListener;
    }

    @NonNull
    @Override
    public TimeRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.time_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TimeRecyclerViewAdapter.ViewHolder holder, int position) {
        if (!this.timesList.get(position).isAvailable()) {
            holder.timeTextView.setVisibility(View.INVISIBLE);
        } else {
            holder.timeTextView.setVisibility(View.VISIBLE);
            holder.timeTextView.setText(this.timesList.get(position).getTime());
            holder.timeTextView.setSelected(currentSelection == position);
            holder.timeTextView.setOnClickListener(v -> {
                if (currentSelection != position) {
                    currentSelection = position;
                    notifyDataSetChanged();
                    timeSelectionListener.onTimeSelection(this.timesList.get(position).getTime());
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return this.timesList.size();
    }

    public void updateList(List<AvailableTime> availableTimeList) {
        this.timesList = availableTimeList;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView timeTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            timeTextView = itemView.findViewById(R.id.time);
        }
    }

    public static interface OnTimeSelectionListener {
        void onTimeSelection(String time);
    }
}
