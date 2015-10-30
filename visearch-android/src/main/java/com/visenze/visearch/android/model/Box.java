package com.visenze.visearch.android.model;

/**
 * Created by visenze on 14/9/15.
 *
 * @author yulu
 */
/**
 * Region selected on the image for search
 */
public class Box {
    private Integer x1;

    private Integer x2;

    private Integer y1;

    private Integer y2;

    /**
     * Construct with coordinates
     *
     * @param x1 top-left corner x-coordinate.
     * @param y1 top-left corner y-coordinate.
     * @param x2 bottom-right corner x-coordinate.
     * @param y2 bottom-right corner y-coordinate.
     */
    public Box(Integer x1, Integer y1, Integer x2, Integer y2) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }

    /**
     * Set x1: top-left corner x-coordinate.
     *
     * @param x1 top-left corner x-coordinate.
     * @return this instance.
     */
    public Box setX1(Integer x1) {
        this.x1 = x1;
        return this;
    }

    /**
     * Set y1: top-left corner y-coordinate.
     *
     * @param y1 top-left corner y-coordinate.
     * @return this instance.
     */
    public Box setY1(Integer y1) {
        this.y1 = y1;
        return this;
    }

    /**
     * Set x2: bottom-right corner x-coordinate.
     *
     * @param x2 bottom-right corner x-coordinate.
     * @return this instance.
     */
    public Box setX2(Integer x2) {
        this.x2 = x2;
        return this;
    }

    /**
     * Set y2: bottom-right corner y-coordinate.
     *
     * @param y2 bottom-right corner y-coordinate.
     * @return this instance.
     */
    public Box setY2(Integer y2) {
        this.y2 = y2;
        return this;
    }

    /**
     * Get x1: top-left corner x-coordinate.
     *
     * @return x1 top-left corner x-coordinate.
     */
    public Integer getX1() {
        return x1;
    }

    /**
     * Get y1: top-left corner y-coordinate.
     *
     * @return y1 top-left corner y-coordinate.
     */
    public Integer getY1() {
        return y1;
    }

    /**
     * Get x2: bottom-right corner x-coordinate.
     *
     * @return x2 bottom-right corner x-coordinate.
     */
    public Integer getX2() {
        return x2;
    }

    /**
     * Get y2: bottom-right corner y-coordinate.
     *
     * @return y2 bottom-right corner y-coordinate.
     */
    public Integer getY2() {
        return y2;
    }
}
