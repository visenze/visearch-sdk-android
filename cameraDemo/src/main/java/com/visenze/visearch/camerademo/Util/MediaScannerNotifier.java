package com.visenze.visearch.camerademo.util;

import android.content.Context;
import android.media.MediaScannerConnection;
import android.net.Uri;

/**
 * media scanner notifier, called to update the photo gallery when photo is taken and saved
 * in local path
 * Created by yulu on 3/3/15.
 */
public class MediaScannerNotifier implements MediaScannerConnection.MediaScannerConnectionClient {
    private Context mContext;
    private MediaScannerConnection mConnection;
    private String mPath;
    private String mMimeType;


    public MediaScannerNotifier(Context context, String path, String mimeType) {
        mContext = context;
        mPath = path;
        mMimeType = mimeType;
        mConnection = new MediaScannerConnection(context, this);
        mConnection.connect();
    }


    public void onMediaScannerConnected() {
        mConnection.scanFile(mPath, mMimeType);
    }


    public void onScanCompleted(String path, Uri uri) {
        mConnection.disconnect();
        mContext = null;
    }
}
