package com.visenze.visearch.android;

import java.nio.charset.Charset;
import java.util.ArrayList;
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

    private Float scoreMin;

    private Float scoreMax;

    private Boolean queryInfo;

    private Boolean getAllFl;

    private Map<String, String> custom;

    private Charset charset;

    private List<String> facets;

    private Integer facetsLimit;

    private String sortBy;

    private String strategyId;

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
        this.getAllFl = null;
        this.charset = null;
        this.facets = null;
        this.facetsLimit = 10;
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
     * Set the requirement for get all fl
     *
     * @param getAllFl set true for false for the requirement, true - all metadata should be returned
     * @return this instance.
     */
    public BaseSearchParams setGetAllFl(Boolean getAllFl) {
        this.getAllFl = getAllFl;
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
     * Set the facets list:
     * Request the search to return the specified schema parameters
     *
     * @param facets field list
     * @return this instance.
     */
    public BaseSearchParams setFacets(List<String> facets) {
        this.facets = facets;
        return this;
    }


    /**
     * Set the facets limit:
     * Request the search to return the specified schema parameters
     *
     * @param facetsLimit facet limit
     * @return this instance.
     */
    public BaseSearchParams setFacetsLimit(Integer facetsLimit) {
        this.facetsLimit = facetsLimit;
        return this;
    }


    /**
     * Set the sortBy:
     * Request the search to return the specified schema parameters
     *
     * @param sortBy
     * @return this instance.
     */
    public BaseSearchParams setSortBy(String sortBy) {
        this.sortBy = sortBy;
        return this;
    }

    public BaseSearchParams setStrategyId(String strategyId) {
        this.strategyId = strategyId;
        return this;
    }

    /**
     * Get the sortBy value
     *
     * @return sortBy
     */
    public String getSortBy() {
        return sortBy;
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
     * Get all fl requirement
     *
     * @return all fl requirement, true - all metadata should be returned (except text type field)
     */
    public Boolean isGetAllFl() {
        return getAllFl;
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
     * get minimum score threshold
     *
     * @return minimum score threshold value
     */
    public Float getScoreMin() {
        return scoreMin;
    }

    /**
     * set minimum score threshold
     * @param scoreMin min score threshold value
     */
    public void setScoreMin(Float scoreMin) {
        this.scoreMin = scoreMin;
    }

    /**
     * get maximum score threshold
     *
     *
     * @return maximum score threshold value
     */
    public Float getScoreMax() {
        return scoreMax;
    }

    /**
     * set maximum score threshold value
     *
     * @param scoreMax max score threshold value
     */
    public void setScoreMax(Float scoreMax) {
        this.scoreMax = scoreMax;
    }

    /**
     * Set the charset in which the parameters will be encoded in
     *
     * @param charset the charset
     */
    public void setCharset(Charset charset) {
        this.charset = charset;
    }

    /**
     * Get the charset in which the parameters will be encoded in
     *
     * @return the charset
     */
    public Charset getCharset() {
        return charset;
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

    /**
     * Get the facets list
     *
     * @return facets list.
     */
    public List<String> getFacets() {
        return facets;
    }

    /**
     * Get the facets limit
     *
     * @return facets limit
     */
    public Integer getFacetsLimit() {
        return facetsLimit;
    }

    public Map<String, List<String> > toMap() {
        Map<String, List<String> > map = new HashMap<String, List<String> >();

        if (limit != null && limit > 0) {
            putStringInMap(map, "limit", limit.toString());
        }

        if (page != null && page > 0) {
            putStringInMap(map, "page", page.toString());
        }

        if (score != null) {
            putStringInMap(map, "score", String.valueOf(score));
        }

        if (scoreMin != null) {
            putStringInMap(map, "score_min", String.valueOf(scoreMin));
        }

        if (scoreMax != null) {
            putStringInMap(map, "score_max", String.valueOf(scoreMax));
        }

        if (queryInfo != null) {
            putStringInMap(map, "qinfo", String.valueOf(queryInfo));
        }

        if (getAllFl != null) {
            putStringInMap(map, "get_all_fl", String.valueOf(getAllFl));
        }

        if (sortBy != null) {
            putStringInMap(map, "sort_by", sortBy);
        }

        if (strategyId != null) {
            putStringInMap(map, "strategy_id", strategyId);
        }

        if (custom != null && custom.size() > 0) {
            for (String key : custom.keySet()) {
                putStringInMap(map, key, custom.get(key));
            }
        }

        if (fq != null && fq.size() > 0) {
            List<String> valueList = new ArrayList<>();

            for (Map.Entry<String, String> entry : fq.entrySet()) {
                valueList.add(entry.getKey() + ":" + entry.getValue());
            }

            map.put("fq", valueList);
        }

        if (fl != null && fl.size() > 0) {
            map.put("fl", fl);
        }

        if (facets != null && facets.size() > 0) {
            map.put("facets", facets);
        }

        if (facetsLimit != null && facetsLimit > 0) {
            putStringInMap(map, "facets_limit", facetsLimit.toString());
        }

        return map;
    }

    private void putStringInMap(Map<String, List<String> > map, String key, String value) {
        List<String> stringList = new ArrayList<>();
        stringList.add(value);

        map.put(key, stringList);
    }

    public Map<String, String> getCustom() {
        return custom;
    }

    public void setCustom(Map<String, String> custom) {
        this.custom = custom;
    }
}
