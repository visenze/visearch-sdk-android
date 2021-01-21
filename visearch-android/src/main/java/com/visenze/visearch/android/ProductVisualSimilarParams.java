package com.visenze.visearch.android;

import com.google.gson.annotations.Expose;

public class ProductVisualSimilarParams extends ProductBaseSearchParams {

    @Expose(deserialize = false, serialize = false)
    private String productId;

    public String getProductId() {
        return productId;
    }

    public ProductVisualSimilarParams(String productId) {
        this.productId = productId;
    }

}
