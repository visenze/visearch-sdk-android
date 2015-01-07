package com.visenze.visearch.android;

import android.content.Context;
import com.visenze.visearch.android.api.impl.SearchOperationsImpl;

/**
 * ViSearch singleton handles all the search methods.
 *
 * ViSearch should be initialised by Builder with a valid API access/secret key pair before it can be used.
 */
public class ViSearch {

    private SearchOperationsImpl searchOperations;

    private ResultListener mListener;

    /**
     * Initialise the ViSearcher with a valid access/secret key pair
     *
     * @param context Activity context
     * @param accessKey the Access Key
     * @param secretKey the Secret Key
     */
    private ViSearch(Context context,
                       String accessKey, String secretKey,
                       String searchApiHost, String searchApiPort) {

        searchOperations = new SearchOperationsImpl(
                searchApiHost +":"+ searchApiPort,
                context,
                accessKey, secretKey);
    }

    /**
     * Sets the {@link com.visenze.visearch.android.ViSearch ResultListener} to be notified of the search result
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
            e.printStackTrace();
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
            e.printStackTrace();
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
            e.printStackTrace();
        }
    }

    /**
     * Builder class for {@link com.visenze.visearch.android.ViSearch}
     */
    public static class Builder {
        private String mAccessKey;
        private String mSecretKey;
        private String searchApiHost;
        private String searchApiPort;

        public Builder(String accessKey, String secretKey) {
            mAccessKey = accessKey;
            mSecretKey = secretKey;
            searchApiHost = "http://visearch.visenze.com";
            searchApiPort = "80";
        }

        public Builder setApiHost(String host) {
            searchApiHost = host;
            return this;
        }

        public Builder setApiPort(String port) {
            searchApiPort = port;
            return this;
        }

        public ViSearch build(Context context) {
            ViSearch viSearch = new ViSearch(context,
                    mAccessKey,
                    mSecretKey,
                    searchApiHost,
                    searchApiPort);

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
