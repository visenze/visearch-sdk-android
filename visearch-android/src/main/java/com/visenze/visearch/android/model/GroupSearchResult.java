package com.visenze.visearch.android.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GroupSearchResult {

    @SerializedName("result")
    private List<ImageResult> result;

    @SerializedName("group_by_value")
    private String groupByValue;

    public List<ImageResult> getResult() {
        return result;
    }

    public void setResult(List<ImageResult> result) {
        this.result = result;
    }

    public String getGroupByValue() {
        return groupByValue;
    }

    public void setGroupByValue(String groupByValue) {
        this.groupByValue = groupByValue;
    }
}
