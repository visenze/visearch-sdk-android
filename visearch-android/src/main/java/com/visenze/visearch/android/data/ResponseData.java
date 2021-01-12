package com.visenze.visearch.android.data;

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
    private List<ResultData> results;

    @SerializedName("product_types")
    private List<ProductTypeData> productTypes;

    @SerializedName("product_types_list")
    private List<ProductTypesListData> productTypesList;

    @SerializedName("query_tags")
    private List<TagGroup> queryTags;

    @SerializedName("objects")
    private List<ObjectData> objects;

    @SerializedName("qinfo")
    private Map<String, String> qinfo;

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

    public void setResults(List<ResultData> results) {
        this.results = results;
    }

    public void setProductTypes(List<ProductTypeData> productTypes) {
        this.productTypes = productTypes;
    }

    public void setProductTypesList(List<ProductTypesListData> productTypesList) {
        this.productTypesList = productTypesList;
    }

    public void setQueryTags(List<TagGroup> queryTags) {
        this.queryTags = queryTags;
    }

    public void setObjects(List<ObjectData> objects) {
        this.objects = objects;
    }

    public void setQinfo(Map<String, String> qinfo) {
        this.qinfo = qinfo;
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

    public List<ProductTypeData> getProductTypes() {
        return productTypes;
    }

    public List<ProductTypesListData> getProductTypesList() {
        return productTypesList;
    }

    public List<ResultData> getResults() {
        return results;
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

        resultList.setImageList(parseImageResult(results));
        resultList.setObjects(parseObjects(objects));

        resultList.setProductTypes(parseProductType(productTypes));
        resultList.setSupportedProductTypeList(parseSupportedProductTypeList(productTypesList));

        resultList.setQueryTags(queryTags);
        resultList.setImId(imId);
        resultList.setReqid(reqId);
        return resultList;
    }


    private List<ImageResult> parseImageResult(List<ResultData> resultData) {
        if(resultData == null || resultData.isEmpty()) {
            return null;
        }

        List<ImageResult> resultList = new ArrayList<ImageResult>();

        for(int i=0; i< resultData.size(); i++) {

            ResultData data = resultData.get(i);
            ImageResult imageResult = new ImageResult();
            imageResult.setImageName(data.getImName());
            imageResult.setScore(data.getScore());
            imageResult.setS3Url(data.getS3Url());


            Map<String, String> valueMap = data.getValueMap();
            if(valueMap != null && valueMap.get("im_url")!= null) {
                imageResult.setImageUrl(valueMap.get("im_url"));
            }
            imageResult.setFieldList(valueMap);

            resultList.add(imageResult);
        }
        return resultList;

    }

    private List<ObjectResult> parseObjects(List<ObjectData> objectDataList) {
        if(objectDataList == null || objectDataList.isEmpty()) {
            return null;
        }

        List<ObjectResult> objects = new ArrayList<ObjectResult>();

        for(int i=0; i<objectDataList.size(); i++) {
            ObjectData objectData = objectDataList.get(i);
            ObjectResult objectResult = new ObjectResult();
            objectResult.setType(objectData.getType());
            objectResult.setScore(objectData.getScore());
            objectResult.setBox(parseBox(objectData.getBox()));
            objectResult.setTotal(objectData.getTotal());
            objectResult.setAttributeList(objectData.getAttributes());
            objectResult.setResult(parseImageResult(objectData.getResult()));

            objects.add(objectResult);
        }
        return objects;
    }


    private Box parseBox(int[] boxData) {

        if(boxData != null && boxData.length > 3) {
            Box box = new Box(boxData[0], boxData[1], boxData[2], boxData[3]);
            return box;
        }

        return null;
    }

    private List<ProductType> parseProductType(List<ProductTypeData> productTypeDataList) {
        if(productTypeDataList == null || productTypeDataList.isEmpty()) return null;

        List<ProductType> resultList = new ArrayList<ProductType>();

        for (int i = 0; i < productTypeDataList.size(); i++) {
            ProductTypeData data = productTypeDataList.get(i);
            ProductType productType = new ProductType();
            productType.setType(data.getType());
            productType.setScore(data.getScore());
            productType.setBox(parseBox(data.getBox()));
            productType.setAttributeList(data.getAttributes());
            resultList.add(productType);
        }

        return resultList;
    }

    private List<ProductType> parseSupportedProductTypeList(List<ProductTypesListData> productTypesList) {
        if(productTypesList == null || productTypesList.isEmpty()) return null;

        List<ProductType> supportedTypeList = new ArrayList<>();
        for(int i=0; i<productTypesList.size(); i++) {
            ProductTypesListData data = productTypesList.get(i);
            ProductType productType = new ProductType();
            productType.setType(data.getType());
            productType.setAttributeList(data.getAttributesList());

            supportedTypeList.add(productType);
        }
        return supportedTypeList;
    }

}
