package com.ops.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.tabs.TabLayout;
import com.ops.R;
import com.ops.adapters.ReservationStatisticsRecyclerViewAdapter;
import com.ops.adapters.TabAdapter;
import com.ops.models.Restaurant;
import com.ops.models.response.AdminMetaDataResponse;
import com.ops.models.response.AdminStatisticsResponse;
import com.ops.models.response.BaseResponse;
import com.ops.network.NetworkApi;
import com.ops.network.services.IStatisticsService;
import com.ops.network.services.IUserService;
import com.ops.ui.fragments.AdminDashboardFragment;
import com.ops.ui.fragments.ReservationFragment;
import com.ops.utils.CacheManager;
import com.ops.utils.Constant;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.ObservableSource;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.schedulers.Schedulers;

/**
 * Admin activity which holding view pager,tab adapter
 */
public class AdminActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {

    ViewPager viewPager;
    List<Fragment> fragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        TabLayout tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
        String[] tabsTitles = getResources().getStringArray(R.array.tabTitles);
        fragments = new ArrayList<>();
        fragments.add(AdminDashboardFragment.newInstance());
        fragments.add(ReservationFragment.newInstance());
        TabAdapter adapter = new TabAdapter(getSupportFragmentManager(), 0, getApplicationContext(), fragments, tabsTitles);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getString(R.string.adminActivity));
        }
    }

    /**
     * Invoke from admin fragment
     */
    public void showReservationTab() {
        viewPager.setCurrentItem(1);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        Log.i(AdminActivity.class.getName(), fragments.get(position).toString());
    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this).setTitle(getString(R.string.app_name)).setMessage(R.string.message_alert)
                .setPositiveButton(getString(R.string.positiveAnswer), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finishAffinity();
                        System.exit(0);
                    }
                })
                .setNegativeButton(getString(R.string.negativeAnswer), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).show();

    }
}