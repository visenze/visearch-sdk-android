package com.visenze.visearch.android.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ProductObject extends ProductType {
    @SerializedName("result")
    private List<Product> result;

    @SerializedName("total")
    private Integer total;


    public List<Product> getResult() {
        return result;
    }

    public void setResult(List<Product> result) {
        this.result = result;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }
}
