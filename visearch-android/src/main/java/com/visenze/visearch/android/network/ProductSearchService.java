package com.visenze.visearch.android.network;

import com.google.gson.Gson;
import com.visenze.visearch.android.BaseProductSearchParams;
import com.visenze.visearch.android.ProductSearchByImageParams;
import com.visenze.visearch.android.ProductSearch;
import com.visenze.visearch.android.ProductSearchByIdParams;
import com.visenze.visearch.android.model.ErrorData;
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
    private int placementId;
    private String userAgent;

    private APIProductService apiService;

    public ProductSearchService(String endPoint, String appKey, int placementId, String userAgent) {
        apiService = Http.getRetrofitInstance(endPoint).create(APIProductService.class);

        this.appKey = appKey;
        this.placementId = placementId;
        this.userAgent = userAgent;
    }

    public void searchById(ProductSearchByIdParams visualSimilarParams, final ProductSearch.ResultListener listener) {
        String productId = visualSimilarParams.getProductId();
        RetrofitQueryMap params = buildQueryMap(visualSimilarParams);
        Call<ProductResponse> call = apiService.searchById(productId, params);
        handleCallback(call, listener);
    }

    public void searchByImage(ProductSearchByImageParams imageSearchParams, final ProductSearch.ResultListener listener) {
        searchByImage(imageSearchParams, listener, false);
    }

    public void searchByImage(ProductSearchByImageParams imageSearchParams, final ProductSearch.ResultListener listener, boolean multiSearch) {
        byte[] imageBytes = null;

        if (imageSearchParams.getImage() != null) {
            imageBytes = imageSearchParams.getImage().getByteArray();
        }

        String imageUrl = imageSearchParams.getImUrl();
        String imId = imageSearchParams.getImId();

        // image is optional for multisearch
        if (!multiSearch) {
            if (imageBytes == null && (imageUrl == null || imageUrl.isEmpty()) && (imId == null || imId.isEmpty())) {
                throw new IllegalArgumentException("Please provide imUrl , imId or image parameter");
            }
        }

        RetrofitQueryMap params = buildQueryMap(imageSearchParams);

        Call<ProductResponse> call;
        if(imageBytes != null) {
            RequestBody imageBody = RequestBody.create(MediaType.parse("image/*"), imageBytes);
            MultipartBody.Part image = MultipartBody.Part.createFormData("image", "image", imageBody);
            call = getProductResponseCall(params, image, multiSearch);
        } else {
            call = getProductResponseCall(params, null, multiSearch);
        }
        handleCallback(call, listener);
    }

    private Call<ProductResponse> getProductResponseCall(RetrofitQueryMap params, MultipartBody.Part image, boolean multiSearch) {
        if (multiSearch) {
            if (image == null) {
                return  apiService.multisearch(params);
            }

            return apiService.multisearch(image, params);
        } else {
            if (image == null) {
                return  apiService.searchByImage(params);
            }

            return apiService.searchByImage(image, params);
        }
    }


    private RetrofitQueryMap buildQueryMap(BaseProductSearchParams params) {
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
                    if (response.errorBody() != null) {
                        Gson gson = new Gson();
                        ProductResponse resp = gson.fromJson(response.errorBody().charStream(), ProductResponse.class);
                        if (resp != null && resp.getError()!= null) {
                            resultListener.onSearchResult(null, resp.getError());
                            return;
                        }
                    }
                    ErrorData error = new ErrorData();
                    error.setMessage("api failed");
                    error.setCode(-1);
                    resultListener.onSearchResult(null, error);
                }
            }

            @Override
            public void onFailure(Call<ProductResponse> call, Throwable t) {

                ErrorData error = new ErrorData();
                error.setMessage(t.getMessage());
                error.setCode(-1);
                resultListener.onSearchResult(null, error);
            }

        });
    }


    public void handleResponse(ProductResponse response, final ProductSearch.ResultListener resultListener) {
        ErrorData error = response.getError();
        if(error != null) {
            resultListener.onSearchResult(null, error);
        } else {
            resultListener.onSearchResult(response, null);
        }
    }

}
