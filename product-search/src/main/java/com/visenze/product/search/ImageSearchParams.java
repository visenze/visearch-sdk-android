package com.visenze.product.search;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.visenze.product.search.model.Image;

import java.util.List;

public class ImageSearchParams extends BaseSearchParams {
    @SerializedName("im_url")
    private String imUrl;

    @SerializedName("im_id")
    private String imId;

    @SerializedName("image")
    @Expose(deserialize = false, serialize = false)
    private Image image;

    @SerializedName("box")
    private int[] box;

    @SerializedName("detection")
    private String detection;

    @SerializedName("detection_limit")
    private Integer detectionLimit;

    @SerializedName("detection_sensitivity")
    private String detectionSensitivity;

    public String getImUrl() {
        return imUrl;
    }

    public void setImUrl(String imUrl) {
        this.imUrl = imUrl;
    }

    public String getImId() {
        return imId;
    }

    public void setImId(String imId) {
        this.imId = imId;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public int[] getBox() {
        return box;
    }

    public void setBox(int[] box) {
        this.box = box;
    }

    public String getDetection() {
        return detection;
    }

    public void setDetection(String detection) {
        this.detection = detection;
    }

    public Integer getDetectionLimit() {
        return detectionLimit;
    }

    public void setDetectionLimit(Integer detectionLimit) {
        this.detectionLimit = detectionLimit;
    }

    public String getDetectionSensitivity() {
        return detectionSensitivity;
    }

    public void setDetectionSensitivity(String detectionSensitivity) {
        this.detectionSensitivity = detectionSensitivity;
    }

    public ImageSearchParams() {
        super();
    }

    public ImageSearchParams(String imUrl) {
        super();
        this.imUrl = imUrl;
    }

    public ImageSearchParams(Image image) {
        super();
        this.image = image;
    }

}
