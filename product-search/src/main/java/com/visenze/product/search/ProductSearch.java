package com.visenze.product.search;

import android.content.Context;

import com.visenze.datatracking.SessionManager;
import com.visenze.datatracking.VisenzeAnalytics;
import com.visenze.datatracking.data.DataCollection;

import java.net.MalformedURLException;
import java.net.URL;

public class ProductSearch {
    private static final String USER_AGENT = "productsearch-android-sdk";
    private static final String SEARCH_URL = "https://visearch.visenze.com";

    private String uid;
    private VisenzeAnalytics visenzeAnalytics;

    private ProductSearch(Context context, String appKey, String placement, String endPoint, String userAgent, String uid) {

        this.uid = uid;
        this.visenzeAnalytics = VisenzeAnalytics.getInstance(context.getApplicationContext(), uid);
    }




    private void addAnalyticsParams(BaseSearchParams searchParams) {
        if (searchParams == null) return;

        SessionManager sessionManager = visenzeAnalytics.getSessionManager();
        DataCollection dataCollection = visenzeAnalytics.getDataCollection();

//        if (searchParams.getUid() == null) {
//            searchParams.setUid(sessionManager.getUid());
//        }
//
//        if (searchParams.getSid() == null) {
//            searchParams.setSid(sessionManager.getSessionId());
//        }
//
//        if (searchParams.getAppId() == null) {
//            searchParams.setAppId(dataCollection.getAppId());
//        }
//
//        if (searchParams.getAppName() == null) {
//            searchParams.setAppName(dataCollection.getAppName());
//        }
//
//        if (searchParams.getAppVersion() == null) {
//            searchParams.setAppVersion(dataCollection.getAppVersion());
//        }
//
//        if (searchParams.getDeviceBrand() == null) {
//            searchParams.setDeviceBrand(dataCollection.getDeviceBrand());
//        }
//
//        if (searchParams.getDeviceModel() == null) {
//            searchParams.setDeviceModel(dataCollection.getDeviceModel());
//        }
//
//        if (searchParams.getLanguage() == null) {
//            searchParams.setLanguage(dataCollection.getLanguage());
//        }
//
//        if (searchParams.getOs() == null) {
//            searchParams.setOs(dataCollection.getOs());
//        }
//
//        if (searchParams.getOsv() == null) {
//            searchParams.setOsv(dataCollection.getOsv());
//        }
//
//        if (searchParams.getPlatform() == null) {
//            searchParams.setPlatform(dataCollection.getPlatform());
//        }

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

}
