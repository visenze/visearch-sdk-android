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
    private String type;

    public ResponseListener(ViSearch.ResultListener resultListener,
                            String type) {
        this.resultListener = resultListener;
        this.type = type;
    }

    @Override
    public void onResponse(JSONObject jsonObject) {
        if (null != resultListener) {
            try {
                ResultList resultList = getResult(jsonObject.toString());
                if (resultList.getErrorMessage() != null)
                    resultListener.onSearchError(resultList.getErrorMessage());
                else {
                    resultListener.onSearchResult(getResult(jsonObject.toString()));
                }
            } catch (ViSearchException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * pass the json response to result list
     *
     * @param jsonResponse json response
     * @return result list
     */
    private ResultList getResult(String jsonResponse) {
        return ResponseParser.parseResult(jsonResponse);
    }
}
