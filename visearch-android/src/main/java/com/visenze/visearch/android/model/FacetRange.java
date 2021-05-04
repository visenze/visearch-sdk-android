package com.visenze.visearch.android.model;

import com.google.gson.annotations.SerializedName;

public class FacetRange {
    @SerializedName("min")
    private String min;

    @SerializedName("max")
    private String max;

    public String getMin() {
        return min;
    }

    public void setMin(String min) {
        this.min = min;
    }

    public String getMax() {
        return max;
    }

    public void setMax(String max) {
        this.max = max;
    }
}
