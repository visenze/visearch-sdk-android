package com.visenze.visearch.android.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;

import java.io.IOException;

/**
 * Created by visenze on 26/2/16.
 */
public class ViSearchUIDManager {
    public final static String PREF_KEY = "visearchudid";
    public final static String PREFS_NAME = "visearchuid_prefs";
    public static Context context;

    private static SharedPreferences preference;

    public static void getAdvertisingId(final Context context) {
        preference =  context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        AsyncTask<Void, Void, String> task = new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                AdvertisingIdClient.Info idInfo = null;
                try {
                    idInfo = AdvertisingIdClient.getAdvertisingIdInfo(context);
                } catch (GooglePlayServicesNotAvailableException | GooglePlayServicesRepairableException e) {
                    Log.w("UID MANAGER", e);
                } catch (IOException e) {
                    Log.w("UID MANAGER", "fail to get google advertising id");
                }
                String advertId = null;

                if (idInfo != null)
                    advertId = idInfo.getId();

                return advertId;
            }

            @Override
            protected void onPostExecute(String advertId) {
                if (advertId != null) {
                    final SharedPreferences.Editor e = preference.edit();
                    e.putString(PREF_KEY, advertId);
                    e.apply();
                }
            }
        };
        task.execute();
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
