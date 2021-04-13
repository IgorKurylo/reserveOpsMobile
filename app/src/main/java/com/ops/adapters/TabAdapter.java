package com.ops.adapters;

import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;

public class TabAdapter extends FragmentPagerAdapter {
    private Context mContext;
    private List<Fragment> fragments;
    private String[] titles;

    public TabAdapter(@NonNull FragmentManager fm, int behavior, Context context, List<Fragment> fragmentList, String[] titles) {
        super(fm, behavior);
        mContext = context;
        fragments = fragmentList;
        this.titles = titles;
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return this.titles[position];
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return this.fragments.get(position);
    }
}
