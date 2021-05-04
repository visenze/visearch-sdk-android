package com.visenze.visearch.android.model;

import com.google.gson.annotations.SerializedName;
import com.visenze.visearch.android.ResultList;
import com.visenze.visearch.android.ViSearchException;
import com.visenze.visearch.android.model.Box;
import com.visenze.visearch.android.model.ImageResult;
import com.visenze.visearch.android.model.ObjectResult;
import com.visenze.visearch.android.model.ProductType;
import com.visenze.visearch.android.model.TagGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import retrofit2.http.PartMap;

public class ResponseData {

    @SerializedName("status")
    private String status;

    @SerializedName("im_id")
    private String imId;

    @SerializedName("method")
    private String method;

    @SerializedName("error")
    private String[] error;

    @SerializedName("page")
    private int page;

    @SerializedName("limit")
    private int limit;

    @SerializedName("total")
    private int total;

    @SerializedName("transId")
    private String transId;

    @SerializedName("reqid")
    private String reqId;

    @SerializedName("result")
    private List<ImageResult> results;

    @SerializedName("product_types")
    private List<ProductType> productTypes;

    @SerializedName("product_types_list")
    private List<ProductType> productTypesList;

    @SerializedName("query_tags")
    private List<TagGroup> queryTags;

    @SerializedName("objects")
    private List<ObjectResult> objects;

    @SerializedName("qinfo")
    private Map<String, String> qinfo;

    @SerializedName("facets")
    private List<Facet> facets;

    public void setStatus(String status) {
        this.status = status;
    }

    public void setImId(String imId) {
        this.imId = imId;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public void setError(String[] error) {
        this.error = error;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public void setTransId(String transId) {
        this.transId = transId;
    }

    public void setReqId(String reqId) {
        this.reqId = reqId;
    }

    public void setResults(List<ImageResult> results) {
        this.results = results;
    }

    public void setProductTypes(List<ProductType> productTypes) {
        this.productTypes = productTypes;
    }

    public void setProductTypesList(List<ProductType> productTypesList) {
        this.productTypesList = productTypesList;
    }

    public void setQueryTags(List<TagGroup> queryTags) {
        this.queryTags = queryTags;
    }

    public void setObjects(List<ObjectResult> objects) {
        this.objects = objects;
    }

    public void setQinfo(Map<String, String> qinfo) {
        this.qinfo = qinfo;
    }

    public void setFacets(List<Facet> facets) {
        this.facets = facets;
    }

    public List<Facet> getFacets() {
        return facets;
    }

    public String getStatus() {
        return status;
    }

    public String getMethod() {
        return method;
    }

    public String[] getError() {
        return error;
    }

    public int getPage() {
        return page;
    }

    public int getLimit() {
        return limit;
    }

    public int getTotoal() {
        return total;
    }

    public List<ProductType> getProductTypes() {
        return productTypes;
    }

    public List<ProductType> getProductTypesList() {
        return productTypesList;
    }

    public List<ImageResult> getResults() {
        return results;
    }

    public List<ObjectResult> getObjects() {
        return objects;
    }
    public ResultList getResultList() {
        ResultList resultList = new ResultList();
        if(error != null && error.length > 0) {
            resultList.setErrorMessage(error[0]);
        }

        resultList.setTotal(total);
        resultList.setPage(page);
        resultList.setLimit(limit);
        resultList.setTransId(transId);
        resultList.setQueryInfo(qinfo);

        resultList.setImageList(results);
        resultList.setObjects(objects);

        resultList.setProductTypes(productTypes);
        resultList.setSupportedProductTypeList(productTypesList);

        resultList.setQueryTags(queryTags);
        resultList.setImId(imId);
        resultList.setReqid(reqId);

        resultList.setFacets(facets);
        return resultList;
    }

}
