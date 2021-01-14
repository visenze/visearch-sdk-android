package com.visenze.visearch.android.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ObjectResult extends ProductType {
    @SerializedName("result")
    private List<ImageResult> result;

    @SerializedName("total")
    private Integer total;

    public List<ImageResult> getResult() {
        return result;
    }

    public void setResult(List<ImageResult> result) {
        this.result = result;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }
}
