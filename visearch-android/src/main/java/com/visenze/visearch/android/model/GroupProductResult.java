package com.visenze.visearch.android.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GroupProductResult {

    @SerializedName("group_by_value")
    private String groupByValue;

    @SerializedName("result")
    private List<Product> products;

    public String getGroupByValue() {
        return groupByValue;
    }

    public void setGroupByValue(String groupByValue) {
        this.groupByValue = groupByValue;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }
}
