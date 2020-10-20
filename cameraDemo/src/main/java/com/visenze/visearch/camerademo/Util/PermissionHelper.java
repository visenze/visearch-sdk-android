package com.visenze.visearch.camerademo.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

/**
 * Created by msomu on 18/01/16.
 */
public class PermissionHelper {
    public static final int CAMERA_PERMISSION_REQ = 101;
    public static final int WRITE_EXTERNAL_PERMISSION_REQ = 102;

    /**
     * Check if the app has access to camera permission. On pre-M
     * devices this will always return true.
     */
    public static boolean checkCameraPermission(Context context) {
        return PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(
                context, Manifest.permission.CAMERA);
    }

    public static boolean checkWriteExternal(Context context) {
        return PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(
                context, Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    public static boolean checkPhoneState(Context context) {
        return PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(
                context, Manifest.permission.READ_PHONE_STATE);
    }

    public static void requestCameraPermission(Activity activity) {
        ActivityCompat.requestPermissions(activity,
                new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, CAMERA_PERMISSION_REQ);
    }
}
