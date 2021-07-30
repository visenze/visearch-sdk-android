package com.visenze.visearch.android;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Sets the index search parameters
 */
public class IdSearchParams extends SearchParams {

    private String imName;

    // for recommendations
    private String algorithm;

    private Integer altLimit;

    private String dedupBy;

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

    public String getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }

    public Integer getAltLimit() {
        return altLimit;
    }

    public void setAltLimit(Integer altLimit) {
        this.altLimit = altLimit;
    }

    public String getDedupBy() {
        return dedupBy;
    }

    public void setDedupBy(String dedupBy) {
        this.dedupBy = dedupBy;
    }

    @Override
    public Map<String, List<String> > toMap() {
        Map<String, List<String> > map = super.toMap();
        putStringInMap(map, "im_name", imName);

        if (algorithm != null) {
            putStringInMap(map, "algorithm", algorithm);
        }

        if (dedupBy != null) {
            putStringInMap(map, "dedup_by", dedupBy);
        }

        if (altLimit != null) {
            putStringInMap(map, "alt_limit", String.valueOf(altLimit));
        }

        return map;
    }

}
