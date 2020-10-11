package com.visenze.visearch.android;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Abstract class. Search parameter settings.
 * This class is extended by
 * {@link IdSearchParams IndexSearchParams}
 * {@link ColorSearchParams ColorSearchParams}
 * {@link UploadSearchParams UploadSearchParams}.
 */
public abstract class SearchParams {
    protected BaseSearchParams mBaseSearchParams;

    // analytics parameters
    protected String uid;
    protected String sid;
    protected String source;
    protected String platform;
    protected String os;
    protected String osv;
    protected String deviceBrand;
    protected String deviceModel;
    protected String language;
    protected String appId;
    protected String appName;
    protected String appVersion;

    protected SearchParams() {
        mBaseSearchParams = new BaseSearchParams();
    }

    protected SearchParams(BaseSearchParams baseSearchParams) {
        this.mBaseSearchParams = baseSearchParams;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getOsv() {
        return osv;
    }

    public void setOsv(String osv) {
        this.osv = osv;
    }

    public String getDeviceBrand() {
        return deviceBrand;
    }

    public void setDeviceBrand(String deviceBrand) {
        this.deviceBrand = deviceBrand;
    }

    public String getDeviceModel() {
        return deviceModel;
    }

    public void setDeviceModel(String deviceModel) {
        this.deviceModel = deviceModel;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    /**
     * Set the basic search parameters. If not set, default settings are used
     *
     * @param baseSearchParams basic search parameter settings
     */
    public void setBaseSearchParams(BaseSearchParams baseSearchParams) {
        this.mBaseSearchParams = baseSearchParams;
    }

    /**
     * Get the basic search parameter settings
     *
     * @return basic search parameter settings.
     */
    public BaseSearchParams getBaseSearchParams() {
        return mBaseSearchParams;
    }

    protected Map<String, List<String>> toMap() {
        Map<String, List<String>> map = mBaseSearchParams.toMap();

        if (uid != null) {
            putStringInMap(map, "va_uid" , uid);
        }

        if (sid != null) {
            putStringInMap(map, "va_sid" , uid);
        }

        if (source != null) {
            putStringInMap(map, "va_source" , source);
        }

        if (platform != null) {
            putStringInMap(map, "va_platform" , platform);
        }

        if (os != null) {
            putStringInMap(map, "va_os" , os);
        }

        if (osv != null) {
            putStringInMap(map, "va_osv" , osv);
        }

        if (appId != null) {
            putStringInMap(map, "va_app_bundle_id" , appId);
        }

        if (appName != null) {
            putStringInMap(map, "va_app_name" , appName);
        }

        if (appVersion != null) {
            putStringInMap(map, "va_app_version" , appVersion);
        }

        if (deviceBrand != null) {
            putStringInMap(map, "va_device_brand" , deviceBrand);
        }

        if (deviceModel != null) {
            putStringInMap(map, "va_device_model" , deviceModel);
        }

        if (language != null) {
            putStringInMap(map, "va_language" , language);
        }

        return map;
    }

    public void putStringInMap(Map<String, List<String> > map, String key, String value) {
        List<String> stringList = new ArrayList<>();
        stringList.add(value);

        map.put(key, stringList);
    }
}
