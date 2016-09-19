package com.visenze.visearch.android.api;

import com.visenze.visearch.android.ColorSearchParams;
import com.visenze.visearch.android.IdSearchParams;
import com.visenze.visearch.android.UploadSearchParams;
import com.visenze.visearch.android.ViSearch;


/**
 * Search operations interface.
 */
public interface SearchOperations {
    /**
     * ID Search (GET /search)
     * Searching against the image collection using an existing image in the collection
     *
     * @param idSearchParams the index search parameter setting
     * @param resultListener result listener
     */
    public void search(IdSearchParams idSearchParams, final ViSearch.ResultListener resultListener);

    /**
     * Recommendation (GET /recommendation)
     * Get list of recommended images against the image collection using an existing image in the collection
     *
     * @param idSearchParams the index search parameter setting
     * @param resultListener result listener
     */
    public void recommendation(IdSearchParams idSearchParams, final ViSearch.ResultListener resultListener);

    /**
     * Color Search (GET /colorsearch)
     * Searching for image matching a color
     *
     * @param colorSearchParams the color search parameter setting
     * @param resultListener result listener
     */
    public void colorSearch(ColorSearchParams colorSearchParams, final ViSearch.ResultListener resultListener);

    /**
     * Upload Search (POST /uploadsearch)
     * Searching against the image collection with an uploading image.
     *
     * @param uploadSearchParams the index search parameter setting
     * @param resultListener result listener
     */
    public void uploadSearch(UploadSearchParams uploadSearchParams, final ViSearch.ResultListener resultListener);
}
