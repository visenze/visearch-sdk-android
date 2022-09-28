package com.visenze.visearch.android.model;

import com.google.gson.annotations.SerializedName;

public class SetInfo {

    @SerializedName("set_id")
    private String setId;

    @SerializedName("set_score")
    private double setScore;

    @SerializedName("item_count")
    private int itemCount;

    public String getSetId() {
        return setId;
    }

    public void setSetId(String setId) {
        this.setId = setId;
    }

    public double getSetScore() {
        return setScore;
    }

    public void setSetScore(double setScore) {
        this.setScore = setScore;
    }

    public int getItemCount() {
        return itemCount;
    }

    public void setItemCount(int itemCount) {
        this.itemCount = itemCount;
    }
}
