package com.visenze.visearch.android.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Map;

public class ProductResponse {

    @SerializedName("status")
    private String status;

    @SerializedName("im_id")
    private String imId;

    @SerializedName("method")
    private String method;

    @SerializedName("error")
    private ErrorData error;

    @SerializedName("product_types")
    private List<ProductType> productTypes;

    @SerializedName("result")
    private List<Product> products;

    @SerializedName("group_results")
    private List<GroupProductResult> groupResults;

    @SerializedName("objects")
    private List<ProductObject> objects;

    @SerializedName("catalog_fields_mapping")
    private Map<String, String> catalogFieldsMapping;

    @SerializedName("facets")
    private List<Facet> facets;

    @SerializedName("page")
    private int page;

    @SerializedName("limit")
    private int limit;

    @SerializedName("total")
    private int total;

    @SerializedName("reqid")
    private String reqId;

    @SerializedName("alt_limit")
    private Integer altLimit;

    @SerializedName("strategy")
    private Strategy strategy;

    @SerializedName("experiment")
    private Experiment experiment;

    @SerializedName("excluded_pids")
    private List<String> excludedPids;

    @SerializedName("set_info")
    private List<SetInfo> setInfoList;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getImId() {
        return imId;
    }

    public void setImId(String imId) {
        this.imId = imId;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public ErrorData getError() {
        return error;
    }

    public void setError(ErrorData error) {
        this.error = error;
    }

    public List<ProductType> getProductTypes() {
        return productTypes;
    }

    public void setProductTypes(List<ProductType> productTypes) {
        this.productTypes = productTypes;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public List<ProductObject> getObjects() {
        return objects;
    }

    public void setObjects(List<ProductObject> objects) {
        this.objects = objects;
    }

    public Map<String, String> getCatalogFieldsMapping() {
        return catalogFieldsMapping;
    }

    public void setCatalogFieldsMapping(Map<String, String> catalogFieldsMapping) {
        this.catalogFieldsMapping = catalogFieldsMapping;
    }

    public List<Facet> getFacets() {
        return facets;
    }

    public void setFacets(List<Facet> facets) {
        this.facets = facets;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getReqId() {
        return reqId;
    }

    public void setReqId(String reqId) {
        this.reqId = reqId;
    }

    public Integer getAltLimit() {
        return altLimit;
    }

    public void setAltLimit(Integer altLimit) {
        this.altLimit = altLimit;
    }

    public Strategy getStrategy() {
        return strategy;
    }

    public void setStrategy(Strategy strategy) {
        this.strategy = strategy;
    }

    public Experiment getExperiment() {
        return experiment;
    }

    public void setExperiment(Experiment experiment) {
        this.experiment = experiment;
    }

    /**
     *
     * @return whether the results are empty due to A/B test setting of returning no recommendations
     */
    public boolean experimentNoRecommendation() {
        return (experiment != null && experiment.isExpNoRecommendation());
    }

    public List<String> getExcludedPids() {
        return excludedPids;
    }

    public void setExcludedPids(List<String> excludedPids) {
        this.excludedPids = excludedPids;
    }

    public List<SetInfo> getSetInfoList() {
        return setInfoList;
    }

    public void setSetInfoList(List<SetInfo> setInfoList) {
        this.setInfoList = setInfoList;
    }

    public List<GroupProductResult> getGroupResults() {
        return groupResults;
    }

    public void setGroupResults(List<GroupProductResult> groupResults) {
        this.groupResults = groupResults;
    }
}
