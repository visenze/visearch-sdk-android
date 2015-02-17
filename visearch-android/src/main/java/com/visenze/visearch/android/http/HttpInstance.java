package com.visenze.visearch.android.http;

import android.content.Context;
import android.net.Uri;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.visenze.visearch.android.ViSearch;
import com.visenze.visearch.android.util.AuthGenerator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class HttpInstance {
    /**
     * HTTP CONSTANT for Multipart Entity Uploading
     */
    public static final int TIME_OUT_FOR_UPLOAD = 10000;

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

    /**
     * request queue
     */
    private RequestQueue            mRequestQueue;

    /**
     * private constructor
     *
     * @param context application context
     * @param accessKey access key
     * @param secretKey secret key
     */
    private HttpInstance(Context context, String accessKey, String secretKey) {
        mContext = context;
        mRequestQueue = getRequestQueue();
        this.accessKey = accessKey;
        this.secretKey = secretKey;
    }

    /**
     * singleton constructor
     *
     * @param context application context
     * @param accessKey access key
     * @param secretKey secret key
     *
     * @return singleton instance
     */
    public static synchronized HttpInstance getInstance(Context context, String accessKey, String secretKey) {
        if (mInstance == null) {
            mInstance = new HttpInstance(context, accessKey, secretKey);
        }

        return mInstance;
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
     * @param resultListener result listener
     */
    public void addGetRequestToQueue(
            final String url,
            Map<String, List<String>> params,
            final ViSearch.ResultListener resultListener) {

        ResponseListener responseListener = new ResponseListener(resultListener);

        if (null == params)
            params = new HashMap<String, List<String> >();

        putValueMapInMap(params, AuthGenerator.getAuthParam(accessKey, secretKey));

        Uri.Builder uri = new Uri.Builder();
        for (Map.Entry<String, List<String> > entry : params.entrySet()) {
            for (String s: entry.getValue())
                uri.appendQueryParameter(entry.getKey(), s);
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url + uri.toString(), null,
                responseListener,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        volleyError.printStackTrace();
                        if (null != resultListener)
                            resultListener.onSearchError("Network Error");
                    }
                });

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
    public void addMultipartRequestToQueue(
            final String url,
            Map<String, List<String> > params,
            byte[] bytes,
            final ViSearch.ResultListener resultListener) {

        ResponseListener responseListener = new ResponseListener(resultListener);

        if (null == params)
            params = new HashMap<String, List<String> >();

        putValueMapInMap(params, AuthGenerator.getAuthParam(accessKey, secretKey));

        MultiPartRequest multipartRequest = new MultiPartRequest(Request.Method.POST, url,
                params, bytes,
                responseListener,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        volleyError.printStackTrace();
                        if (null != resultListener)
                            resultListener.onSearchError("Network Error");
                    }
                });

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
            if (null != resultListener)
                resultListener.onSearchCanceled();
        }
    }

    private void putValueMapInMap(Map<String, List<String> > map, Map<String, String> valueMap) {
        for (Map.Entry<String, String> set : valueMap.entrySet()) {
            List<String> stringList = new ArrayList<>();
            stringList.add(set.getValue());
            map.put(set.getKey(), stringList);
        }
    }
}
