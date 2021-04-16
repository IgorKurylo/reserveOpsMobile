package com.ops.ui.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ops.R;
import com.ops.adapters.ReservationStatisticsRecyclerViewAdapter;
import com.ops.models.Restaurant;
import com.ops.models.response.AdminMetaDataResponse;
import com.ops.models.response.AdminStatisticsResponse;
import com.ops.models.response.BaseResponse;
import com.ops.network.NetworkApi;
import com.ops.network.services.IStatisticsService;
import com.ops.network.services.IUserService;
import com.ops.ui.AdminActivity;
import com.ops.utils.CacheManager;
import com.ops.utils.Constant;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.ObservableSource;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.schedulers.Schedulers;

/**
 * Admin Dashboard, Show admin statistics
 */
public class AdminDashboardFragment extends Fragment implements View.OnClickListener {

    TextView welcomeText, reservationCountTxt, pendingReservation, restName, reservationWeekTxt;
    ImageView userAvatar, restImage;
    RecyclerView recyclerviewWeekStats;
    private ReservationStatisticsRecyclerViewAdapter adapter;
    private ImageView openReservationBtn;
    private AdminActivity mContext;

    public AdminDashboardFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = (AdminActivity) context;
    }

    public static AdminDashboardFragment newInstance() {
        return new AdminDashboardFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.admin_fragment, container, false);
        findViews(view);
        initWeekStatsAdapter(recyclerviewWeekStats);
        initWelcomeLayout(welcomeText, userAvatar);
        IStatisticsService statisticsService = NetworkApi.getInstance().getRetrofit().create(IStatisticsService.class);
        IUserService userService = NetworkApi.getInstance().getRetrofit().create(IUserService.class);
        getAdminStatistic(statisticsService, userService);
        return view;

    }

    private void updateRestaurantUI(Restaurant restaurant) {
        restName.setText(restaurant.getRestaurantName());
        Glide.with(mContext)
                .load(restaurant.getImageUrl())
                .apply(new RequestOptions().circleCrop())
                .error(R.drawable.ic_restaurant_white)
                .into(restImage);
    }

    private void updateUI(AdminStatisticsResponse statisticsResponse) {
        reservationCountTxt.setText(String.valueOf(statisticsResponse.getTodayReservation()));
        pendingReservation.setText(String.valueOf(statisticsResponse.getPendingReservation()));
        if (statisticsResponse.getReservationWeekList().size() > 0) {
            reservationWeekTxt.setText(mContext.getString(R.string.reservationWeek));
            reservationWeekTxt.setTextColor(mContext.getColor(R.color.black));
            adapter.updateList(statisticsResponse.getReservationWeekList());
        } else {
            reservationWeekTxt.setText(mContext.getString(R.string.noReservationInWeek));
            reservationWeekTxt.setTextColor(mContext.getColor(R.color.colorPrimary));
        }

    }

    private void findViews(View view) {
        recyclerviewWeekStats = view.findViewById(R.id.recyclerviewWeekStats);
        reservationWeekTxt = view.findViewById(R.id.reservationWeek);
        welcomeText = view.findViewById(R.id.welcomeText);
        userAvatar = view.findViewById(R.id.userAvatar);
        reservationCountTxt = view.findViewById(R.id.reservationCountTxt);
        pendingReservation = view.findViewById(R.id.pendingReservation);
        restName = view.findViewById(R.id.restNameTxt);
        restImage = view.findViewById(R.id.restImageView);
        openReservationBtn = view.findViewById(R.id.openReservationBtn);
        openReservationBtn.setOnClickListener(this);
    }

    private void initWelcomeLayout(TextView welcomeText, ImageView userAvatar) {
        String imageUrl = CacheManager.getInstance().getString(Constant.AVATAR);
        Picasso.get().load(imageUrl)
                .error(R.drawable.ic_user)
                .into(userAvatar);
        welcomeText.setText(getString(R.string.welcomeText, CacheManager.getInstance().getString(Constant.FIRST_NAME) +
                " " + CacheManager.getInstance().getString(Constant.LAST_NAME)));
    }

    private void initWeekStatsAdapter(RecyclerView recyclerviewWeekStats) {
        adapter = new ReservationStatisticsRecyclerViewAdapter(mContext.getApplicationContext(), new ArrayList<>());
        recyclerviewWeekStats.setLayoutManager(new LinearLayoutManager(mContext.getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerviewWeekStats.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        mContext.showReservationTab();
    }

    private void getAdminStatistic(IStatisticsService service, IUserService userService) {
        userService.metaData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Function<BaseResponse<AdminMetaDataResponse>, ObservableSource<?>>() {
                    @Override
                    public ObservableSource<?> apply(BaseResponse<AdminMetaDataResponse> metadataResponse) throws Throwable {

                        AdminMetaDataResponse response = (AdminMetaDataResponse) metadataResponse.getData();
                        updateRestaurantUI(response.getRestaurant());
                        return service.adminStatistics(response.getRestaurant().getId());
                    }
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Object>() {
                    @Override
                    public void onNext(@io.reactivex.rxjava3.annotations.NonNull Object o) {
                        try {
                            BaseResponse<AdminStatisticsResponse> response = (BaseResponse<AdminStatisticsResponse>) o;
                            if (response.isSuccess()) {
                                AdminStatisticsResponse statisticsResponse = (AdminStatisticsResponse) response.getData();
                                updateUI(statisticsResponse);
                            }
                        } catch (ClassCastException ex) {
                            Log.e(AdminActivity.class.getName(), ex.toString());
                        }
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        Log.e(AdminActivity.class.getName() + "onError", e.toString());
                    }

                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {

                    }
                });
    }
}