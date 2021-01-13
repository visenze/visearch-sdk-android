package com.visenze.visearch.android.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by visenze on 14/9/15.
 */
public class ImageResult {


    @SerializedName("im_name")
    private String imageName;

    @SerializedName("image_url")
    @Expose(deserialize = false, serialize = false)
    private String imageUrl;

    @SerializedName("score")
    private Float score;

    @SerializedName("value_map")
    private Map<String, String> fieldList;

    @SerializedName("s3_url")
    private String s3Url;



    public ImageResult() {
        this.fieldList = new HashMap<String, String>();
    }

    public ImageResult(String imageName, String imageUrl, Float score, Map<String, String> filedList) {
        this.imageName = imageName;
        this.imageUrl = imageUrl;
        this.score = score;
        this.fieldList = filedList;
    }

    /**
     * Get image id
     *
     * @return image id.
     */
    public String getImageName() {
        return imageName;
    }

    /**
     * Get image url, null if the field parameter is not set
     *
     * @return image url
     */
    public String getImageUrl() {
        if(imageUrl != null) return imageUrl;

        if(fieldList !=null) {
            imageUrl = fieldList.get("im_url");
        }
        return imageUrl;
    }

    /**
     * Get search score for the image, null if the field parameter is not set
     *
     * @return search score.
     */
    public Float getScore() {
        try {
            return score;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Get field list as set in the search parameter
     *
     * @return field list.
     */
    public Map<String, String> getMetaData() {
        return fieldList;
    }

    public String getS3Url() {
        return s3Url;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public void setImageUrl(String imageUrl) {

        this.imageUrl = imageUrl;
    }

    public void setScore(Float score) {

        this.score = score;

    }

    public void setFieldList(Map<String, String> fieldList) {
        this.fieldList = fieldList;
    }

    public void setS3Url(String s3Url) {
        this.s3Url = s3Url;
    }
}
