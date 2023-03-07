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

    private Boolean showPinnedImNames;

    private Boolean showExcludedImNames;

    private Integer setLimit;
    private Boolean useSetBasedCtl;
    private Boolean showBestProductImages;

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

    public Boolean getShowPinnedImNames() {
        return showPinnedImNames;
    }

    public void setShowPinnedImNames(Boolean showPinnedImNames) {
        this.showPinnedImNames = showPinnedImNames;
    }

    public Boolean getShowExcludedImNames() {
        return showExcludedImNames;
    }

    public void setShowExcludedImNames(Boolean showExcludedImNames) {
        this.showExcludedImNames = showExcludedImNames;
    }

    public Integer getSetLimit() {
        return setLimit;
    }

    public void setSetLimit(Integer setLimit) {
        this.setLimit = setLimit;
    }

    public Boolean getUseSetBasedCtl() {
        return useSetBasedCtl;
    }

    public void setUseSetBasedCtl(Boolean useSetBasedCtl) {
        this.useSetBasedCtl = useSetBasedCtl;
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

        if (showPinnedImNames != null) {
            putStringInMap(map, "show_pinned_im_names", showPinnedImNames.toString());
        }

        if (showExcludedImNames != null) {
            putStringInMap(map, "show_excluded_im_names", showExcludedImNames.toString());
        }

        if (setLimit != null) {
            putStringInMap(map, "set_limit", String.valueOf(setLimit));
        }

        if (useSetBasedCtl != null) {
            putStringInMap(map, "use_set_based_ctl", useSetBasedCtl.toString());
        }

        if (showBestProductImages != null) {
            putStringInMap(map, "show_best_product_images", showBestProductImages.toString());
        }

        return map;
    }

}
