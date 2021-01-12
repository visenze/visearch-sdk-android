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

    @GET("search")
    Call<ResponseData> search(@HeaderMap Map<String, String> headers, @QueryMap Map<String, String> query);

    @GET("recommendation")
    Call<ResponseData> recommendation(@HeaderMap Map<String, String> headers, @QueryMap Map<String, String> query);

    @GET("colorsearch")
    Call<ResponseData> colorSearch(@HeaderMap Map<String, String> headers, @QueryMap Map<String, String> query);

    @GET("uploadsearch")
    Call<ResponseData> uploadSearch(@HeaderMap Map<String, String> headers, @QueryMap Map<String, String> query);

    @Multipart
    @POST("uploadsearch")
    Call<ResponseData> uploadSearch(@HeaderMap Map<String, String> headers, @Part MultipartBody.Part image, @PartMap Map<String, RequestBody> query);

    // unlike uploadSearch accept GET method if im_url
    // discoversearch accept post type if im_url is set...
    @POST("discoversearch")
    Call<ResponseData> discoverSearch(@HeaderMap Map<String, String> headers, @QueryMap Map<String, String> query);

    @Multipart
    @POST("discoversearch")
    Call<ResponseData> discoverSearch(@HeaderMap Map<String, String> headers, @Part MultipartBody.Part image, @PartMap Map<String, RequestBody> query);


}
