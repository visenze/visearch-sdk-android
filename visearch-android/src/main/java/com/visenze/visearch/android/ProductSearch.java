package com.visenze.visearch.android;

import android.content.Context;

import com.visenze.datatracking.SessionManager;
import com.visenze.datatracking.Tracker;
import com.visenze.datatracking.VisenzeAnalytics;
import com.visenze.datatracking.data.DataCollection;
import com.visenze.visearch.android.model.ErrorData;
import com.visenze.visearch.android.model.ProductResponse;
import com.visenze.visearch.android.network.ProductSearchService;

import java.net.URL;

public class ProductSearch {
    private static final String USER_AGENT = "productsearch-android-sdk";
    private static final String SEARCH_URL = "https://search.visenze.com/v1/";

    private String uid;
    private String trackCode;
    private VisenzeAnalytics visenzeAnalytics;
    private ProductSearchService productSearchService;

    private ProductSearch(Context context, String appKey, int placementId, String endPoint, String userAgent, String uid) {

        this.uid = uid;
        this.trackCode = appKey + ":" + placementId;
        this.visenzeAnalytics = VisenzeAnalytics.getInstance(context.getApplicationContext(), uid);
        this.productSearchService = new ProductSearchService(endPoint, appKey, placementId, userAgent);
    }

    public void searchByImage(ProductSearchByImageParams imageSearchParams, ResultListener listener) {
        addAnalyticsParams(imageSearchParams);
        productSearchService.searchByImage(imageSearchParams, listener);
    }

    public void searchById(ProductSearchByIdParams visualSimilarParams, ResultListener listener) {
        addAnalyticsParams(visualSimilarParams);
        productSearchService.searchById(visualSimilarParams, listener);
    }

    public void recommendations(ProductSearchByIdParams visualSimilarParams, ResultListener listener) {
        searchById(visualSimilarParams, listener);
    }

    // get auto-generated uid string (va_uid) for Analytics purpose
    public String getUid() {
        return visenzeAnalytics.getSessionManager().getUid();
    }

    // get auto-generated session ID string (va_sid) for Analytics purpose
    public String getSid() {
        return visenzeAnalytics.getSessionManager().getUid();
    }

    private void addAnalyticsParams(BaseProductSearchParams searchParams) {
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

    // tracking related
    public Tracker newTracker(String code, boolean useCnEndpoint) {
        if(code == null) code = trackCode;
        return visenzeAnalytics.newTracker(code, useCnEndpoint);
    }



    public static class Builder {
        private String mAppKey;
        private int mPlacement;
        private String searchApiEndPoint;
        private String userAgent;
        private String uid;

        public Builder(String appKey, int placement) {
            mAppKey = appKey;
            mPlacement = placement;
            searchApiEndPoint = SEARCH_URL;
            userAgent = USER_AGENT + "/" + BuildConfig.VERSION_NAME;
        }

        public Builder(URL endPoint, String appKey, int placement) {
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

            ProductSearch productSearch = new ProductSearch(context,
                    mAppKey, mPlacement,
                    searchApiEndPoint,
                    userAgent, uid);

            return productSearch;
        }
    }

    public static interface ResultListener {
        public void onSearchResult(final ProductResponse response, ErrorData error);

    }

}
