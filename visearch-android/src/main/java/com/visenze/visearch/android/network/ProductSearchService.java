package com.visenze.visearch.android.network;

import com.google.gson.Gson;
import com.visenze.visearch.android.BaseProductSearchParams;
import com.visenze.visearch.android.ProductSearchByImageParams;
import com.visenze.visearch.android.ProductSearch;
import com.visenze.visearch.android.ProductSearchByIdParams;
import com.visenze.visearch.android.model.AutoCompleteResponse;
import com.visenze.visearch.android.model.ErrorData;
import com.visenze.visearch.android.model.ProductResponse;
import com.visenze.visearch.android.model.ProductSearchApi;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ProductSearchService {
    private final static String APP_KEY = "app_key";
    private final static String PLACEMENT_ID = "placement_id";

    private static final Set<String> NEW_DOMAIN_PREFIXES = new HashSet<>(Arrays.asList(
            "https://multisearch-aw.rezolve.com",
            "https://multisearch-az.rezolve.com"
    ));

    private String appKey;
    private int placementId;
    private String userAgent;

    private final APIProductService legacyApi;
    private final APIProductServiceV2 newApi;

    public ProductSearchService(String endPoint, String appKey, int placementId, String userAgent) {
        Retrofit retrofit = Http.getRetrofitInstance(endPoint);
        boolean isNew = isNewDomain(endPoint);

        this.legacyApi = isNew ? null : retrofit.create(APIProductService.class);
        this.newApi    = isNew ? retrofit.create(APIProductServiceV2.class) : null;

        this.appKey = appKey;
        this.placementId = placementId;
        this.userAgent = userAgent;
    }

    private static boolean isNewDomain(String endPoint) {
        for (String prefix : NEW_DOMAIN_PREFIXES) {
            if (endPoint.startsWith(prefix)) return true;
        }
        return false;
    }

    public void searchById(ProductSearchByIdParams visualSimilarParams, final ProductSearch.ResultListener listener) {
        String productId = visualSimilarParams.getProductId();
        RetrofitQueryMap params = buildQueryMap(visualSimilarParams);
        Call<ProductResponse> call = newApi != null
                ? newApi.searchById(productId, params)
                : legacyApi.searchById(productId, params);
        handleCallback(call, listener);
    }

    public void searchByImage(ProductSearchByImageParams imageSearchParams, final ProductSearch.ResultListener listener) {
        searchByImage(imageSearchParams, listener, ProductSearchApi.SBI);
    }

    public void searchByImage(ProductSearchByImageParams imageSearchParams, final ProductSearch.ResultListener listener, ProductSearchApi api) {
        byte[] imageBytes = validateImageParams(imageSearchParams, api.isMultisearchRelated());

        RetrofitQueryMap params = buildQueryMap(imageSearchParams);

        Call<ProductResponse> call;
        if (imageBytes != null) {
            RequestBody imageBody = RequestBody.create(MediaType.parse("image/*"), imageBytes);
            MultipartBody.Part image = MultipartBody.Part.createFormData("image", "image", imageBody);
            call = getProductResponseCall(params, image, api);
        } else {
            call = getProductResponseCall(params, null, api);
        }
        handleCallback(call, listener);
    }

    public void multisearchAutocomplete(ProductSearchByImageParams imageSearchParams,
                                        final ProductSearch.AutoCompleteResultListener listener) {
        byte[] imageBytes = validateImageParams(imageSearchParams, true);

        RetrofitQueryMap params = buildQueryMap(imageSearchParams);

        Call<AutoCompleteResponse> call;
        if (imageBytes != null) {
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

    private Call<ProductResponse> getProductResponseCall(RetrofitQueryMap params, MultipartBody.Part image, ProductSearchApi api) {
        if (ProductSearchApi.SBI.equals(api)) {
            return getSbiResponseCall(params, image);
        }

        if (ProductSearchApi.MS_OUTFIT_REC.equals(api)) {
            return getOutfitRecResponseCall(params, image);
        }

        if (ProductSearchApi.MS_COMPLEMENTARY.equals(api)) {
            return getMsCtlResponseCall(params, image);
        }

        // default: multisearch
        if (newApi != null) {
            return image == null ? newApi.multisearch(params) : newApi.multisearch(image, params);
        }
        return image == null ? legacyApi.multisearch(params) : legacyApi.multisearch(image, params);
    }

    private Call<ProductResponse> getSbiResponseCall(RetrofitQueryMap params, MultipartBody.Part image) {
        if (newApi != null) {
            return image == null ? newApi.searchByImage(params) : newApi.searchByImage(image, params);
        }
        return image == null ? legacyApi.searchByImage(params) : legacyApi.searchByImage(image, params);
    }

    private Call<ProductResponse> getMsCtlResponseCall(RetrofitQueryMap params, MultipartBody.Part image) {
        if (newApi != null) {
            return image == null ? newApi.multisearchComplementary(params) : newApi.multisearchComplementary(image, params);
        }
        return image == null ? legacyApi.multisearchComplementary(params) : legacyApi.multisearchComplementary(image, params);
    }

    private Call<ProductResponse> getOutfitRecResponseCall(RetrofitQueryMap params, MultipartBody.Part image) {
        if (newApi != null) {
            return image == null ? newApi.multisearchOutfitRec(params) : newApi.multisearchOutfitRec(image, params);
        }
        return image == null ? legacyApi.multisearchOutfitRec(params) : legacyApi.multisearchOutfitRec(image, params);
    }

    private Call<AutoCompleteResponse> getAutoCompleteResponseCall(RetrofitQueryMap params, MultipartBody.Part image) {
        if (newApi != null) {
            return image == null ? newApi.multisearchAutocomplete(params) : newApi.multisearchAutocomplete(image, params);
        }
        return image == null ? legacyApi.multisearchAutocomplete(params) : legacyApi.multisearchAutocomplete(image, params);
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
                if (response.isSuccessful() && response.body() != null) {
                    handleResponse(response.body(), resultListener);
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
        if (error != null) {
            resultListener.onSearchResult(null, error);
        } else {
            resultListener.onSearchResult(response, null);
        }
    }

    private void handleCallback(Call<AutoCompleteResponse> call, final ProductSearch.AutoCompleteResultListener resultListener) {
        call.enqueue(new Callback<AutoCompleteResponse>() {
            @Override
            public void onResponse(Call<AutoCompleteResponse> call, Response<AutoCompleteResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    handleAutoCompleteResponse(response.body(), resultListener);
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
        if (error != null) {
            listener.onResult(null, error);
        } else {
            listener.onResult(response, null);
        }
    }
}
