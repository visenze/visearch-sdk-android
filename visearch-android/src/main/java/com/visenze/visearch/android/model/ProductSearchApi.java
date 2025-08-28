package com.visenze.visearch.android.model;

public enum ProductSearchApi {
    SBI,
    MS,
    MS_OUTFIT_REC,
    MS_COMPLEMENTARY;

    public boolean isMultisearchRelated() {
        return name().startsWith("MS");
    }
}
