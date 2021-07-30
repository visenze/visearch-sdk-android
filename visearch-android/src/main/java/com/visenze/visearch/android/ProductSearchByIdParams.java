package com.visenze.visearch.android;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductSearchByIdParams extends BaseProductSearchParams {

    @Expose(deserialize = false, serialize = false)
    private String productId;

    @SerializedName("alt_limit")
    private Integer altLimit;

    public String getProductId() {
        return productId;
    }

    public Integer getAltLimit() {
        return altLimit;
    }

    public void setAltLimit(Integer altLimit) {
        this.altLimit = altLimit;
    }

    public ProductSearchByIdParams(String productId) {
        this.productId = productId;
    }

}
