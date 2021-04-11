package com.ops.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ops.R;
import com.ops.adapters.ReservationStatisticsRecyclerViewAdapter;
import com.ops.models.ReservationWeekStatistics;
import com.ops.models.Restaurant;
import com.ops.models.response.AdminMetaDataResponse;
import com.ops.models.response.AdminStatisticsResponse;
import com.ops.models.response.BaseResponse;
import com.ops.network.NetworkApi;
import com.ops.network.services.IStatisticsService;
import com.ops.network.services.IUserService;
import com.ops.utils.CacheManager;
import com.ops.utils.Constant;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.ObservableSource;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminActivity extends AppCompatActivity implements View.OnClickListener {
    TextView welcomeText, reservationCountTxt, pendingReservation, restName;
    ImageView userAvatar, restImage;
    RecyclerView recyclerviewWeekStats;
    private ReservationStatisticsRecyclerViewAdapter adapter;
    private ImageView openReservationBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        findViews();
        initWelcomeLayout(welcomeText, userAvatar);
        initWeekStatsAdapter(recyclerviewWeekStats);
        IStatisticsService service = NetworkApi.getInstance().getRetrofit().create(IStatisticsService.class);
        IUserService userService = NetworkApi.getInstance().getRetrofit().create(IUserService.class);
        getAdminStatistic(service, userService);

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
                    public void onNext(@NonNull Object o) {
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
                    public void onError(@NonNull Throwable e) {
                        Log.e(AdminActivity.class.getName() + "onError", e.toString());
                    }

                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }
                });
    }

    private void updateRestaurantUI(Restaurant restaurant) {
        restName.setText(restaurant.getRestaurantName());
        Glide.with(getApplicationContext())
                .load(restaurant.getImageUrl())
                .apply(new RequestOptions().circleCrop())
                .error(R.drawable.ic_restaurant_white)
                .into(restImage);
    }

    private void updateUI(AdminStatisticsResponse statisticsResponse) {
        reservationCountTxt.setText(String.valueOf(statisticsResponse.getTodayReservation()));
        pendingReservation.setText(String.valueOf(statisticsResponse.getPendingReservation()));
        adapter.updateList(statisticsResponse.getReservationWeekList());

    }

    private void findViews() {
        recyclerviewWeekStats = findViewById(R.id.recyclerviewWeekStats);
        welcomeText = findViewById(R.id.welcomeText);
        userAvatar = findViewById(R.id.userAvatar);
        reservationCountTxt = findViewById(R.id.reservationCountTxt);
        pendingReservation = findViewById(R.id.pendingReservation);
        restName = findViewById(R.id.restNameTxt);
        restImage = findViewById(R.id.restImageView);
        openReservationBtn = findViewById(R.id.openReservationBtn);
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
        adapter = new ReservationStatisticsRecyclerViewAdapter(getApplicationContext(), new ArrayList<>());
        recyclerviewWeekStats.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerviewWeekStats.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.openReservationBtn) {

        }
    }
}