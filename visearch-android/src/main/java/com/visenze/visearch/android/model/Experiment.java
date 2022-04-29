package com.visenze.visearch.android.model;

import com.google.gson.annotations.SerializedName;

public class Experiment {

    @SerializedName("experiment_id")
    private int experimentId;

    @SerializedName("variant_id")
    private int variantId;

    @SerializedName("variant_name")
    private String variantName;

    @SerializedName("strategy_id")
    private Integer strategyId;

    @SerializedName("experiment_no_recommendation")
    private boolean expNoRecommendation;

    public int getExperimentId() {
        return experimentId;
    }

    public void setExperimentId(int experimentId) {
        this.experimentId = experimentId;
    }

    public int getVariantId() {
        return variantId;
    }

    public void setVariantId(int variantId) {
        this.variantId = variantId;
    }

    public String getVariantName() {
        return variantName;
    }

    public void setVariantName(String variantName) {
        this.variantName = variantName;
    }

    public Integer getStrategyId() {
        return strategyId;
    }

    public void setStrategyId(Integer strategyId) {
        this.strategyId = strategyId;
    }

    public boolean isExpNoRecommendation() {
        return expNoRecommendation;
    }

    public void setExpNoRecommendation(boolean expNoRecommendation) {
        this.expNoRecommendation = expNoRecommendation;
    }

}
