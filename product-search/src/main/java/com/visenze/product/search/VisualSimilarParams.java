package com.visenze.product.search;

import com.google.gson.annotations.Expose;

public class VisualSimilarParams extends BaseSearchParams{


    @Expose(deserialize = false, serialize = false)
    private String productId;

    public String getProductId() {
        return productId;
    }

    public VisualSimilarParams(String productId) {
        this.productId = productId;
    }


}
