package com.ops.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ops.R;
import com.ops.models.Restaurant;
import com.ops.utils.Constant;
import com.ops.utils.UiUtils;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class RestaurantRecyclerViewAdapter extends RecyclerView.Adapter<RestaurantRecyclerViewAdapter.ViewHolder> {

    private List<Restaurant> restaurantList;
    private Context mContext;
    public OnChooseRestaurantListener listener;

    public RestaurantRecyclerViewAdapter(List<Restaurant> restaurantList, Context context) {
        this.restaurantList = restaurantList;
        this.mContext = context;
    }

    public void setListener(OnChooseRestaurantListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.restaurant_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Restaurant restaurant = this.restaurantList.get(position);
        holder.restName.setText(restaurant.getRestaurantName());
        holder.restAddress.setText(restaurant.getAddress());
        holder.restaurantAreaTxt.setText(restaurant.getArea());
        holder.restWorkingTime.setText(String.format(mContext.getString(R.string.openAtRest),
                restaurant.getWorkTimeStart(), restaurant.getWorkTimeEnd()));
        try {
            setOpenCloseStatus(holder, restaurant);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Glide.with(mContext)
                .load(restaurant.getImageUrl())
                .apply(new RequestOptions().circleCrop())
                .error(R.drawable.ic_restaurant)
                .into(holder.restImageView);
        holder.restItemCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.OnChooseRestaurant(restaurant);
            }
        });
    }

    private void setOpenCloseStatus(@NonNull ViewHolder holder, Restaurant restaurant) throws ParseException {
        Date startTime = UiUtils.convertTimeFromString(restaurant.getWorkTimeStart());
        Date endTime = UiUtils.convertTimeFromString(restaurant.getWorkTimeEnd());
        Calendar start = Calendar.getInstance();
        Calendar end = Calendar.getInstance();
        Calendar now = Calendar.getInstance();
        start.setTime(startTime);
        end.setTime(endTime);
        int nowHour = now.get(Calendar.HOUR_OF_DAY);
        int startHour = start.get(Calendar.HOUR_OF_DAY);
        int endHour = end.get(Calendar.HOUR_OF_DAY) == 0 ? Constant.MIDNIGHT : end.get(Calendar.HOUR_OF_DAY);
        if (startHour <= nowHour && nowHour <= endHour) {
            holder.restaurantOpenStatus.setText(mContext.getString(R.string.open));
            holder.restaurantOpenStatus.setTextColor(mContext.getColor(R.color.green));
        } else {
            holder.restaurantOpenStatus.setText(mContext.getString(R.string.close));
            holder.restaurantOpenStatus.setTextColor(mContext.getColor(R.color.colorPrimary));
        }

    }

    @Override
    public int getItemCount() {
        return this.restaurantList.size();
    }

    public void updateRestaurants(List<Restaurant> restaurantList) {
        this.restaurantList = restaurantList;
        notifyDataSetChanged();
    }

    public List<Restaurant> getRestaurantList() {
        return restaurantList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView restName, restAddress, restWorkingTime, restaurantAreaTxt, restaurantOpenStatus;
        ImageView restImageView;
        CardView restItemCardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            restName = itemView.findViewById(R.id.restNameTxt);
            restAddress = itemView.findViewById(R.id.restAddressTxt);
            restWorkingTime = itemView.findViewById(R.id.restWorkingTimeTxt);
            restaurantAreaTxt = itemView.findViewById(R.id.restaurantAreaTxt);
            restImageView = itemView.findViewById(R.id.restImageView);
            restItemCardView = itemView.findViewById(R.id.restItemCardView);
            restaurantOpenStatus = itemView.findViewById(R.id.restaurantOpenOrClose);
        }
    }

    public interface OnChooseRestaurantListener {
        void OnChooseRestaurant(Restaurant restaurant);
    }
}
