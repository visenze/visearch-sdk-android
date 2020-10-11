package com.visenze.visearch.android.http;

import android.content.Context;
import android.net.Uri;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.visenze.visearch.android.ViSearch;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class HttpInstance {
    /**
     * HTTP CONSTANT for Multipart Entity Uploading
     */
    public static final int TIME_OUT_FOR_UPLOAD = 10000;
    public static final int TIME_OUT_FOR_ID = 5000;

    /**
     * http instance
     */
    private static HttpInstance     mInstance;

    /**
     * application context
     */
    private static Context          mContext;

    /**
     * api access key and secret key
     */
    private String                  accessKey;
    private String                  secretKey;

    private String                  userAgent;

    /**
     * request queue
     */
    private RequestQueue            mRequestQueue;

    /**
     * private constructor
     *
     * @param context application context
     */
    private HttpInstance(Context context) {
        CookieManager manager = new CookieManager();
        CookieHandler.setDefault(manager);
        mContext = context;
        mRequestQueue = getRequestQueue();
    }

    /**
     * singleton constructor
     *
     * @param context application context
     *
     * @return singleton instance
     */
    public static synchronized HttpInstance getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new HttpInstance(context);
        }

        return mInstance;
    }

    public void setKeys(String accessKey, String secretKey) {
        this.accessKey = accessKey;
        this.secretKey = secretKey;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    /**
     * request queue getter
     *
     * @return requestQueue
     */
    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(mContext.getApplicationContext());
        }

        return mRequestQueue;
    }

    /**
     * start a new request by passing the url, params and result listener
     *
     * @param url url to call
     * @param params parameters
     * @param type search type
     * @param resultListener result listener
     */
    public void addGetRequestToQueue(
            final String url,
            Map<String, List<String>> params,
            String type,
            final ViSearch.ResultListener resultListener) {

        ResponseListener responseListener = new ResponseListener(resultListener, type);

        if (null == params)
            params = new HashMap<String, List<String> >();

        Uri.Builder uri = new Uri.Builder();
        for (Map.Entry<String, List<String> > entry : params.entrySet()) {
            for (String s: entry.getValue())
                uri.appendQueryParameter(entry.getKey(), s);
        }

        // add key
        if (secretKey == null)
            uri.appendQueryParameter("access_key", accessKey);

        JsonWithHeaderRequest jsonObjectRequest = new JsonWithHeaderRequest(
                Request.Method.GET, url + uri.toString(), null,
                accessKey, secretKey, userAgent,
                responseListener,
                new ResponseErrorListener(resultListener));

        jsonObjectRequest.setTag(mContext);
        getRequestQueue().add(jsonObjectRequest);
    }

    /**
     * start a request by add multipart parameters
     *
     * @param url url to call
     * @param params parameters
     * @param bytes byte array
     * @param resultListener result listener
     */
    public void addMultipartRequestToQueue (
            final String url,
            Map<String, List<String> > params, Charset charset,
            byte[] bytes,
            final ViSearch.ResultListener resultListener) {

        ResponseListener responseListener = new ResponseListener(resultListener, "uploadsearch");

        if (null == params)
            params = new HashMap<String, List<String> >();

        MultiPartRequest multipartRequest = new MultiPartRequest(Request.Method.POST, url,
                params, charset, bytes,
                accessKey, secretKey, userAgent,
                responseListener,
                new ResponseErrorListener(resultListener));

        multipartRequest.setTag(mContext);
        getRequestQueue().add(multipartRequest);
    }

    /**
     * cancel request
     *
     * @param resultListener callback listener
     */
    public void cancelRequest(ViSearch.ResultListener resultListener) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(mContext);

            // clear all requests
            mRequestQueue.cancelAll(new RequestQueue.RequestFilter() {
                @Override
                public boolean apply(Request<?> request) {
                    return true;
                }
            });

            if (null != resultListener)
                resultListener.onSearchCanceled();
        }
    }
}
