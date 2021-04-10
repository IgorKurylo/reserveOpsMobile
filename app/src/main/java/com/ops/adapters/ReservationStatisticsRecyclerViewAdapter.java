package com.ops.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ops.R;
import com.ops.models.ReservationWeekStatistics;

import java.util.List;

public class ReservationStatisticsRecyclerViewAdapter extends RecyclerView.Adapter<ReservationStatisticsRecyclerViewAdapter.ViewHolder> {

    private Context mContext;
    private List<ReservationWeekStatistics> statisticsList;

    public ReservationStatisticsRecyclerViewAdapter(Context mContext, List<ReservationWeekStatistics> statisticsList) {
        this.mContext = mContext;
        this.statisticsList = statisticsList;
    }

    @NonNull
    @Override
    public ReservationStatisticsRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.bar_layout, parent, false);

        return new ReservationStatisticsRecyclerViewAdapter.ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ReservationStatisticsRecyclerViewAdapter.ViewHolder holder, int position) {
        ReservationWeekStatistics statistics = statisticsList.get(position);
        holder.reservationTextView.setText(String.valueOf(statistics.getReservations()));
        holder.dayTextView.setText(statistics.getDay());
        holder.progressBar.setProgress((int) (statistics.getReservationsInPercent() * 100));
    }

    @Override
    public int getItemCount() {
        return statisticsList.size();
    }

    public void updateList(List<ReservationWeekStatistics> reservationWeekList) {
        this.statisticsList = reservationWeekList;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ProgressBar progressBar;
        private TextView dayTextView, reservationTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressBar);
            dayTextView = itemView.findViewById(R.id.dayName);
            reservationTextView = itemView.findViewById(R.id.reservationCount);
        }
    }
}
