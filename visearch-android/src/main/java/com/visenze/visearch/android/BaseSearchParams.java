package com.visenze.visearch.android;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Sets the basic search parameters
 */
public class BaseSearchParams {
    private Integer page;

    private Integer limit;

    private List<String> fl;

    private Map<String, String> fq;

    private Boolean score;

    private Boolean queryInfo;

    /**
     * The default sets limit at 10 and page at 1, other basic parameters are set as null
     */
    public BaseSearchParams() {
        this.limit = 10;
        this.page = 1;
        this.score = null;
        this.fl = null;
        this.fq = null;
        this.queryInfo = null;
    }

    /**
     * Set the limit of items per page
     *
     * @param limit limit of items per page.
     * @return this instance
     */
    public BaseSearchParams setLimit(int limit) {
        this.limit = limit;
        return this;
    }

    /**
     * Set the page number of the request result
     *
     * @param page page number.
     * @return this instance.
     */
    public BaseSearchParams setPage(int page) {
        this.page = page;
        return this;
    }

    /**
     * Set the requirement for image score
     *
     * @param score set true or false for the requirement, true - search score should be returned.
     * @return this instance.
     */
    public BaseSearchParams setScore(Boolean score) {
        this.score = score;
        return this;
    }

    /**
     * Set the requirement for query information
     *
     * @param queryInfo set true for false for the requirement, true - query information should be returned
     * @return this instance.
     */
    public BaseSearchParams setQueryInfo(Boolean queryInfo) {
        this.queryInfo = queryInfo;
        return this;
    }

    /**
     * Set the filter queries:
     * Schema parameters that are set as searchable can be used as filter queries in search
     *
     * @param fq filter queries.
     * @return this instance.
     */
    public BaseSearchParams setFq(Map<String, String> fq) {
        this.fq = fq;
        return this;
    }

    /**
     * Set the field list:
     * Request the search to return the specified schema parameters
     *
     * @param fl field list
     * @return this instance.
     */
    public BaseSearchParams setFl(List<String> fl) {
        this.fl = fl;
        return this;
    }

    /**
     * Get the limit value
     *
     * @return limit of items per page
     */
    public Integer getLimit() {
        return limit;
    }

    /**
     * Get the page numberfq
     *
     * @return page number
     */
    public Integer getPage() {
        return page;
    }

    /**
     * Get the query information requirement
     *
     * @return query information requirement, true - query information should be returned.
     */
    public Boolean isQueryInfo() {
        return queryInfo;
    }

    /**
     * Get the search score requirement
     *
     * @return search score requirement, true - search score should be returned.
     */
    public Boolean isScore() {
        return score;
    }

    /**
     * Get the filter queries
     *
     * @return filter queries.
     */
    public Map<String, String> getFq() {
        return fq;
    }

    /**
     * Get the field list
     *
     * @return field list.
     */
    public List<String> getFl() {
        return fl;
    }

    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<String, String>();

        if (limit != null && limit > 0) {
            map.put("limit", limit.toString());
        }

        if (page != null && page > 0) {
            map.put("page", page.toString());
        }

        if (score != null) {
            map.put("score", String.valueOf(score));
        }

        if (queryInfo != null) {
            map.put("qinfo", String.valueOf(queryInfo));
        }

        if (fq != null && fq.size() > 0) {
            StringBuilder builder = new StringBuilder();
            for (Map.Entry<String, String> entry : fq.entrySet()) {
                builder.append(entry.getKey())
                        .append(":")
                        .append(entry.getValue())
                        .append(",");
            }
            //remove the last ','
            String s = builder.toString().substring(0, builder.length() - 1);

            map.put("fq", s);
        }

        if (fl != null && fl.size() > 0) {
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < fl.size(); i++) {
                builder.append(fl.get(i));
                if (i < fl.size() - 1) {
                    builder.append(',');
                }
            }
            map.put("fl", builder.toString());
        }

        return map;
    }
}
