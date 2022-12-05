package com.visenze.visearch.android;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.visenze.visearch.android.model.Image;

public class ProductSearchByImageParams extends BaseProductSearchParams {

    @SerializedName("im_url")
    private String imUrl;

    @SerializedName("im_id")
    private String imId;

    @SerializedName("image")
    @Expose(deserialize = false, serialize = false)
    private Image image;

    // transient is to prevent serialization
    private transient int[] box;

    @SerializedName("detection")
    private String detection;

    @SerializedName("detection_limit")
    private Integer detectionLimit;

    @SerializedName("detection_sensitivity")
    private String detectionSensitivity;

    @SerializedName("search_all_object")
    private Boolean searchAllObjects;

    // internal serialization
    @SerializedName("box")
    private String boxParam;
    
    public Boolean getSearchAllObjects() {
        return searchAllObjects;
    }

    public void setSearchAllObjects(Boolean searchAllObjects) {
        this.searchAllObjects = searchAllObjects;
    }

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

    public void setBox(int x1, int y1, int x2, int y2) {
        this.box = new int[] {x1, y1, x2, y2};
        this.boxParam = String.format("%d,%d,%d,%d", x1, y1, x2, y2);
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

    public String getBoxParam() {
        return boxParam;
    }

    public ProductSearchByImageParams() {
        super();
    }

    public ProductSearchByImageParams(String imUrl) {
        super();
        this.imUrl = imUrl;
    }

    public ProductSearchByImageParams(Image image) {
        super();
        this.image = image;
    }

}
