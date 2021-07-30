package com.visenze.visearch.android.network;

import com.visenze.visearch.android.model.ResponseData;
import com.visenze.visearch.android.network.retry.Retry;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface APIService {
    @Retry
    @GET("search")
    Call<ResponseData> search(@HeaderMap Map<String, String> headers, @QueryMap RetrofitQueryMap query);

    @Retry
    @GET("recommendations")
    Call<ResponseData> recommendation(@HeaderMap Map<String, String> headers, @QueryMap RetrofitQueryMap query);

    @Retry
    @GET("colorsearch")
    Call<ResponseData> colorSearch(@HeaderMap Map<String, String> headers, @QueryMap RetrofitQueryMap query);

    @Retry
    @GET("uploadsearch")
    Call<ResponseData> uploadSearch(@HeaderMap Map<String, String> headers, @QueryMap RetrofitQueryMap query);

    @Retry
    @Multipart
    @POST("uploadsearch")
    Call<ResponseData> uploadSearch(@HeaderMap Map<String, String> headers, @Part MultipartBody.Part image, @QueryMap RetrofitQueryMap query);


    // unlike uploadSearch accept GET method if im_url
    // discoversearch accept post type if im_url is set...
    @Retry
    @POST("discoversearch")
    Call<ResponseData> discoverSearch(@HeaderMap Map<String, String> headers, @QueryMap RetrofitQueryMap query);

    @Retry
    @Multipart
    @POST("discoversearch")
    Call<ResponseData> discoverSearch(@HeaderMap Map<String, String> headers, @Part MultipartBody.Part image, @QueryMap RetrofitQueryMap query);

}
