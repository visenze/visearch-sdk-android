package com.visenze.visearch.android;

import com.google.gson.annotations.Expose;

public class ProductSearchByIdParams extends BaseProductSearchParams {

    @Expose(deserialize = false, serialize = false)
    private String productId;

    public String getProductId() {
        return productId;
    }

    public ProductSearchByIdParams(String productId) {
        this.productId = productId;
    }

}
