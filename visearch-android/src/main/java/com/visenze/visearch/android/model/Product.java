package com.visenze.visearch.android.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Map;

public class Product {

    @SerializedName("product_id")
    private String productId;

    @SerializedName("main_image_url")
    private String imageUrl;

    @SerializedName("data")
    private Map<String, Object> data;

    @SerializedName("vs_data")
    private Map<String, Object> vsData;

    // only for multisearch
    @SerializedName("sys")
    private Map<String, Object> sysData;

    @SerializedName("score")
    private Float score;

    @SerializedName("pinned")
    private Boolean pinned;

    @SerializedName("image_s3_url")
    private String s3Url;

    @SerializedName("detect")
    private String detect;

    @SerializedName("keyword")
    private String keyword;

    @SerializedName("box")
    private int[] boxArray;

    @SerializedName("boxObj")
    @Expose(deserialize = false, serialize = false)
    private Box box;

    @SerializedName("tags")
    private Map<String, Object> tags;

    @SerializedName("alternatives")
    private List<Product> alternatives;

    @SerializedName("best_images")
    private List<BestImage> bestImages;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    public Map<String, Object> getVsData() {
        return vsData;
    }

    public void setVsData(Map<String, Object> vsData) {
        this.vsData = vsData;
    }

    public Map<String, Object> getSysData() {
        return sysData;
    }

    public void setSysData(Map<String, Object> sysData) {
        this.sysData = sysData;
    }

    public Float getScore() {
        return score;
    }

    public void setScore(Float score) {
        this.score = score;
    }

    public String getS3Url() {
        return s3Url;
    }

    public void setS3Url(String s3Url) {
        this.s3Url = s3Url;
    }

    public String getDetect() {
        return detect;
    }

    public void setDetect(String detect) {
        this.detect = detect;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }


    public Box getBox() {
        if(box != null) return box;
        box = parseBox(boxArray);
        return box;
    }

    public void setBox(Box box) {
        this.box = box;
    }

    private Box parseBox(int[] boxData) {

        if(boxData != null && boxData.length > 3) {
            Box box = new Box(boxData[0], boxData[1], boxData[2], boxData[3]);
            return box;
        }

        return null;
    }

    public Map<String, Object> getTags() {
        return tags;
    }

    public void setTags(Map<String, Object> tags) {
        this.tags = tags;
    }

    public List<Product> getAlternatives() {
        return alternatives;
    }

    public void setAlternatives(List<Product> alternatives) {
        this.alternatives = alternatives;
    }

    public Boolean getPinned() {
        return pinned;
    }

    public void setPinned(Boolean pinned) {
        this.pinned = pinned;
    }

    public List<BestImage> getBestImages() {
        return bestImages;
    }

    public void setBestImages(List<BestImage> bestImages) {
        this.bestImages = bestImages;
    }
}
