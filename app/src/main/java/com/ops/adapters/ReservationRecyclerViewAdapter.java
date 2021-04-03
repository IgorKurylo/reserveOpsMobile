package com.ops.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ops.R;
import com.ops.models.Reserve;
import com.ops.utils.UiUtils;

import java.text.ParseException;
import java.util.List;
import java.util.Locale;

public class ReservationRecyclerViewAdapter extends RecyclerView.Adapter<ReservationRecyclerViewAdapter.ViewHolder> {

    private List<Reserve> reserveList;
    private Context mContext;

    public ReservationRecyclerViewAdapter(List<Reserve> reserveList, Context context) {
        this.reserveList = reserveList;
        this.mContext = context;
    }

    @NonNull
    @Override
    public ReservationRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.reserve_item, parent, false);

        return new ReservationRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReservationRecyclerViewAdapter.ViewHolder holder, int position) {
        Reserve reserve = this.reserveList.get(position);
        holder.restaurantName.setText(reserve.getRestaurant().getRestaurantName());
        holder.reservationGuests.setText(String.format(Locale.getDefault(),
                "%d %s", reserve.getGuestsNumber(), mContext.getString(R.string.guestLabel)));
        try {
            holder.reservationDate.setText(UiUtils.formatDate(reserve.getDate()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.reservationTime.setText(reserve.getTime());
        Glide.with(mContext)
                .load(reserve.getRestaurant().getImageUrl())
                .apply(new RequestOptions().circleCrop())
                .into(holder.restaurantImage);
    }

    @Override
    public int getItemCount() {
        return reserveList.size();
    }

    public void update(List<Reserve> reserveList) {
        this.reserveList = reserveList;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView restaurantName, reservationTime, reservationDate, reservationGuests;
        ImageView restaurantImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            restaurantName = itemView.findViewById(R.id.restaurantName);
            reservationTime = itemView.findViewById(R.id.timeReservation);
            reservationDate = itemView.findViewById(R.id.dateReservation);
            reservationGuests = itemView.findViewById(R.id.reservationGuest);
            restaurantImage = itemView.findViewById(R.id.restaurantImage);

        }
    }
}
