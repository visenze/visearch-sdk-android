package com.visenze.product.search.network;

import com.visenze.product.search.ProductSearch;
import com.visenze.product.search.model.Error;
import com.visenze.product.search.model.ResponseData;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchService {

    public final static String SIMILAR_PRODUCTS = "similar-products";

    private final static String APP_KEY = "app_key";
    private final static String placement_id = "placement_id";
    private String appKey;
    private String placementId;
    private String userAgent;

    private APIService apiService;

    public SearchService(String endPoint, String appKey, String placementId, String userAgent) {
        apiService = Http.getRetrofitInstance(endPoint).create(APIService.class);

        this.appKey = appKey;
        this.placementId = placementId;
        this.userAgent = userAgent;
    }


    private void handleCallback(Call<ResponseData> call, final ProductSearch.ResultListener resultListener) {
        call.enqueue(new Callback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {

                if(response.isSuccessful() && response.body() !=null) {
                    ResponseData data = response.body();
                    handleResponse(data, resultListener);
                } else {
                    resultListener.onSearchResult(null, "api failed");
                }
            }

            @Override
            public void onFailure(Call<ResponseData> call, Throwable t) {
                resultListener.onSearchResult(null, t.getMessage());
            }

        });
    }


    public void handleResponse(ResponseData response, final ProductSearch.ResultListener resultListener) {
        Error error = response.getError();
        if(error != null) {
            resultListener.onSearchResult(null, error.getMessage());
        } else {
            resultListener.onSearchResult(response, null);
        }
    }



}
