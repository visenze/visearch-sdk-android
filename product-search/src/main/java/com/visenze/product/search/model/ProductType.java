package com.visenze.product.search.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Map;

public class ProductType {

    @SerializedName("box")
    private int[] boxArray;

    @SerializedName("boxObj")
    @Expose(deserialize = false, serialize = false)
    private Box box;


    @SerializedName("type")
    private String type;

    @SerializedName("score")
    private double score;


    @SerializedName("attributes")
    private Map attributeList;


    public ProductType() {

    }

    public ProductType(String type, Double score, Box box) {
        this.type = type;
        this.score = score;
        this.box = box;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }


    public Box getBox() {
        if(box != null) return box;
        box = parseBox(boxArray);
        return box;
    }

    public void setBox(Box box) {
        this.box = box;
    }


    private Box parseBox(int[] boxData) {

        if(boxData != null && boxData.length > 3) {
            Box box = new Box(boxData[0], boxData[1], boxData[2], boxData[3]);
            return box;
        }

        return null;
    }

    public Map getAttributeList() {
        return attributeList;
    }

    public void setAttributeList(Map attributeList) {
        this.attributeList = attributeList;
    }


}
