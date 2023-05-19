package com.visenze.visearch.android.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Map;

/**
 * Created by visenze on 14/9/15.
 *
 * @author yulu
 */
public class ProductType {

    @SerializedName("box")
    private int[] boxArray;

    @SerializedName("box_type")
    private String boxType;

    @SerializedName("type")
    private String type;

    @SerializedName("score")
    private double score;

    @SerializedName("attributes")
    private Map attributeList;

    @SerializedName("boxObj")
    @Expose(deserialize = false, serialize = false)
    private Box box;


    public ProductType() {

    }

    public ProductType(String type, Double score, Box box, Map attributeList) {
        this.type = type;
        this.score = score;
        this.box = box;
        this.attributeList = attributeList;
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

    public Map getAttributeList() {
        return attributeList;
    }

    public void setAttributeList(Map attributeList) {
        this.attributeList = attributeList;
    }

    public String getBoxType() {
        return boxType;
    }

    public void setBoxType(String boxType) {
        this.boxType = boxType;
    }

    private Box parseBox(int[] boxData) {

        if(boxData != null && boxData.length > 3) {
            Box box = new Box(boxData[0], boxData[1], boxData[2], boxData[3]);
            return box;
        }

        return null;
    }

}
