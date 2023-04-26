package com.visenze.visearch.android.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ProductObject extends ProductType {
    @SerializedName("result")
    private List<Product> result;

    @SerializedName("group_results")
    private List<GroupProductResult> groupResults;

    @SerializedName("total")
    private Integer total;

    @SerializedName("id")
    private String id;

    @SerializedName("category")
    private String category;

    @SerializedName("name")
    private String name;

    @SerializedName("excluded_pids")
    private List<String> excludedPids;

    @SerializedName("facets")
    private List<Facet> facets;

    public List<Product> getResult() {
        return result;
    }

    public void setResult(List<Product> result) {
        this.result = result;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getExcludedPids() {
        return excludedPids;
    }

    public void setExcludedPids(List<String> excludedPids) {
        this.excludedPids = excludedPids;
    }

    public List<Facet> getFacets() {
        return facets;
    }

    public void setFacets(List<Facet> facets) {
        this.facets = facets;
    }

    public List<GroupProductResult> getGroupResults() {
        return groupResults;
    }

    public void setGroupResults(List<GroupProductResult> groupResults) {
        this.groupResults = groupResults;
    }
}
