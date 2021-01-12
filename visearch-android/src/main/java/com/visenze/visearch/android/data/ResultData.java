package com.visenze.visearch.android.data;

import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

import java.util.Map;

public class ResultData {

    @SerializedName("im_name")
    private String imName;

    @SerializedName("score")
    private float score;

    @SerializedName("value_map")
    private Map<String, String> valueMap;

    @SerializedName("s3_url")
    private String s3Url;


    public void setImName(String imName) {
        this.imName = imName;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public void setValueMap(Map<String, String> valueMap) {
        this.valueMap = valueMap;
    }

    public void setS3Url(String s3Url) {
        this.s3Url = s3Url;
    }


    public String getImName() {
        return imName;
    }

    public float getScore() {
        return score;
    }

    public Map<String, String> getValueMap() {
        return valueMap;
    }

    public String getS3Url() {
        return s3Url;
    }



}
