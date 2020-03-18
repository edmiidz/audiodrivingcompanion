package com.altever.audiodrivingcompanion.database;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefUtil {
    static String SHARED_PREF = "SP_STATUS_MODE";

    public static void setStatusMode(Context context, String key, String value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String getStatusMode(Context context, String key, String defaultVal) {
        String prefVal = defaultVal;
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF, 0);
        prefVal = sharedPreferences.getString(key, defaultVal);
        return prefVal;
    }
}
