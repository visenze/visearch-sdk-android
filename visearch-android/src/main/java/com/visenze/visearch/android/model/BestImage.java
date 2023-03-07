package com.visenze.visearch.android.model;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

public class BestImage implements Serializable {

    @SerializedName("type")
    private String type;

    @SerializedName("url")
    private String url;

    @SerializedName("index")
    private String index;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }
}
