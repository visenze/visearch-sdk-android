package com.visenze.visearch.android.api;

import com.visenze.visearch.android.TrackParams;

/**
 * Created by visenze on 26/2/16.
 */
public interface TrackOperations {
    /**
     * Event Track (GET /__aq.gif)
     * Send tracking event
     *
     * @param trackParams tracking parameters
     */
    void track(TrackParams trackParams);
}
