package com.visenze.visearch.android.http;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.visenze.visearch.android.util.ViSearchUIDManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * Created by visenze on 29/2/16.
 */
public class JsonWithHeaderRequest extends JsonObjectRequest {

    public JsonWithHeaderRequest(int method, String url, JSONObject jsonRequest,
                                 Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {

        super(method, url, jsonRequest, listener, errorListener);
        setRetryPolicy(new DefaultRetryPolicy(HttpInstance.TIME_OUT_FOR_ID, 0, 1));
    }

    public JsonWithHeaderRequest(String url, JSONObject jsonRequest,
                                 Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        super(url, jsonRequest, listener, errorListener);
    }

    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString =
                    new String(response.data, HttpHeaderParser.parseCharset(response.headers));

            Map headers = response.headers;
            if (headers.containsKey("Set-Cookie")) {
                String value = (String)headers.get("Set-Cookie");
                String[] cv = value.split(";");
                String[] uid = new String[0];
                for (String v : cv) {
                    if (v.startsWith("uid")) {
                        uid = v.split("=");
                        break;
                    }
                }
                if (uid.length > 0) {
                    ViSearchUIDManager.updateUidFromCookie(uid[1]);
                }
            }

            JSONObject result = new JSONObject(jsonString);

            if (headers.containsKey("X-Log-ID")) {
                String transId = (String)headers.get("X-Log-ID");
                result.put("transId", transId);
            }

            return Response.success(result,
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JSONException je) {
            return Response.error(new ParseError(je));
        }
    }
}
