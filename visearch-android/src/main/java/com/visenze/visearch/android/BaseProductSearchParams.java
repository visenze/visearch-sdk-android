package com.visenze.visearch.android;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.visenze.visearch.android.network.RetrofitQueryMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class BaseProductSearchParams {

    @SerializedName("page")
    private Integer page;

    @SerializedName("limit")
    private Integer limit;

    @SerializedName("va_uid")
    private String vaUid;

    @SerializedName("va_sid")
    private String vaSid;

    @SerializedName("facet_limit")
    private Integer facetLimit;

    @SerializedName("facets_show_count")
    private Boolean facetsShowCount;

    @SerializedName("sort_by")
    private String sortBy;

    @SerializedName("group_by")
    private String groupBy;

    @SerializedName("group_limit")
    private Integer groupLimit;

    @SerializedName("sort_group_strategy")
    private String sortGroupStrategy;

    @SerializedName("score")
    private Boolean score;

    @SerializedName("score_min")
    private Float score_min;

    @SerializedName("score_max")
    private Float scoreMax;

    @SerializedName("return_fields_mapping")
    private Boolean returnFieldsMapping;

    @SerializedName("return_image_s3_url")
    private Boolean returnImageS3Url;

    @SerializedName("va_sdk")
    private String vaSdk;

    @SerializedName("va_sdk_version")
    private String vaSdkVersion;

    @SerializedName("va_os")
    private String vaOs;

    @SerializedName("va_osv")
    private String vaOsv;

    @SerializedName("va_device_brand")
    private String vaDeviceBrand;

    @SerializedName("va_device_model")
    private String vaDeviceModel;

    @SerializedName("va_app_bundle_id")
    private String vaAppBundleId;

    @SerializedName("va_app_name")
    private String vaAppName;

    @SerializedName("va_app_version")
    private String vaAppVersion;

    @SerializedName("dedup")
    private Boolean dedup;

    @SerializedName("dedup_score_threshold")
    private Double dedupScoreThreshold;

    @SerializedName("color_rel_weight")
    private Double colorRelWeight;

    @SerializedName("va_aaid")
    private String vaAaid;

    @SerializedName("va_didmd5")
    private String vaDidmd5;

    @SerializedName("va_n1")
    private Double vaN1;

    @SerializedName("va_n2")
    private Double vaN2;

    @SerializedName("va_s1")
    private String vaS1;

    @SerializedName("va_s2")
    private String vaS2;

    public Double getColorRelWeight() {
        return colorRelWeight;
    }

    public void setColorRelWeight(Double colorRelWeight) {
        this.colorRelWeight = colorRelWeight;
    }

    public Boolean getDedup() {
        return dedup;
    }

    public Double getDedupScoreThreshold() {
        return dedupScoreThreshold;
    }

    public void setDedup(Boolean dedup) {
        this.dedup = dedup;
    }

    public void setDedupScoreThreshold(Double dedupScoreThreshold) {
        this.dedupScoreThreshold = dedupScoreThreshold;
    }

    public void setVaN1(Double vaN1) {
        this.vaN1 = vaN1;
    }

    public void setVaN2(Double vaN2) {
        this.vaN2 = vaN2;
    }

    private static final Gson gson = new Gson();

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public String getVaUid() {
        return vaUid;
    }

    public void setVaUid(String vaUid) {
        this.vaUid = vaUid;
    }

    public String getVaSid() {
        return vaSid;
    }

    public void setVaSid(String vaSid) {
        this.vaSid = vaSid;
    }

    public int getFacetLimit() {
        return facetLimit;
    }

    public void setFacetLimit(Integer facetLimit) {
        this.facetLimit = facetLimit;
    }

    public Boolean getFacetsShowCount() {
        return facetsShowCount;
    }

    public void setFacetsShowCount(Boolean facetsShowCount) {
        this.facetsShowCount = facetsShowCount;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public String getGroupBy() {
        return groupBy;
    }

    public void setGroupBy(String groupBy) {
        this.groupBy = groupBy;
    }

    public Integer getGroupLimit() {
        return groupLimit;
    }

    public void setGroupLimit(Integer groupLimit) {
        this.groupLimit = groupLimit;
    }

    public String getSortGroupStrategy() {
        return sortGroupStrategy;
    }

    public void setSortGroupStrategy(String sortGroupStrategy) {
        this.sortGroupStrategy = sortGroupStrategy;
    }

    public Boolean getScore() {
        return score;
    }

    public void setScore(Boolean score) {
        this.score = score;
    }

    public Float getScore_min() {
        return score_min;
    }

    public void setScore_min(Float score_min) {
        this.score_min = score_min;
    }

    public Float getScoreMax() {
        return scoreMax;
    }

    public void setScoreMax(Float scoreMax) {
        this.scoreMax = scoreMax;
    }

    public Boolean getReturnFieldsMapping() {
        return returnFieldsMapping;
    }

    public void setReturnFieldsMapping(Boolean returnFieldsMapping) {
        this.returnFieldsMapping = returnFieldsMapping;
    }

    public Boolean getReturnImageS3Url() {
        return returnImageS3Url;
    }

    public void setReturnImageS3Url(Boolean returnImageS3Url) {
        this.returnImageS3Url = returnImageS3Url;
    }


    public String getVaSdk() {
        return vaSdk;
    }

    public void setVaSdk(String vaSdk) {
        this.vaSdk = vaSdk;
    }

    public String getVaSdkVersion() {
        return vaSdkVersion;
    }

    public void setVaSdkVersion(String vaSdkVersion) {
        this.vaSdkVersion = vaSdkVersion;
    }

    public String getVaOs() {
        return vaOs;
    }

    public void setVaOs(String vaOs) {
        this.vaOs = vaOs;
    }

    public String getVaOsv() {
        return vaOsv;
    }

    public void setVaOsv(String vaOsv) {
        this.vaOsv = vaOsv;
    }

    public String getVaDeviceBrand() {
        return vaDeviceBrand;
    }

    public void setVaDeviceBrand(String vaDeviceBrand) {
        this.vaDeviceBrand = vaDeviceBrand;
    }

    public String getVaDeviceModel() {
        return vaDeviceModel;
    }

    public void setVaDeviceModel(String vaDeviceModel) {
        this.vaDeviceModel = vaDeviceModel;
    }

    public String getVaAppBundleId() {
        return vaAppBundleId;
    }

    public void setVaAppBundleId(String vaAppBundleId) {
        this.vaAppBundleId = vaAppBundleId;
    }

    public String getVaAppName() {
        return vaAppName;
    }

    public void setVaAppName(String vaAppName) {
        this.vaAppName = vaAppName;
    }

    public String getVaAppVersion() {
        return vaAppVersion;
    }

    public void setVaAppVersion(String vaAppVersion) {
        this.vaAppVersion = vaAppVersion;
    }

    public String getVaAaid() {
        return vaAaid;
    }

    public void setVaAaid(String vaAaid) {
        this.vaAaid = vaAaid;
    }

    public String getVaDidmd5() {
        return vaDidmd5;
    }

    public void setVaDidmd5(String vaDidmd5) {
        this.vaDidmd5 = vaDidmd5;
    }

    public Double getVaN1() {
        return vaN1;
    }

    public void setVaN1(double vaN1) {
        this.vaN1 = vaN1;
    }

    public Double getVaN2() {
        return vaN2;
    }

    public void setVaN2(double vaN2) {
        this.vaN2 = vaN2;
    }

    public String getVaS1() {
        return vaS1;
    }

    public void setVaS1(String vaS1) {
        this.vaS1 = vaS1;
    }

    public String getVaS2() {
        return vaS2;
    }

    public void setVaS2(String vaS2) {
        this.vaS2 = vaS2;
    }

    @SerializedName("facets")
    private List<String> facets;

    @SerializedName("attrs_to_get")
    @Expose(deserialize = false, serialize = false)
    private List<String> attrsToGet;


    @Expose(deserialize = false, serialize = false)
    private Map<String, String> filters;

    @Expose(deserialize = false, serialize = false)
    private Map<String, String> textFilters;



    @SerializedName("custom_map")
    @Expose(deserialize = false, serialize = false)
    private Map<String, String> customParams;



    public Map<String, String> getFilters() {
        return filters;
    }

    public void setFilters(Map<String, String> filters) {
        this.filters = filters;
    }

    public Map<String, String> getTextFilters() {
        return textFilters;
    }

    public void setTextFilters(Map<String, String> textFilters) {
        this.textFilters = textFilters;
    }

    public List<String> getAttrsToGet() {
        return attrsToGet;
    }

    public void setAttrsToGet(List<String> attrsToGet) {
        this.attrsToGet = attrsToGet;
    }

    public List<String> getFacets() {
        return facets;
    }

    public void setFacets(List<String> facets) {
        this.facets = facets;
    }

    public Map<String, String> getCustomParams() {
        return customParams;
    }

    public void setCustomParams(Map<String, String> customMap) {
        this.customParams = customMap;
    }

    public RetrofitQueryMap getQueryMap() {

        Map<String, Object> map = new HashMap<String, Object>();
        RetrofitQueryMap ret = new RetrofitQueryMap(map);


        String jsonStr = gson.toJson(this);

        JsonObject jsonObj = gson.fromJson(jsonStr, JsonObject.class);
        Set<Map.Entry<String, JsonElement>> entrySet = jsonObj.entrySet();
        for(Map.Entry<String, JsonElement> entry : entrySet) {
            String key = entry.getKey();
            JsonElement val = entry.getValue();
            if(val.isJsonArray()) {
                List<String> list = new ArrayList<String>();
                JsonArray array = val.getAsJsonArray();
                for(int i=0; i<array.size(); i++) {
                    if (array.get(i).isJsonPrimitive()) {
                        list.add(array.get(i).getAsString());
                    } else {
                        list.add(array.get(i).toString());
                    }
                }
                ret.put(key, list);
            } else if (val.isJsonPrimitive()) {
                ret.put(key, val.getAsString());
            }
        }

        if(customParams != null) {
            Set<Map.Entry<String, String>> set = customParams.entrySet();
            for(Map.Entry<String, String> entry : set) {
                String key = entry.getKey();
                String val = entry.getValue();
                ret.put(key, val);
            }
        }


        if(filters != null) {
            List<String> list = formatToList(filters);
            ret.put("filters", list);
        }

        if(textFilters != null) {
            List<String> list = formatToList(textFilters);
            ret.put("text_filters", list);
        }

        return ret;
    }

    private List<String> formatToList(Map<String, String> params) {
        Set<Map.Entry<String, String>> set = params.entrySet();
        List<String> ret = new ArrayList<String>();
        for(Map.Entry<String, String>entry : set) {
            String key = entry.getKey();
            String val = entry.getValue();
            ret.add(key+":"+val);
        }
        return ret;
    }


}
