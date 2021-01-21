package com.visenze.visearch.android.network;

import com.visenze.visearch.android.ProductBaseSearchParams;
import com.visenze.visearch.android.ProductImageSearchParams;
import com.visenze.visearch.android.ProductSearch;
import com.visenze.visearch.android.ProductVisualSimilarParams;
import com.visenze.visearch.android.model.Error;
import com.visenze.visearch.android.model.ProductResponse;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductSearchService {
    private final static String APP_KEY = "app_key";
    private final static String PLACEMENT_ID = "placement_id";
    private String appKey;
    private String placementId;
    private String userAgent;

    private APIProductService apiService;

    public ProductSearchService(String endPoint, String appKey, String placementId, String userAgent) {
        apiService = Http.getRetrofitInstance(endPoint).create(APIProductService.class);

        this.appKey = appKey;
        this.placementId = placementId;
        this.userAgent = userAgent;
    }

    public void visualSimilarSearch(ProductVisualSimilarParams visualSimilarParams, final ProductSearch.ResultListener listener) {
        String productId = visualSimilarParams.getProductId();
        RetrofitQueryMap params = buildQueryMap(visualSimilarParams);
        Call<ProductResponse> call = apiService.similarProducts(productId, params);
        handleCallback(call, listener);
    }


    public void similarSearch(ProductImageSearchParams imageSearchParams, final ProductSearch.ResultListener listener) {
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

        Call<ProductResponse> call;
        if(imageBytes != null) {
            RequestBody imageBody = RequestBody.create(MediaType.parse("image/*"), imageBytes);
            MultipartBody.Part image = MultipartBody.Part.createFormData("image", "image", imageBody);
            call = apiService.similarProducts(image, params);
        } else {
            call = apiService.similarProducts(params);
        }
        handleCallback(call, listener);
    }



    private RetrofitQueryMap buildQueryMap(ProductBaseSearchParams params) {
        RetrofitQueryMap map = params.getQueryMap();
        map.put(APP_KEY, appKey);
        map.put(PLACEMENT_ID, placementId);
        return map;
    }

    private void handleCallback(Call<ProductResponse> call, final ProductSearch.ResultListener resultListener) {
        call.enqueue(new Callback<ProductResponse>() {
            @Override
            public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {

                if(response.isSuccessful() && response.body() !=null) {
                    ProductResponse data = response.body();
                    handleResponse(data, resultListener);
                } else {
                    resultListener.onSearchResult(null, "api failed");
                }
            }

            @Override
            public void onFailure(Call<ProductResponse> call, Throwable t) {
                resultListener.onSearchResult(null, t.getMessage());
            }

        });
    }


    public void handleResponse(ProductResponse response, final ProductSearch.ResultListener resultListener) {
        Error error = response.getError();
        if(error != null) {
            resultListener.onSearchResult(null, error.getMessage());
        } else {
            resultListener.onSearchResult(response, null);
        }
    }

}
