package com.visenze.visearch.android.model;

import com.google.gson.annotations.SerializedName;

public class FacetItem {

    @SerializedName("value")
    private String value;

    @SerializedName("count")
    private Integer count;


    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }


}
