package com.visenze.product.search.model;

import com.google.gson.annotations.SerializedName;

public class FacetItem {
    @SerializedName("value")
    private String value;

    @SerializedName("count")
    private String count;


    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }



}
