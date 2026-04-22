package com.visenze.visearch.android.network;

import com.visenze.visearch.android.model.AutoCompleteResponse;
import com.visenze.visearch.android.model.ProductResponse;
import com.visenze.visearch.android.network.retry.Retry;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

public interface APIProductServiceV2 {

    @Retry
    @GET("v1/visearch/recommendations/{product_id}")
    Call<ProductResponse> searchById(@Path("product_id") String productId, @QueryMap RetrofitQueryMap query);

    @Retry
    @POST("v1/visearch/search_by_image")
    Call<ProductResponse> searchByImage(@QueryMap RetrofitQueryMap query);

    @Retry
    @Multipart
    @POST("v1/visearch/search_by_image")
    Call<ProductResponse> searchByImage(@Part MultipartBody.Part image, @QueryMap RetrofitQueryMap query);

    @Retry
    @POST("v1/search")
    Call<ProductResponse> multisearch(@QueryMap RetrofitQueryMap query);

    @Retry
    @Multipart
    @POST("v1/search")
    Call<ProductResponse> multisearch(@Part MultipartBody.Part image, @QueryMap RetrofitQueryMap query);

    @Retry
    @POST("v1/autocomplete")
    Call<AutoCompleteResponse> multisearchAutocomplete(@QueryMap RetrofitQueryMap query);

    @Retry
    @Multipart
    @POST("v1/autocomplete")
    Call<AutoCompleteResponse> multisearchAutocomplete(@Part MultipartBody.Part image, @QueryMap RetrofitQueryMap query);

    @Retry
    @POST("v1/search/complementary")
    Call<ProductResponse> multisearchComplementary(@QueryMap RetrofitQueryMap query);

    @Retry
    @Multipart
    @POST("v1/search/complementary")
    Call<ProductResponse> multisearchComplementary(@Part MultipartBody.Part image, @QueryMap RetrofitQueryMap query);

    @Retry
    @POST("v1/search/outfit-recommendations")
    Call<ProductResponse> multisearchOutfitRec(@QueryMap RetrofitQueryMap query);

    @Retry
    @Multipart
    @POST("v1/search/outfit-recommendations")
    Call<ProductResponse> multisearchOutfitRec(@Part MultipartBody.Part image, @QueryMap RetrofitQueryMap query);
}
