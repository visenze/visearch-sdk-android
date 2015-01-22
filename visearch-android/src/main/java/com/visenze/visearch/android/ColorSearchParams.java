package com.visenze.visearch.android;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Sets the color search parameters
 */
public class ColorSearchParams extends SearchParams {
    private String color;

    public ColorSearchParams() {
        super();
    }

    /**
     * Construct with a color code
     *
     * @param color color code in Hexadecimal.
     */
    public ColorSearchParams(String color) {
        super();
        this.color = color;
    }

    /**
     * Set the color code for search
     *
     * @param color color code in Hexadecimal.
     * @return this instance.
     */
    public ColorSearchParams setColor(String color) {
        this.color = color;
        return this;
    }

    /**
     * Get the color code set for search
     *
     * @return color code in Hexadecimal.
     */
    public String getColor() {
        return color;
    }

    @Override
    public Map<String, List<String>> toMap() {
        Map<String, List<String> > map = super.toMap();
        putStringInMap(map, "color", color);
        return map;
    }

    private void putStringInMap(Map<String, List<String> > map, String key, String value) {
        List<String> stringList = new ArrayList<>();
        stringList.add(value);

        map.put(key, stringList);
    }
}
