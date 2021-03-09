package com.visenze.visearch.android.network;

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

public interface APIProductService {

    @Retry
    @GET("product/search_by_id/{product_id}")
    Call<ProductResponse> searchById(@Path("product_id") String productId, @QueryMap RetrofitQueryMap query);


    @Retry
    @POST("product/search_by_image")
    Call<ProductResponse> searchByImage(@QueryMap RetrofitQueryMap query);

    @Retry
    @Multipart
    @POST("product/search_by_image")
    Call<ProductResponse> searchByImage(@Part MultipartBody.Part image, @QueryMap RetrofitQueryMap query);


}
