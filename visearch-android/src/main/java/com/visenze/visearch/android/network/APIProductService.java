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
    @GET("similar-products/{product_id}")
    Call<ProductResponse> similarProducts(@Path("product_id") String productId, @QueryMap RetrofitQueryMap query);


    @Retry
    @POST("similar-products")
    Call<ProductResponse> similarProducts(@QueryMap RetrofitQueryMap query);

    @Retry
    @Multipart
    @POST("similar-products")
    Call<ProductResponse> similarProducts(@Part MultipartBody.Part image, @QueryMap RetrofitQueryMap query);


}
