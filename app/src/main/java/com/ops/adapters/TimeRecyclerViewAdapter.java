package com.ops.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ops.R;

import java.util.List;

public class TimeRecyclerViewAdapter extends RecyclerView.Adapter<TimeRecyclerViewAdapter.ViewHolder> {

    List<String> timesList;
    private Context mContext;
    private int currentSelection = 0;

    public TimeRecyclerViewAdapter(List<String> timesList, Context context) {
        this.timesList = timesList;
        this.mContext = context;
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
        holder.timeTextView.setText(this.timesList.get(position));
        holder.timeTextView.setSelected(currentSelection == position);
        holder.timeTextView.setOnClickListener(v -> {
            if (currentSelection != position) {
                currentSelection = position;
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.timesList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView timeTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            timeTextView = itemView.findViewById(R.id.time);
        }
    }
}
