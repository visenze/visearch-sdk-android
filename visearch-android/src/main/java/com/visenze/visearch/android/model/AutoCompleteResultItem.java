package com.visenze.visearch.android.model;

import com.google.gson.annotations.SerializedName;

public class AutoCompleteResultItem {

    @SerializedName("text")
    private String text;

    @SerializedName("score")
    private double score;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }
}
