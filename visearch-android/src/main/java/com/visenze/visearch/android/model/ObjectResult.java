package com.visenze.visearch.android.model;

import java.util.List;

public class ObjectResult extends ProductType {
    private List<ImageResult> result;
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
