package com.visenze.visearch.android;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The ResultList class represents a successful search result
 * <p/>
 * In practice you will never need to initialise a Result by yourself. Instead you should implement
 * {@link ViSearch.ResultListener ResultListener} to get the returned result from a search session.
 */
public class ResultList {

    private Integer page;

    private Integer limit;

    private Integer total;

    private String errorMessage;

    private List<ImageResult> imageResult;

    private Map<String, String> queryInfo;

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
     * Get the list of Image {@link com.visenze.visearch.android.ResultList.ImageResult ImageResult}
     *
     * @return image list.
     */
    public List<ImageResult> getImageList() {
        return imageResult;
    }

    /**
     * Get the query information of the search
     *
     * @return query information.
     */
    public Map<String, String> getQueryInfo() {
        return queryInfo;
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

    /**
     * Single image result
     */
    public static class ImageResult {
        private String imageName;

        private String imageUrl;

        private Double score;

        private Map<String, String> fieldList;

        public ImageResult() {
            this.fieldList = new HashMap<String, String>();
        }

        public ImageResult(String imageName, String imageUrl, Double score, Map<String, String> filedList) {
            this.imageName = imageName;
            this.imageUrl = imageUrl;
            this.score = score;
            this.fieldList = filedList;
        }

        /**
         * Get image id
         *
         * @return image id.
         */
        public String getImageName() {
            return imageName;
        }

        /**
         * Get image url, null if the field parameter is not set
         *
         * @return image url
         */
        public String getImageUrl() {
            return imageUrl;
        }

        /**
         * Get search score for the image, null if the field parameter is not set
         *
         * @return search score.
         */
        public Double getScore() {
            try {
                return score;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        /**
         * Get field list as set in the search parameter
         *
         * @return field list.
         */
        public Map<String, String> getFieldList() {

            return fieldList;
        }

        public void setImageName(String imageName) {
            this.imageName = imageName;
        }

        public void setImageUrl(String imageUrl) {

            this.imageUrl = imageUrl;
        }

        public void setScore(Double score) {

            this.score = score;

        }

        public void setFieldList(Map<String, String> fieldList) {
            this.fieldList = fieldList;
        }
    }
}
