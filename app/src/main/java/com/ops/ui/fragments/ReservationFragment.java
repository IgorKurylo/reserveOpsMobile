package com.ops.ui.fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ops.R;
import com.ops.adapters.ReservationRecyclerViewAdapter;
import com.ops.models.Reserve;
import com.ops.models.ReserveStatus;
import com.ops.models.Role;
import com.ops.models.request.RequestReserve;
import com.ops.models.response.AdminMetaDataResponse;
import com.ops.models.response.BaseResponse;
import com.ops.models.response.ReserveResponse;
import com.ops.models.response.ReservesResponse;
import com.ops.network.NetworkApi;
import com.ops.network.services.IReserveService;
import com.ops.network.services.IUserService;
import com.ops.utils.CacheManager;
import com.ops.utils.Constant;
import com.ops.utils.UiUtils;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Reservation fragment for user and admin
 */
public class ReservationFragment extends Fragment implements View.OnClickListener, ReservationRecyclerViewAdapter.AdminActionListener {

    private ReservationRecyclerViewAdapter adapter;
    private LinearLayout calendarLayout;
    private RecyclerView reservationsRV;
    private TextView calendarTextView, noReservationTxt, reservationLbl;
    final Calendar calendar = Calendar.getInstance();
    private int todayDate;
    private View view;
    final String TAG = ReservationFragment.class.getName();
    private IUserService userService;
    private IReserveService reserveService;


    public static ReservationFragment newInstance() {
        return new ReservationFragment();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_reservation, container, false);
        reserveService = NetworkApi.getInstance().getRetrofit().create(IReserveService.class);
        userService = NetworkApi.getInstance().getRetrofit().create(IUserService.class);
        calendarLayout = view.findViewById(R.id.calendarLayout);
        reservationLbl = view.findViewById(R.id.reservationLbl);
        reservationsRV = view.findViewById(R.id.reservationRv);
        calendarTextView = view.findViewById(R.id.dateTxt);
        todayDate = UiUtils.initDateTextView(calendar, calendarTextView);
        noReservationTxt = view.findViewById(R.id.noReservation);
        calendarLayout.setOnClickListener(this);
        initReservationAdapter();
        String date = UiUtils.getTodayDate();
        if (Role.valueOf(CacheManager.getInstance().getString(Constant.ROLE)) == Role.Admin) {
            reservationLbl.setText(view.getContext().getString(R.string.adminReservation));
            calendarLayout.setVisibility(View.VISIBLE);
            initViewByRoleAdmin(date);
        } else {
            loadReservation(date, 0);
        }

        return view;
    }

    private void initViewByRoleAdmin(String date) {

        userService.metaData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseResponse<AdminMetaDataResponse>>() {
                    @Override
                    public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@io.reactivex.rxjava3.annotations.NonNull BaseResponse<AdminMetaDataResponse> adminMetaDataResponseBaseResponse) {
                        if (adminMetaDataResponseBaseResponse.isSuccess()) {
                            AdminMetaDataResponse response = (AdminMetaDataResponse) adminMetaDataResponseBaseResponse.getData();
                            loadReservation(date, response.getRestaurant().getId());
                        }
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        Log.e(TAG, e.toString());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void initReservationAdapter() {
        adapter = new ReservationRecyclerViewAdapter(new ArrayList<>(), view.getContext());
        reservationsRV.setLayoutManager(new LinearLayoutManager(view.getContext()));
        reservationsRV.setAdapter(adapter);
        adapter.setActionListener(this);
    }

    public DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            String dayText = dayOfMonth == todayDate ? "Today," : "";
            String monthDisplayTxt = UiUtils.getMonthName(month + 1);
            String chooseDate = String.format(Locale.getDefault(), "%s %d %s", dayText, dayOfMonth, monthDisplayTxt);
            calendarTextView.setText(chooseDate);
            SimpleDateFormat sdf = new SimpleDateFormat(Constant.DATE_FORMAT, Locale.getDefault());
            calendar.set(year, month, dayOfMonth);
            String reserveDate = sdf.format(calendar.getTime());
            initViewByRoleAdmin(reserveDate);
        }
    };

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.calendarLayout) {
            Calendar minDate = Calendar.getInstance();
            minDate.add(Calendar.DATE, -1);
            DatePickerDialog datePickerDialog = new DatePickerDialog(view.getContext(), dateSetListener, calendar
                    .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.getDatePicker().setMinDate(minDate.getTimeInMillis());
            datePickerDialog.show();
        }
    }

    private void loadReservation(String date, int restId) {
        reserveService.reserves(date, restId).enqueue(new Callback<BaseResponse<ReservesResponse>>() {
            @Override
            public void onResponse(@NotNull Call<BaseResponse<ReservesResponse>> call, @NotNull Response<BaseResponse<ReservesResponse>> response) {
                if (response.body() != null && response.body().isSuccess()) {
                    ReservesResponse reservesResponse = (ReservesResponse) response.body().getData();
                    if (reservesResponse.getReserveList().size() > 0) {
                        adapter.update(reservesResponse.getReserveList());
                        noReservationTxt.setVisibility(View.GONE);

                    } else {
                        adapter.update(new ArrayList<>());
                        noReservationTxt.setVisibility(View.VISIBLE);
                        reservationLbl.setVisibility(View.GONE);

                    }
                }
            }

            @Override
            public void onFailure(@NotNull Call<BaseResponse<ReservesResponse>> call, @NotNull Throwable t) {
                Log.e(TAG, t.getMessage());

            }
        });
    }

    @Override
    public void OnApprove(int reservationId) {
        reserveService.updateReservationStatus(new RequestReserve(new Reserve(reservationId, ReserveStatus.Approved.name())), reservationId).enqueue(new Callback<BaseResponse<ReserveResponse>>() {
            @Override
            public void onResponse(@NotNull Call<BaseResponse<ReserveResponse>> call, @NotNull Response<BaseResponse<ReserveResponse>> response) {
                if (response.body() != null) {
                    if (response.body().isSuccess()) {
                        adapter.setApproved(reservationId);
                    } else {
                        Toast.makeText(getContext(), view.getContext().getString(R.string.errorAction), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(@NotNull Call<BaseResponse<ReserveResponse>> call, @NotNull Throwable t) {
                Toast.makeText(getContext(), view.getContext().getString(R.string.errorAction), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void OnDecline(int reservationId) {
        reserveService.updateReservationStatus(new RequestReserve(new Reserve(reservationId, ReserveStatus.Decline.name())), reservationId).enqueue(new Callback<BaseResponse<ReserveResponse>>() {
            @Override
            public void onResponse(@NotNull Call<BaseResponse<ReserveResponse>> call, @NotNull Response<BaseResponse<ReserveResponse>> response) {
                if (response.body() != null) {
                    if (response.body().isSuccess()) {
                        adapter.setDecline(reservationId);
                    } else {
                        Toast.makeText(getContext(), view.getContext().getString(R.string.errorAction), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(@NotNull Call<BaseResponse<ReserveResponse>> call, @NotNull Throwable t) {
                Toast.makeText(getContext(), view.getContext().getString(R.string.errorAction), Toast.LENGTH_LONG).show();
            }
        });
    }
}