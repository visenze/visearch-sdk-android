package com.visenze.visearch.android;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductSearchByIdParams extends BaseProductSearchParams {

    @Expose(deserialize = false, serialize = false)
    private String productId;

    @SerializedName("alt_limit")
    private Integer altLimit;

    @SerializedName("strategy_id")
    private Integer strategyId;

    @SerializedName("show_pinned_pids")
    private Boolean showPinnedPids;

    @SerializedName("show_excluded_pids")
    private Boolean showExcludedPids;

    @SerializedName("set_limit")
    private Integer setLimit;

    @SerializedName("use_set_based_ctl")
    private Boolean useSetBasedCtl;

    @SerializedName("show_best_product_images")
    private Boolean showBestProductImages;

    public String getProductId() {
        return productId;
    }

    public Integer getAltLimit() {
        return altLimit;
    }

    public void setAltLimit(Integer altLimit) {
        this.altLimit = altLimit;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public Integer getStrategyId() {
        return strategyId;
    }

    public void setStrategyId(Integer strategyId) {
        this.strategyId = strategyId;
    }

    public Boolean getShowPinnedPids() {
        return showPinnedPids;
    }

    public void setShowPinnedPids(Boolean showPinnedPids) {
        this.showPinnedPids = showPinnedPids;
    }

    public Boolean getShowExcludedPids() {
        return showExcludedPids;
    }

    public void setShowExcludedPids(Boolean showExcludedPids) {
        this.showExcludedPids = showExcludedPids;
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

    public Boolean getShowBestProductImages() {
        return showBestProductImages;
    }

    public void setShowBestProductImages(Boolean showBestProductImages) {
        this.showBestProductImages = showBestProductImages;
    }

    public ProductSearchByIdParams(String productId) {
        this.productId = productId;
    }

}
