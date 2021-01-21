package com.visenze.product.search.network;

import com.visenze.product.search.BaseSearchParams;
import com.visenze.product.search.ImageSearchParams;
import com.visenze.product.search.ProductSearch;
import com.visenze.product.search.VisualSimilarParams;
import com.visenze.product.search.model.Error;
import com.visenze.product.search.model.ResponseData;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchService {


    private final static String APP_KEY = "app_key";
    private final static String PLACEMENT_ID = "placement_id";
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

    public void visualSimilarSearch(VisualSimilarParams visualSimilarParams, final ProductSearch.ResultListener listener) {
        String productId = visualSimilarParams.getProductId();
        RetrofitQueryMap params = buildQueryMap(visualSimilarParams);
        Call<ResponseData> call = apiService.similarProducts(productId, params);
        handleCallback(call, listener);
    }


    public void similarSearch(ImageSearchParams imageSearchParams, final ProductSearch.ResultListener listener) {
        byte[] imageBytes = null;

        if (imageSearchParams.getImage() != null) {
            imageBytes = imageSearchParams.getImage().getByteArray();
        }

        String imageUrl = imageSearchParams.getImUrl();
        String imId = imageSearchParams.getImId();

        if (imageBytes == null && (imageUrl == null || imageUrl.isEmpty()) && (imId == null || imId.isEmpty())) {
            throw new IllegalArgumentException("Missing parameter, image empty");
        }

        RetrofitQueryMap params = buildQueryMap(imageSearchParams);

        Call<ResponseData> call;
        if(imageBytes != null) {
            RequestBody imageBody = RequestBody.create(MediaType.parse("image/*"), imageBytes);
            MultipartBody.Part image = MultipartBody.Part.createFormData("image", "image", imageBody);
             call = apiService.similarProducts(image, params);
        } else {
             call = apiService.similarProducts(params);
        }
        handleCallback(call, listener);
    }



    private RetrofitQueryMap buildQueryMap(BaseSearchParams params) {
        RetrofitQueryMap map = params.getQueryMap();
        map.put(APP_KEY, appKey);
        map.put(PLACEMENT_ID, placementId);
        return map;
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
