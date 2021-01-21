package com.visenze.product.search;

import android.content.Context;

import com.visenze.datatracking.SessionManager;
import com.visenze.datatracking.VisenzeAnalytics;
import com.visenze.datatracking.data.DataCollection;
import com.visenze.product.search.model.ResponseData;
import com.visenze.product.search.network.SearchService;

import java.net.MalformedURLException;
import java.net.URL;

public class ProductSearch {
    private static final String USER_AGENT = "productsearch-android-sdk";
    private static final String SEARCH_URL = "https://search-dev.visenze.com/v1";
//    private static final String SEARCH_URL = "https://search.visenze.com/v1";


    private String uid;
    private VisenzeAnalytics visenzeAnalytics;
    private SearchService searchService;

    private ProductSearch(Context context, String appKey, String placementId, String endPoint, String userAgent, String uid) {

        this.uid = uid;
        this.visenzeAnalytics = VisenzeAnalytics.getInstance(context.getApplicationContext(), uid);
        this.searchService = new SearchService(endPoint, appKey, placementId, userAgent);
    }


    public void similarSearch(ImageSearchParams imageSearchParams, ResultListener listener) {
        addAnalyticsParams(imageSearchParams);
        searchService.similarSearch(imageSearchParams, listener);
    }


    private void addAnalyticsParams(BaseSearchParams searchParams) {
        if (searchParams == null) return;

        SessionManager sessionManager = visenzeAnalytics.getSessionManager();
        DataCollection dataCollection = visenzeAnalytics.getDataCollection();



        if (searchParams.getVaUid() == null) {
            searchParams.setVaUid(sessionManager.getUid());
        }

        if (searchParams.getVaSid() == null) {
            searchParams.setVaSid(sessionManager.getSessionId());
        }

        if (searchParams.getVaAppBundleId() == null) {
            searchParams.setVaAppBundleId(dataCollection.getAppId());
        }

        if (searchParams.getVaAppName() == null) {
            searchParams.setVaAppName(dataCollection.getAppName());
        }

        if (searchParams.getVaAppVersion() == null) {
            searchParams.setVaAppVersion(dataCollection.getAppVersion());
        }

        if (searchParams.getVaDeviceBrand() == null) {
            searchParams.setVaDeviceBrand(dataCollection.getDeviceBrand());
        }

        if (searchParams.getVaDeviceModel() == null) {
            searchParams.setVaDeviceModel(dataCollection.getDeviceModel());
        }

        if (searchParams.getVaOs() == null) {
            searchParams.setVaOs(dataCollection.getOs());
        }

        if (searchParams.getVaOsv() == null) {
            searchParams.setVaOsv(dataCollection.getOsv());
        }
    }



    public static class Builder {
        private String mAppKey;
        private String mPlacement;
        private String searchApiEndPoint;
        private String userAgent;
        private String uid;

        public Builder(String appKey, String placement) {
            mAppKey = appKey;
            mPlacement = placement;
            searchApiEndPoint = SEARCH_URL;
            userAgent = USER_AGENT + "/" + BuildConfig.VERSION_NAME;
        }

        public Builder(URL endPoint, String appKey, String placement) {
            this(appKey, placement);
            searchApiEndPoint = endPoint.toString();
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

        public ProductSearch build(Context context) {

            ProductSearch viSearch = new ProductSearch(context,
                    mAppKey, mPlacement,
                    searchApiEndPoint,
                    userAgent, uid);

            return viSearch;
        }
    }

    public static interface ResultListener {
        public void onSearchResult(final ResponseData response, String errorMsg);

    }

}
