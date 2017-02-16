package com.visenze.visearch.android.http;

import android.util.Base64;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;

import org.apache.http.HttpEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MultiPartRequest extends Request<JSONObject> {

    private static final String FILE_PART_NAME = "image";

    private final Response.Listener<JSONObject> mListener;
    private String                              accessKey;
    private String                              secretKey;
    private HttpEntity                          entity;
    private String                              userAgent;


    public MultiPartRequest(int method, String url,
                            Map<String, List<String>> params, byte[] bytes,
                            String accessKey, String secretKey, String userAgent,
                            Response.Listener<JSONObject> mListener,
                            Response.ErrorListener listener) {

        super(method, url, listener);
        this.mListener = mListener;
        this.accessKey = accessKey;
        this.secretKey = secretKey;
        this.userAgent = userAgent;

        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        for (Map.Entry<String, List<String> > entry : params.entrySet()) {
            for (String s : entry.getValue())
                builder.addTextBody(entry.getKey(), s);
        }

        // add auth access key
        if (secretKey == null)
            builder.addTextBody("access_key", accessKey);

        ByteArrayBody byteArrayBody = new ByteArrayBody(bytes, FILE_PART_NAME);
        builder.addPart(FILE_PART_NAME, byteArrayBody);
        entity = builder.build();

        //retry policy for upload multipart, set retry number as 1
        setRetryPolicy(new DefaultRetryPolicy(HttpInstance.TIME_OUT_FOR_UPLOAD, 1, 1));
    }

    @Override
    public String getBodyContentType() {
        return entity.getContentType().getValue();
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            entity.writeTo(bos);
        } catch (IOException e) {
            VolleyLog.e("IOException writing to ByteArrayOutputStream");
        }

        return bos.toByteArray();
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> params = new HashMap<>();
        //add auth header if secret key presents
        if (secretKey != null) {
            String creds = String.format("%s:%s", accessKey, secretKey);
            String auth = "Basic " + Base64.encodeToString(creds.getBytes(), Base64.NO_WRAP);
            params.put("Authorization", auth);
        }

        //add request header
        params.put("X-Requested-With", userAgent);

        return params;
    }

    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString =
                    new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            JSONObject result = new JSONObject(jsonString);

            Map headers = response.headers;
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

    @Override
    protected void deliverResponse(JSONObject jsonObject) {
        mListener.onResponse(jsonObject);
    }
}
