package com.visenze.visearch.android;

import com.visenze.visearch.android.model.ImageResult;
import com.visenze.visearch.android.model.ProductType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The ResultList class represents a successful search result
 * In practice you will never need to initialise a Result by yourself. Instead you should implement
 * {@link ViSearch.ResultListener ResultListener} to get the returned result from a search session.
 */
public class ResultList {

    private Integer page;

    private Integer limit;

    private Integer total;

    private String errorMessage;

    private String transId;

    private List<ImageResult> imageResult;

    private Map<String, String> queryInfo;

    private List<ProductType> productTypes;

    private List<ProductType> supportedProductTypeList;

    private String imId;

    public ResultList() {
        imageResult = new ArrayList<ImageResult>();
        queryInfo = new HashMap<String, String>();
    }

    public ResultList(Integer page, Integer limit, Integer total, String errorMessage, List<ImageResult> imageResult, Map<String, String> queryInfo) {
        this.page = page;
        this.limit = limit;
        this.total = total;
        this.errorMessage = errorMessage;
        this.imageResult = imageResult;
        this.queryInfo = queryInfo;
    }

    /**
     * Get the page number of the search result
     *
     * @return page number.
     */
    public int getPage() {
        return page;
    }

    /**
     * Get the limit for the number of items per page for the search result
     *
     * @return limit for the number of items per page.
     */
    public int getPageLimit() {
        return limit;
    }

    /**
     * Get the total number of items in the search result
     *
     * @return total number of items.
     */
    public int getTotal() {
        return total;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }

    /**
     * Get the list of Image {@link ImageResult ImageResult}
     *
     * @return image list.
     */
    public List<ImageResult> getImageList() {
        return imageResult;
    }


    /**
     * Get the list of Product Type (detection result) {@link ProductType}
     *
     * @return product type list
     */
    public List<ProductType> getProductTypes() {
        return productTypes;
    }


    /**
     * Get the list of Supported Product Type (supported categories) {@link ProductType}
     *
     * @return product type list
     */
    public List<ProductType> getSupportedProductTypeList() {
        return supportedProductTypeList;
    }

    /**
     * Get the query information of the search
     *
     * @return query information.
     */
    public Map<String, String> getQueryInfo() {
        return queryInfo;
    }

    /**
     * Get the trans id of the search
     * @return trans id
     */
    public String getTransId() {
        return transId;
    }

    /**
     * Get im id for uploadsearch
     * @return imId
     */
    public String getImId() {
        return imId;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public void setImageList(List<ImageResult> imageResult) {
        this.imageResult = imageResult;
    }

    public void setQueryInfo(Map<String, String> queryInfo) {
        this.queryInfo = queryInfo;
    }

    public void setProductTypes(List<ProductType> productTypes) {
        this.productTypes = productTypes;
    }

    public void setSupportedProductTypeList(List<ProductType> supportedProductTypeList) {
        this.supportedProductTypeList = supportedProductTypeList;
    }

    public void setTransId(String transId) {
        this.transId = transId;
    }

    public void setImId(String imId) {
        this.imId = imId;
    }
}
