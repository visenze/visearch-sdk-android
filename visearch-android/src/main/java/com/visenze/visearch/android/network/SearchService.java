package com.visenze.visearch.android.network;

import com.visenze.visearch.android.ResultList;
import com.visenze.visearch.android.UploadSearchParams;
import com.visenze.visearch.android.ViSearch;
import com.visenze.visearch.android.ViSearchException;
import com.visenze.visearch.android.data.ResponseData;
import com.visenze.visearch.android.util.AuthGenerator;

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
    public final static String RECOMMENDATION = "recommendation";
    public final static String COLOR_SEARCH = "colorsearch";
    public final static String UPLOAD_SEARCH = "uploadsearch";
    public final static String DISCOVER_SEARCH = "discoversearch";

    private String appKey;
    private String secretKey;
    private String userAgent;


    private APIService apiService;
    public SearchService(String endPoint, String appKey, String secretKey, String userAgent) {
        apiService = Http.getRetrofitInstance(endPoint).create(APIService.class);

        this.appKey = appKey;
        this.secretKey = secretKey;
        this.userAgent = userAgent;
    }

    public void search(String endPoint, Map<String, List<String>> paramsMap, final ViSearch.ResultListener resultListener) {
        Map<String, String> headers = getHeaders();
        Map<String, String> params = buildGetParams(paramsMap);
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
        if(imageBytes != null) {
            RequestBody imageBody = RequestBody.create(MediaType.parse("image/*"), imageBytes);
            MultipartBody.Part image = MultipartBody.Part.createFormData("image", "image", imageBody);
            Map<String, RequestBody> params = buildPostParams(uploadSearchParams.toMap());
            if(UPLOAD_SEARCH.equals(endPoint)) {
                call = apiService.uploadSearch(headers, image, params);
            } else if (DISCOVER_SEARCH.equals(endPoint)) {
                call = apiService.discoverSearch(headers, image, params);
            } else {
                throw new ViSearchException("wrong API method");
            }

        } else {
            Map<String, String> params = buildGetParams(uploadSearchParams.toMap());
            if(UPLOAD_SEARCH.equals(endPoint)) {
                call = apiService.uploadSearch(headers, params);
            } else if(DISCOVER_SEARCH.equals(endPoint)) {
                call = apiService.discoverSearch(headers, params);
            } else {
                throw new ViSearchException("wrong API method");
            }

        }

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

    private RequestBody createRequestBody(String value) {
        return RequestBody.create(MediaType.parse("text/plain"), value);
    }

    private Map<String, RequestBody> buildPostParams(Map<String, List<String>> params) {
        Map<String, RequestBody> ret = new HashMap<String, RequestBody>();

        if (secretKey == null)
            ret.put("access_key", createRequestBody(appKey));
        for (Map.Entry<String, List<String> > entry : params.entrySet()) {
            for (String s : entry.getValue()) {
                ret.put(entry.getKey(), createRequestBody(s));
            }
        }
        return ret;
    }

    private Map<String, String> buildGetParams(Map<String, List<String>> params) {

        Map<String, String> ret = new HashMap<String, String>();

        if (secretKey == null)
            ret.put("access_key", appKey);
        for (Map.Entry<String, List<String> > entry : params.entrySet()) {
            for (String s : entry.getValue()) {
                ret.put(entry.getKey(), s);
            }
        }
        return ret;

    }




}
