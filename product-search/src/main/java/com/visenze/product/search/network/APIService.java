package com.visenze.product.search.network;

import com.visenze.product.search.model.ResponseData;
import com.visenze.product.search.network.retry.Retry;

import java.util.Map;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

public interface APIService {

    @Retry
    @GET("similar-products/{product_id}")
    Call<ResponseData> similarProducts(@Path("product_id") String productId, @QueryMap RetrofitQueryMap query);


    @Retry
    @POST("similar-products")
    Call<ResponseData> similarProducts(@QueryMap RetrofitQueryMap query);

    @Retry
    @Multipart
    @POST("similar-products")
    Call<ResponseData> similarProducts(@Part MultipartBody.Part image, @QueryMap RetrofitQueryMap query);


}
