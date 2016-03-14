package com.visenze.visearch.android.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by visenze on 14/9/15.
 */
public class ImageResult {
    private String imageName;

    private String imageUrl;

    private Float score;

    private Map<String, String> fieldList;

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

}
