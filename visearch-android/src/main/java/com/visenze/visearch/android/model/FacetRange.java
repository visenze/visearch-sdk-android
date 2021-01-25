package com.visenze.visearch.android.model;

import com.google.gson.annotations.SerializedName;

public class FacetRange {
    @SerializedName("min")
    private Number min;

    @SerializedName("max")
    private Number max;

    public Number getMin() {
        return min;
    }

    public void setMin(Number min) {
        this.min = min;
    }

    public Number getMax() {
        return max;
    }

    public void setMax(Number max) {
        this.max = max;
    }
}
