package com.visenze.visearch.android.network;

import com.visenze.visearch.android.data.ResponseData;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.QueryMap;

public interface APIService {

    @GET("uploadsearch")
    Call<ResponseData> uploadSearch(@QueryMap Map<String, RequestBody> query);

    @Multipart
    @POST("uploadsearch")
    Call<ResponseData> uploadSearch(@HeaderMap Map<String, String> headers, @Part MultipartBody.Part image, @PartMap Map<String, RequestBody> query);
}
