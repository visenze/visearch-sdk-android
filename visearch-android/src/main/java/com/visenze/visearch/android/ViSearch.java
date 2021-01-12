package com.visenze.visearch.android;

import android.content.Context;
import android.util.Log;

import com.visenze.datatracking.SessionManager;
import com.visenze.datatracking.Tracker;
import com.visenze.datatracking.VisenzeAnalytics;
import com.visenze.datatracking.data.DataCollection;
import com.visenze.visearch.android.api.SearchOperations;
import com.visenze.visearch.android.api.impl.SearchOperationsImpRetrofit;

import java.net.URL;

/**
 * ViSearch singleton handles all the search methods.
 *
 * ViSearch should be initialised by Builder with a valid API access/secret key pair before it can be used.
 */
public class ViSearch {

    private static final String USER_AGENT = "visearch-android-sdk";
    private static final String SEARCH_URL = "https://visearch.visenze.com";

    private SearchOperations searchOperations;

    private ResultListener mListener;

    private String uid;

    private VisenzeAnalytics visenzeAnalytics;

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
                     String userAgent,
                     String uid) {

        this.searchOperations = new SearchOperationsImpRetrofit(
                searchApiEndPoint, accessKey, secretKey, userAgent
        );

        this.uid = uid;
        this.visenzeAnalytics = VisenzeAnalytics.getInstance(context.getApplicationContext(), uid);

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
        addAnalyticsParams(idSearchParams);

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
        addAnalyticsParams(idSearchParams);

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
        addAnalyticsParams(colorSearchParams);

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
        addAnalyticsParams(uploadSearchParams);

        try {
            searchOperations.uploadSearch(uploadSearchParams, mListener);
        } catch (ViSearchException e) {
            Log.e("ViSearch SDK", e.getMessage());
        }
    }

    public void discoverSearch(final UploadSearchParams uploadSearchParams) {
        addAnalyticsParams(uploadSearchParams);

        try {
            searchOperations.discoverSearch(uploadSearchParams, mListener);
        } catch (ViSearchException e) {
            Log.e("ViSearch SDK", e.getMessage());
        }
    }

    // tracking related
    public Tracker newTracker(String code, boolean useCnEndpoint) {
        return visenzeAnalytics.newTracker(code, useCnEndpoint);
    }

    private void addAnalyticsParams(SearchParams searchParams) {
        if (searchParams == null) return;

        SessionManager sessionManager = visenzeAnalytics.getSessionManager();
        DataCollection dataCollection = visenzeAnalytics.getDataCollection();

        if (searchParams.getUid() == null) {
            searchParams.setUid(sessionManager.getUid());
        }

        if (searchParams.getSid() == null) {
            searchParams.setSid(sessionManager.getSessionId());
        }

        if (searchParams.getAppId() == null) {
            searchParams.setAppId(dataCollection.getAppId());
        }

        if (searchParams.getAppName() == null) {
            searchParams.setAppName(dataCollection.getAppName());
        }

        if (searchParams.getAppVersion() == null) {
            searchParams.setAppVersion(dataCollection.getAppVersion());
        }

        if (searchParams.getDeviceBrand() == null) {
            searchParams.setDeviceBrand(dataCollection.getDeviceBrand());
        }

        if (searchParams.getDeviceModel() == null) {
            searchParams.setDeviceModel(dataCollection.getDeviceModel());
        }

        if (searchParams.getLanguage() == null) {
            searchParams.setLanguage(dataCollection.getLanguage());
        }

        if (searchParams.getOs() == null) {
            searchParams.setOs(dataCollection.getOs());
        }

        if (searchParams.getOsv() == null) {
            searchParams.setOsv(dataCollection.getOsv());
        }

        if (searchParams.getPlatform() == null) {
            searchParams.setPlatform(dataCollection.getPlatform());
        }

    }

    /**
     * Builder class for {@link ViSearch}
     */
    public static class Builder {
        private String mAppKey;
        private String mSecretKey;
        private String searchApiEndPoint;
        private String userAgent;
        private String uid;

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

        public Builder setUid(String uid) {
            this.uid = uid;
            return this;
        }

        public ViSearch build(Context context) {

            ViSearch viSearch = new ViSearch(context,
                    mAppKey, mSecretKey,
                    searchApiEndPoint,
                    userAgent, uid);

            return viSearch;
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
