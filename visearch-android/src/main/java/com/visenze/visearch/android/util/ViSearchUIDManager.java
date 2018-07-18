package com.visenze.visearch.android.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by visenze on 26/2/16.
 */
public class ViSearchUIDManager {
    public final static String PREF_KEY = "visearchudid";
    public final static String PREFS_NAME = "visearchuid_prefs";
    public static Context context;

    private static SharedPreferences preference;

    public static void initUIDManager(final Context context) {
        preference =  context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public static String getUid() {
        return preference.getString(PREF_KEY, null);
    }

    public static void updateUidFromCookie(String uid) {
        if (preference.getString(PREF_KEY, null) == null) {
            final SharedPreferences.Editor e = preference.edit();
            e.putString(PREF_KEY, uid);
            e.apply();
        }
    }
}
