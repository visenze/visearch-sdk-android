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
