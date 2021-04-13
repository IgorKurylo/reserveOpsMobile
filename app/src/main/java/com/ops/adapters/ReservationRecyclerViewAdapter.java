package com.ops.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ops.R;
import com.ops.models.Reserve;
import com.ops.models.Role;
import com.ops.utils.CacheManager;
import com.ops.utils.Constant;
import com.ops.utils.UiUtils;

import java.text.ParseException;
import java.util.List;
import java.util.Locale;

public class ReservationRecyclerViewAdapter extends RecyclerView.Adapter<ReservationRecyclerViewAdapter.ViewHolder> {

    private List<Reserve> reserveList;
    private Context mContext;
    private AdminActionListener actionListener;

    public ReservationRecyclerViewAdapter(List<Reserve> reserveList, Context context) {
        this.reserveList = reserveList;
        this.mContext = context;
    }

    public void setActionListener(AdminActionListener actionListener) {
        this.actionListener = actionListener;
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
        if (Role.valueOf(CacheManager.getInstance().getString(Constant.ROLE)) == Role.SimpleUser) {
            holder.adminActionLayout.setVisibility(View.GONE);
        } else {
            holder.approveReservation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    actionListener.OnApprove(reserve.getId());
                }
            });
            holder.declineReservation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    actionListener.OnApprove(reserve.getId());
                }
            });
        }

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
        Button approveReservation, declineReservation;
        RelativeLayout adminActionLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            restaurantName = itemView.findViewById(R.id.restaurantName);
            reservationTime = itemView.findViewById(R.id.timeReservation);
            reservationDate = itemView.findViewById(R.id.dateReservation);
            reservationGuests = itemView.findViewById(R.id.reservationGuest);
            restaurantImage = itemView.findViewById(R.id.restaurantImage);
            approveReservation = itemView.findViewById(R.id.approveReservation);
            declineReservation = itemView.findViewById(R.id.declineReservation);
            adminActionLayout = itemView.findViewById(R.id.adminActionLayout);
        }
    }

    public interface AdminActionListener {
        void OnApprove(int reservationId);

        void OnDecline(int reservationId);
    }
}
