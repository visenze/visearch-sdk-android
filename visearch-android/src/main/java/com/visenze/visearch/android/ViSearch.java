package com.visenze.visearch.android;

import android.content.Context;
import android.util.Log;

import com.visenze.visearch.android.api.impl.SearchOperationsImpl;
import com.visenze.visearch.android.api.impl.TrackOperationsImpl;
import com.visenze.visearch.android.util.ViSearchUIDManager;

import java.net.URL;

/**
 * ViSearch singleton handles all the search methods.
 *
 * ViSearch should be initialised by Builder with a valid API access/secret key pair before it can be used.
 */
public class ViSearch {

    private static final String USER_AGENT = "visearch-android-sdk";
    private static final String SEARCH_URL = "https://visearch.visenze.com";

    private SearchOperationsImpl searchOperations;

    private TrackOperationsImpl trackOperations;

    private ResultListener mListener;

    private String uid;

    /**
     * Initialise the ViSearcher with a valid access/secret key pair
     *
     * @param context Activity context
     * @param accessKey the App Key or the Access Key
     * @param secretKey the Secret Key
     */
    private ViSearch(Context context,
                     String accessKey,
                     String secretKey,
                     String searchApiEndPoint,
                     String userAgent) {

        initTracking(context.getApplicationContext());
        searchOperations = new SearchOperationsImpl(
                searchApiEndPoint,
                context,
                accessKey, secretKey, userAgent);
        trackOperations = new TrackOperationsImpl(context, accessKey);
    }

    /**
     * Sets the {@link ViSearch ResultListener} to be notified of the search result
     *
     * @param listener the ViSearcher.ResultListener to notify.
     */
    public void setListener(ResultListener listener) {
        mListener = listener;
    }


    /**
     * Cancel the search
     */
    public void cancelSearch() {
        searchOperations.cancelSearch(mListener);
    }

    /**
     * Start search session with index parameters: search by provide the image id
     *
     * @param idSearchParams index parameters.
     */
    public void idSearch(final IdSearchParams idSearchParams) {
        try {
            searchOperations.search(idSearchParams, mListener);
        } catch (ViSearchException e) {
            Log.e("ViSearch SDK", e.getMessage());
        }
    }

    /**
     * Start search session with index parameters: search by provide the image id
     *
     * @param idSearchParams index parameters.
     */
    public void recommendation(final IdSearchParams idSearchParams) {
        try {
            searchOperations.recommendation(idSearchParams, mListener);
        } catch (ViSearchException e) {
            Log.e("ViSearch SDK", e.getMessage());
        }
    }

    /**
     * Start search session with color parameters: search by providing a color code in hexadecimal
     *
     * @param colorSearchParams color parameters.
     */
    public void colorSearch(final ColorSearchParams colorSearchParams) {
        try {
            searchOperations.colorSearch(colorSearchParams, mListener);
        } catch (ViSearchException e) {
            Log.e("ViSearch SDK", e.getMessage());
        }
    }

    /**
     * Start search session with upload parameters: search by uploading a image or using an image url
     *
     * @param uploadSearchParams upload parameters
     */
    public void uploadSearch(final UploadSearchParams uploadSearchParams) {
        try {
            searchOperations.uploadSearch(uploadSearchParams, mListener);
        } catch (ViSearchException e) {
            Log.e("ViSearch SDK", e.getMessage());
        }
    }

    public void track(final TrackParams trackParams) {
        try {
            trackOperations.track(trackParams);
        } catch (ViSearchException e) {
            Log.e("ViSearch SDK", e.getMessage());
        }
    }

    private void initTracking(final Context context) {
        ViSearchUIDManager.getAdvertisingId(context);
    }

    /**
     * Builder class for {@link ViSearch}
     */
    public static class Builder {
        private String mAppKey;
        private String mSecretKey;
        private String searchApiEndPoint;
        private String userAgent;

        public Builder(String appKey) {
            mAppKey = appKey;
            searchApiEndPoint = SEARCH_URL;
            userAgent = USER_AGENT + "/" + BuildConfig.VERSION_NAME;
        }

        public Builder(URL endPoint, String appKey) {
            if (endPoint == null)
                throw new ViSearchException("Endpoint is not specified");

            searchApiEndPoint = endPoint.toString();
            mAppKey = appKey;
            userAgent = USER_AGENT + "/" + BuildConfig.VERSION_NAME;
        }

        public Builder(String accessKey, String secretKey) {
            mAppKey = accessKey;
            mSecretKey = secretKey;
            searchApiEndPoint = SEARCH_URL;
            userAgent = USER_AGENT + "/" + BuildConfig.VERSION_NAME;
        }

        public Builder(URL endPoint, String accessKey, String secretKey) {
            if (endPoint == null)
                throw new ViSearchException("Endpoint is not specified");

            searchApiEndPoint = endPoint.toString();
            mAppKey = accessKey;
            mSecretKey = secretKey;
            userAgent = USER_AGENT + "/" + BuildConfig.VERSION_NAME;
        }

        public Builder setApiEndPoint(URL endPoint) {
            searchApiEndPoint = endPoint.toString();
            return this;
        }

        public Builder setUserAgent(String userAgent) {
            this.userAgent = userAgent;
            return this;
        }

        public ViSearch build(Context context) {

            return new ViSearch(context,
                    mAppKey, mSecretKey,
                    searchApiEndPoint,
                    userAgent);
        }
    }

    /**
     * Interface to be notified of results and potential errors
     */
    public static interface ResultListener {

        /**
         * Called after a search session is started and a valid result is returned
         *
         * @param resultList the result list if any, null otherwise.
         */
        public abstract void onSearchResult(final ResultList resultList);

        /**
         * Called after a search session is started and an error occurs
         *
         * @param errorMessage the error message if error occurs.
         */
        public abstract void onSearchError(String errorMessage);

        /**
         * Called when cancelSearch is called
         */
        public abstract void onSearchCanceled();

    }

}
