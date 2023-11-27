package com.visenze.visearch.android.network;

import com.google.gson.Gson;
import com.visenze.visearch.android.BaseProductSearchParams;
import com.visenze.visearch.android.ProductSearchByImageParams;
import com.visenze.visearch.android.ProductSearch;
import com.visenze.visearch.android.ProductSearchByIdParams;
import com.visenze.visearch.android.model.AutoCompleteResponse;
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
        byte[] imageBytes = validateImageParams(imageSearchParams, multiSearch);

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

    public void multisearchAutocomplete(ProductSearchByImageParams imageSearchParams,
                                        final ProductSearch.AutoCompleteResultListener listener) {
        byte[] imageBytes = validateImageParams(imageSearchParams, true);

        RetrofitQueryMap params = buildQueryMap(imageSearchParams);

        Call<AutoCompleteResponse> call;
        if(imageBytes != null) {
            RequestBody imageBody = RequestBody.create(MediaType.parse("image/*"), imageBytes);
            MultipartBody.Part image = MultipartBody.Part.createFormData("image", "image", imageBody);
            call = getAutoCompleteResponseCall(params, image);
        } else {
            call = getAutoCompleteResponseCall(params, null);
        }
        handleCallback(call, listener);
    }

    /**
     * If not multi-search, 1 of image, im_url or im_id must be provided, throw Exception if missing
     *
     * @param imageSearchParams request params
     * @param multiSearch whether this is multisearch related API or normal SBI
     * @return image file bytes if provided
     */
    private byte[] validateImageParams(ProductSearchByImageParams imageSearchParams, boolean multiSearch) {
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
        return imageBytes;
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

    private Call<AutoCompleteResponse> getAutoCompleteResponseCall(RetrofitQueryMap params, MultipartBody.Part image) {
        if (image == null) {
            return  apiService.multisearchAutocomplete(params);
        }

        return apiService.multisearchAutocomplete(image, params);
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
                if(response.isSuccessful() && response.body() != null) {
                    ProductResponse data = response.body();
                    handleResponse(data, resultListener);
                    return;
                }

                if (response.errorBody() != null) {
                    Gson gson = new Gson();
                    ProductResponse resp = gson.fromJson(response.errorBody().charStream(), ProductResponse.class);
                    if (resp != null && resp.getError() != null) {
                        resultListener.onSearchResult(null, resp.getError());
                        return;
                    }
                }

                resultListener.onSearchResult(null, ErrorData.unknownError("api failed"));

            }

            @Override
            public void onFailure(Call<ProductResponse> call, Throwable t) {
                resultListener.onSearchResult(null, ErrorData.unknownError(t.getMessage()));
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

    private void handleCallback(Call<AutoCompleteResponse> call, final ProductSearch.AutoCompleteResultListener resultListener) {
        call.enqueue(new Callback<AutoCompleteResponse>() {

            @Override
            public void onResponse(Call<AutoCompleteResponse> call, Response<AutoCompleteResponse> response) {
                if(response.isSuccessful() && response.body() != null) {
                    AutoCompleteResponse data = response.body();
                    handleAutoCompleteResponse(data, resultListener);
                    return;
                }

                if (response.errorBody() != null) {
                    Gson gson = new Gson();
                    AutoCompleteResponse resp = gson.fromJson(response.errorBody().charStream(), AutoCompleteResponse.class);
                    if (resp != null && resp.getError() != null) {
                        resultListener.onResult(null, resp.getError());
                        return;
                    }
                }

                resultListener.onResult(null, ErrorData.unknownError("api failed"));
            }

            @Override
            public void onFailure(Call<AutoCompleteResponse> call, Throwable t) {
                resultListener.onResult(null, ErrorData.unknownError(t.getMessage()));
            }

        });
    }

    public void handleAutoCompleteResponse(AutoCompleteResponse response, final ProductSearch.AutoCompleteResultListener listener) {
        ErrorData error = response.getError();
        if(error != null) {
            listener.onResult(null, error);
        } else {
            listener.onResult(response, null);
        }
    }

}
