package com.visenze.visearch.android.http;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.visenze.visearch.android.util.AuthGenerator;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * Created by visenze on 29/2/16.
 */
public class JsonWithHeaderRequest extends JsonObjectRequest {
    private String                              accessKey;
    private String                              secretKey;
    private String                              userAgent;

    public JsonWithHeaderRequest(int method, String url, JSONObject jsonRequest,
                                 String accessKey, String secretKey, String userAgent,
                                 Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {

        super(method, url, jsonRequest, listener, errorListener);
        this.accessKey = accessKey;
        this.secretKey = secretKey;
        this.userAgent = userAgent;
        setRetryPolicy(new DefaultRetryPolicy(HttpInstance.TIME_OUT_FOR_ID, 0, 1));
    }

    public JsonWithHeaderRequest(String url, JSONObject jsonRequest,
                                 String accessKey, String secretKey, String userAgent,
                                 Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        super(url, jsonRequest, listener, errorListener);
        this.accessKey = accessKey;
        this.secretKey = secretKey;
        this.userAgent = userAgent;
    }

    //set auth information in header
    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return AuthGenerator.generateHeaderParams(accessKey, secretKey, userAgent);
    }

    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString =
                    new String(response.data, HttpHeaderParser.parseCharset(response.headers));

            Map headers = response.headers;
            JSONObject result = new JSONObject(jsonString);

            if (headers.containsKey("X-Log-ID")) {
                String transId = (String)headers.get("X-Log-ID");
                result.put("transId", transId);
            }

            return Response.success(result, HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JSONException je) {
            return Response.error(new ParseError(je));
        }
    }
}
