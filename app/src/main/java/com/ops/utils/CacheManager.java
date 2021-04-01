package com.ops.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.ops.R;

public class CacheManager {

    private static CacheManager instance;
    private Context mContext;
    private static SharedPreferences cache;
    private static SharedPreferences.Editor editor;
    public static CacheManager getInstance() {
        if (instance == null) {
            instance = new CacheManager();
        }
        return instance;
    }

    public void Init(Context context) {
        mContext = context;
        cache = context.getSharedPreferences(context.getResources().getString(R.string.shared_preference_file_key), Context.MODE_PRIVATE);
    }
    public  void setString(String key, String value) {
        editor = cache.edit();
        editor.putString(key, value);
        editor.apply();
    }
    public  String getString(String key) {
        return cache.getString(key, "");
    }
    public void clean() {
        editor = cache.edit();
        editor.clear();
        editor.apply();
    }
}
