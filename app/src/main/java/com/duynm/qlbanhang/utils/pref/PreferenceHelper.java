package com.duynm.qlbanhang.utils.pref;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by DuyNM on 26/12/2019
 */
public class PreferenceHelper {

    private SharedPreferences sharedPreferences;
    private Context context;

    public PreferenceHelper(Context context, String name) {
        if (context == null) {
            return;
        }

        sharedPreferences = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        this.context = context;
    }

    public String getStoreName() {
        return sharedPreferences.getString(PrefKeys.STORE_NAME, "");
    }

    public void setStoreName(String newValue) {
        if (sharedPreferences == null) return;
        sharedPreferences.edit().putString(PrefKeys.STORE_NAME, newValue).apply();
    }

    public String getStorePhone() {
        return sharedPreferences.getString(PrefKeys.STORE_PHONE, "");
    }

    public void setStorePhone(String newValue) {
        if (sharedPreferences == null) return;
        sharedPreferences.edit().putString(PrefKeys.STORE_PHONE, newValue).apply();
    }

    public String getStoreAddress() {
        return sharedPreferences.getString(PrefKeys.STORE_ADDRESS, "");
    }

    public void setStoreAddress(String newValue) {
        if (sharedPreferences == null) return;
        sharedPreferences.edit().putString(PrefKeys.STORE_ADDRESS, newValue).apply();
    }
}
