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
    private Error error;

    @SerializedName("product_types")
    private List<ProductType> productTypes;

    @SerializedName("result")
    private List<ProductResult> productResults;

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

    public Error getError() {
        return error;
    }

    public void setError(Error error) {
        this.error = error;
    }

    public List<ProductType> getProductTypes() {
        return productTypes;
    }

    public void setProductTypes(List<ProductType> productTypes) {
        this.productTypes = productTypes;
    }

    public List<ProductResult> getProductResults() {
        return productResults;
    }

    public void setImageResults(List<ProductResult> productResults) {
        this.productResults = productResults;
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
}
