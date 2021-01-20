package com.visenze.product.search.network;

import com.visenze.product.search.network.retry.RetryCallAdapterFactory;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Http {

    private static Retrofit retrofit;
    public static Retrofit getRetrofitInstance(String url) {
        if(retrofit == null) {
            retrofit = new retrofit2.Retrofit.Builder()
                    .baseUrl(url)
                    .addCallAdapterFactory(RetryCallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

}
