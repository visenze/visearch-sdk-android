package com.visenze.visearch.android.network;

import com.visenze.visearch.android.ResultList;
import com.visenze.visearch.android.UploadSearchParams;
import com.visenze.visearch.android.ViSearch;
import com.visenze.visearch.android.ViSearchException;
import com.visenze.visearch.android.model.ResponseData;
import com.visenze.visearch.android.util.AuthGenerator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchService {

    public final static String ID_SEARCH = "search";
    public final static String RECOMMENDATION = "recommendations";
    public final static String COLOR_SEARCH = "colorsearch";
    public final static String UPLOAD_SEARCH = "uploadsearch";
    public final static String DISCOVER_SEARCH = "discoversearch";

    private final static String ACCESS_KEY = "access_key";
    private String appKey;
    private String secretKey;
    private String userAgent;


    private APIService apiService;

    public SearchService(String endPoint, String appKey, String userAgent) {
        this(endPoint, appKey, null, userAgent);
    }


    public SearchService(String endPoint, String appKey, String secretKey, String userAgent) {
        apiService = Http.getRetrofitInstance(endPoint).create(APIService.class);

        this.appKey = appKey;
        this.secretKey = secretKey;
        this.userAgent = userAgent;
    }


    public void search(String endPoint, Map<String, List<String>> paramsMap, final ViSearch.ResultListener resultListener) {
        Map<String, String> headers = getHeaders();
        RetrofitQueryMap params = buildParams(paramsMap);
        Call<ResponseData> call;
        if(ID_SEARCH.equals(endPoint)) {
            call = apiService.search(headers, params);
        } else if(RECOMMENDATION.equals(endPoint)) {
            call = apiService.recommendation(headers, params);
        } else if (COLOR_SEARCH.equals(endPoint)) {
            call = apiService.colorSearch(headers, params);
        } else {
            throw new ViSearchException("wrong API method");
        }

        handleCallback(call, resultListener);
    }


    public void uploadSearch(String endPoint, UploadSearchParams uploadSearchParams, final ViSearch.ResultListener resultListener) {
        byte[] imageBytes = null;

        if (uploadSearchParams.getImage() != null) {
            imageBytes = uploadSearchParams.getImage().getByteArray();
        }

        String imageUrl = uploadSearchParams.getImageUrl();
        String imId = uploadSearchParams.getImId();

        if (imageBytes == null && (imageUrl == null || imageUrl.isEmpty()) && (imId == null || imId.isEmpty())) {
            throw new ViSearchException("Missing parameter, image empty");
        }

        Map<String, String> headers = getHeaders();
        Call<ResponseData> call;
        RetrofitQueryMap params = buildParams(uploadSearchParams.toMap());
        if(imageBytes != null) {
            RequestBody imageBody = RequestBody.create(MediaType.parse("image/*"), imageBytes);
            MultipartBody.Part image = MultipartBody.Part.createFormData("image", "image", imageBody);
            call = getCall(endPoint, headers, params, image);
        } else {
            call = getCall(endPoint, headers, params, null);
        }
        handleCallback(call, resultListener);
    }

    private Call<ResponseData> getCall(String endPoint, Map<String, String> headers, RetrofitQueryMap params, MultipartBody.Part image) {
        Call<ResponseData> call;
        if(UPLOAD_SEARCH.equals(endPoint)) {
            if(image == null) {
                call = apiService.uploadSearch(headers, params);
            } else {
                call = apiService.uploadSearch(headers, image, params);
            }
        } else if(DISCOVER_SEARCH.equals(endPoint)) {
            if(image == null) {
                call = apiService.discoverSearch(headers, params);
            } else {
                call = apiService.discoverSearch(headers, image, params);
            }
        } else {
            throw new ViSearchException("wrong API method");
        }
        return call;
    }

    private void handleCallback(Call<ResponseData> call, final ViSearch.ResultListener resultListener) {
        call.enqueue(new Callback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {

                if(response.isSuccessful() && response.body() !=null) {
                    ResponseData data = response.body();
                    handleResponse(data, resultListener);
                } else {
                    resultListener.onSearchError("api failed");
                }
            }

            @Override
            public void onFailure(Call<ResponseData> call, Throwable t) {
                resultListener.onSearchError(t.getMessage());
            }
        });
    }


    public void handleResponse(ResponseData response, final ViSearch.ResultListener resultListener) {
        ResultList resultList = response.getResultList();
        if (resultList.getErrorMessage() != null)
            resultListener.onSearchError(resultList.getErrorMessage());
        else {
            resultListener.onSearchResult(resultList);
        }

    }

    private Map<String, String> getHeaders() {
        return AuthGenerator.generateHeaderParams(appKey, secretKey, userAgent);
    }


    public RetrofitQueryMap buildParams(Map<String, List<String>> params) {
        Map<String, Object> map = new HashMap<String, Object>();
        RetrofitQueryMap ret = new RetrofitQueryMap(map);

        if (secretKey == null) {
            ret.put(ACCESS_KEY, appKey);
        }
        for(Map.Entry<String, List<String>> entry : params.entrySet()) {
            String key = entry.getKey();
            List<String> val = entry.getValue();
            if(val.size() == 1) {
                ret.put(key, val.get(0));
            } else if(val.size() > 1){
                ret.put(key, val);
            }
        }
        return ret;
    }




}
