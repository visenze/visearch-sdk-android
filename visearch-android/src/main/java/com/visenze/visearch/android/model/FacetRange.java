package com.visenze.visearch.android.model;

import com.google.gson.annotations.SerializedName;

public class FacetRange {
    @SerializedName("min")
    private float min;

    @SerializedName("max")
    private float max;

    public float getMin() {
        return min;
    }

    public void setMin(float min) {
        this.min = min;
    }

    public float getMax() {
        return max;
    }

    public void setMax(float max) {
        this.max = max;
    }
}
