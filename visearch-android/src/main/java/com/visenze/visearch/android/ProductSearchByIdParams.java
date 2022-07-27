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

    public ProductSearchByIdParams(String productId) {
        this.productId = productId;
    }

}
