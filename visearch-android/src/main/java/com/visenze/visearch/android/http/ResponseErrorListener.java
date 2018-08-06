package com.visenze.visearch.android.http;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.visenze.visearch.android.ViSearch;

public class ResponseErrorListener implements Response.ErrorListener {
    private ViSearch.ResultListener visearchResultListener;

    public ResponseErrorListener(ViSearch.ResultListener visearchResultListener) {
        this.visearchResultListener = visearchResultListener;
    }

    @Override
    public void onErrorResponse(VolleyError volleyError) {
        volleyError.printStackTrace();
        if (null != visearchResultListener)
            visearchResultListener.onSearchError("Network Error");
    }
}
