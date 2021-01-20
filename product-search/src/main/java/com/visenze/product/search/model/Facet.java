package com.visenze.product.search.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Facet {
    @SerializedName("key")
    private String key;

    @SerializedName("items")
    private List<FacetItem> items;

    @SerializedName("range")
    private FacetRange range;


    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public List<FacetItem> getItems() {
        return items;
    }

    public void setItems(List<FacetItem> items) {
        this.items = items;
    }

    public FacetRange getRange() {
        return range;
    }

    public void setRange(FacetRange range) {
        this.range = range;
    }



}
