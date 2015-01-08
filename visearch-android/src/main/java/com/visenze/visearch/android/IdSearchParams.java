package com.visenze.visearch.android;

import java.util.Map;

/**
 * Sets the index search parameters
 */
public class IdSearchParams extends SearchParams {

    private String imName;

    public IdSearchParams() {
        super();
    }

    /**
     * Construct with image id
     *
     * @param imName image id.
     */
    public IdSearchParams(String imName) {
        super();
        this.imName = imName;
    }

    /**
     * Set the image id for search
     *
     * @param imName image id.
     * @return this instance.
     */
    public IdSearchParams setImageName(String imName) {
        this.imName = imName;
        return this;
    }

    /**
     * Get the image id set for search
     *
     * @return image id.
     */
    public String getImageName() {
        return imName;
    }

    @Override
    public Map<String, String> toMap() {
        Map<String, String> map = super.toMap();
        map.put("im_name", imName);
        return map;
    }

}
