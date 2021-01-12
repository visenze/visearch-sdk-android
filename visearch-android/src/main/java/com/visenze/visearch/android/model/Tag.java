package com.visenze.visearch.android.model;

import com.google.gson.annotations.SerializedName;

// for Instance Recognition API response in /uploadsearch
public class Tag {

    @SerializedName("tag")
    private String tag;

    @SerializedName("score")
    private double score;

    public Tag(String tag, float score) {
        this.tag = tag;
        this.score = score;
    }

    public Tag() {
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }
}
