package com.visenze.visearch.android.data;

import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

import java.util.List;
import java.util.Map;

public class ObjectData {

    @SerializedName("box")
    private int[] box;

    @SerializedName("type")
    private String type;

    @SerializedName("score")
    private double score;

    @SerializedName("total")
    private int total;

    @SerializedName("attributes")
    private Map attributes;

    @SerializedName("result")
    private List<ResultData> result;


    public void setBox(int[] box) {
        this.box = box;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public void setAttributes(Map attributes) {
        this.attributes = attributes;
    }

    public void setResult(List<ResultData> result) {
        this.result = result;
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

    public int getTotal() {
        return total;
    }

    public Map getAttributes() {
        return attributes;
    }

    public List<ResultData> getResult() {
        return result;
    }




}
