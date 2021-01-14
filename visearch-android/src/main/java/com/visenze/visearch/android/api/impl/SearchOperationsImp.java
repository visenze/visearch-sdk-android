package com.visenze.visearch.android.api.impl;

import android.content.Context;

import com.visenze.visearch.android.ColorSearchParams;
import com.visenze.visearch.android.IdSearchParams;
import com.visenze.visearch.android.UploadSearchParams;
import com.visenze.visearch.android.ViSearch;
import com.visenze.visearch.android.api.SearchOperations;
import com.visenze.visearch.android.network.SearchService;

public class SearchOperationsImp implements SearchOperations {

    SearchService searchService;

    public SearchOperationsImp(String apiUrl, String accessKey, String secretKey, String userAgent) {
        searchService = new SearchService(apiUrl, accessKey, secretKey, userAgent);
    }

    @Override
    public void search(IdSearchParams idSearchParams, ViSearch.ResultListener resultListener) {
        searchService.search(SearchService.ID_SEARCH, idSearchParams.toMap(), resultListener);
    }

    @Override
    public void recommendation(IdSearchParams idSearchParams, ViSearch.ResultListener resultListener) {
        searchService.search(SearchService.RECOMMENDATION, idSearchParams.toMap(), resultListener);
    }

    @Override
    public void colorSearch(ColorSearchParams colorSearchParams, ViSearch.ResultListener resultListener) {
        searchService.search(SearchService.COLOR_SEARCH, colorSearchParams.toMap(), resultListener);
    }

    @Override
    public void uploadSearch(UploadSearchParams uploadSearchParams, ViSearch.ResultListener resultListener) {
        searchService.uploadSearch(SearchService.UPLOAD_SEARCH, uploadSearchParams, resultListener);
    }

    @Override
    public void discoverSearch(UploadSearchParams uploadSearchParams, ViSearch.ResultListener resultListener) {
        searchService.uploadSearch(SearchService.DISCOVER_SEARCH, uploadSearchParams, resultListener);
    }

    @Override
    public void cancelSearch(ViSearch.ResultListener resultListener) {
        resultListener.onSearchCanceled();
    }


}
