package com.visenze.visearch.android.model;

/**
 * Created by visenze on 14/9/15.
 *
 * @author yulu
 */
public class ProductType {
    private String type;
    private Double score;
    private Box box;

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
        return box;
    }

    public void setBox(Box box) {
        this.box = box;
    }
}
