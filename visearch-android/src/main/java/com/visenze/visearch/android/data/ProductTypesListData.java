package com.visenze.visearch.android.data;

import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

import java.util.List;
import java.util.Map;

public class ProductTypesListData {

    @SerializedName("type")
    private String type;

    @SerializedName("attributes_list")
    private Map attributesList;
    

    public void setType(String type) {
        this.type = type;
    }

    public void setAttributesList(Map attributesList) {
        this.attributesList = attributesList;
    }

    public String getType() {
        return type;
    }

    public Map getAttributesList() {
        return attributesList;
    }


}
