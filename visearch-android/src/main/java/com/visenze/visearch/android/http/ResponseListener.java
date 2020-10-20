package com.visenze.visearch.android.http;

import com.android.volley.Response;
import com.visenze.visearch.android.ResultList;
import com.visenze.visearch.android.ViSearch;
import com.visenze.visearch.android.ViSearchException;
import com.visenze.visearch.android.util.ResponseParser;

import org.json.JSONObject;

/**
 * Response listener
 */
public class ResponseListener implements Response.Listener<JSONObject> {
    private ViSearch.ResultListener resultListener;

    public ResponseListener(ViSearch.ResultListener resultListener) {
        this.resultListener = resultListener;
    }

    @Override
    public void onResponse(JSONObject jsonObject) {
        if (null != resultListener) {
            try {
                ResultList resultList = getResult(jsonObject);
                if (resultList.getErrorMessage() != null)
                    resultListener.onSearchError(resultList.getErrorMessage());
                else {
                    resultListener.onSearchResult(getResult(jsonObject));
                }
            } catch (ViSearchException e) {
                e.printStackTrace();
            }
        }
    }

    private ResultList getResult(JSONObject jsonObject) {
        return ResponseParser.parseResult(jsonObject);
    }
}
