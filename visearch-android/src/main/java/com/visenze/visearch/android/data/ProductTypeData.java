package com.visenze.visearch.android.data;

import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

import java.util.List;
import java.util.Map;


public class ProductTypeData {

    @SerializedName("box")
    private int[] box;

    @SerializedName("type")
    private String type;

    @SerializedName("score")
    private double score;

    @SerializedName("attributes")
    private Map attributes;


    public void setBox(int[] box) {
        this.box = box;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public void setAttributes(Map attributes) {
        this.attributes = attributes;
    }



    public int[] getBox() {
        return box;
    }

    public String getType() {
        return type;
    }

    public double getScore() {
        return score;
    }

    public Map getAttributes() {
        return attributes;
    }

}
