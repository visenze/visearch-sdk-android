package com.visenze.visearch.android;


import android.util.Log;

import com.visenze.visearch.android.model.Image;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Set upload search parameters
 * Upload search can be initialised with an image attached or an image url.
 */
public class UploadSearchParams extends SearchParams {

    private Image image;

    private String imageUrl;

    public UploadSearchParams() {
        super();
    }

    /**
     * Construct with an image url.
     *
     * @param imageUrl image url.
     */
    public UploadSearchParams(String imageUrl) {
        super();
        this.imageUrl = imageUrl;
    }

    /**
     * Construct with an {@link Image Image}
     *
     * @param image {@link Image Image} instance, handles image decode and optimisation
     */
    public UploadSearchParams(Image image) {
        super();
        this.image = image;
    }

    /**
     * Set the {@link Image Image} to upload
     *
     * @param image {@link Image Image} instance.
     * @return this instance.
     */
    public UploadSearchParams setImage(Image image) {
        //free memory
        this.image = image;

        return this;
    }

    /**
     * Set image url
     *
     * @param imageUrl image url.
     * @return this instance.
     */
    public UploadSearchParams setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }

    /**
     * Get {@link Image Image} that is set to search
     *
     * @return {@link Image Image} instance
     */
    public Image getImage() {
        return image;
    }

    /**
     * Get image url that is set to search
     *
     * @return image url.
     */
    public String getImageUrl() {
        return imageUrl;
    }

    @Override
    public Map<String, List<String> > toMap() {
        Map<String, List<String> > map = super.toMap();

        if (image.getBox() != null) {
            if (image.getBox().getX1() != null && image.getBox().getX2() != null &&
                    image.getBox().getY1() != null && image.getBox().getY2() != null) {
                putStringInMap(map, "box", image.getBox().getX1() + "," + image.getBox().getY1() + "," + image.getBox().getX2() + "," + image.getBox().getY2());
                Log.d("Upload params", "box size: " + image.getBox().getX1() + ", " + image.getBox().getY1() + ", " + image.getBox().getX2() + ", " + image.getBox().getY2());
            }
        }

        if (imageUrl != null) {
            putStringInMap(map, "im_url", imageUrl);
        }

        return map;
    }

    private void putStringInMap(Map<String, List<String> > map, String key, String value) {
        List<String> stringList = new ArrayList<>();
        stringList.add(value);

        map.put(key, stringList);
    }
}
