package com.visenze.visearch.android.http;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.visenze.visearch.android.util.AuthGenerator;
import com.visenze.visearch.android.util.StringUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.visenze.visearch.android.util.MultipartUtils.*;


public class MultiPartRequest extends Request<JSONObject> {

    private static final String PROTOCOL_CHARSET = "utf-8";

    private final Response.Listener<JSONObject> mListener;
    private String                              accessKey;
    private String                              secretKey;
    private String                              userAgent;

    private int curTime;
    private String boundaryPrefixed;
    private Map<String, MultiPartParam> mMultipartParams = null;
    public static final int TIMEOUT_MS = 30000;
    private boolean isFixedStreamingMode;

    byte[] imageBytes;

    public MultiPartRequest(int method, String url,
                            Map<String, List<String>> params, byte[] bytes,
                            String accessKey, String secretKey, String userAgent,
                            Response.Listener<JSONObject> mListener,
                            Response.ErrorListener listener) {
        this(method, url, params, null, bytes, accessKey, secretKey, userAgent, mListener, listener);
    }

    public MultiPartRequest(int method, String url,
                            Map<String, List<String>> params, Charset charset,
                            byte[] bytes, String accessKey, String secretKey, String userAgent,
                            Response.Listener<JSONObject> mListener,
                            Response.ErrorListener listener) {

        super(method, url, listener);
        this.mListener = mListener;
        this.accessKey = accessKey;
        this.secretKey = secretKey;
        this.userAgent = userAgent;


        mMultipartParams = new HashMap<String, MultiPartParam>();

        curTime = (int) (System.currentTimeMillis() / 1000);
        boundaryPrefixed = BOUNDARY_PREFIX + curTime;

        // add auth access key
        if (secretKey == null)
            addStringParam("access_key", accessKey);

        for (Map.Entry<String, List<String> > entry : params.entrySet()) {
            for (String s : entry.getValue()) {
                addStringParam(entry.getKey(), s);
            }
        }

        this.imageBytes = bytes;

        //retry policy for upload multipart, set retry number as 1
        setRetryPolicy(new DefaultRetryPolicy(HttpInstance.TIME_OUT_FOR_UPLOAD, 1, 1));
    }

    @Override
    public String getBodyContentType() {
        return String.format(CONTENT_TYPE_MULTIPART, getProtocolCharset(), getBoundry());
    }

    public String getProtocolCharset() {
        return PROTOCOL_CHARSET;
    }

    /**
     * Get the Content Length
     */
    public int getContentLength() {
        return getContentLengthForMultipartRequest(getBoundryPrefixed(), getMultipartParams(), imageBytes);
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);

        try {
            buildParts(dos);
            return bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            VolleyLog.e("IOException writing to ByteArrayOutputStream");
        }

        return super.getBody();
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return AuthGenerator.generateHeaderParams(accessKey, secretKey, userAgent);
    }

    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        try {
            String utf8String = StringUtils.getUtf8String(response.data);
            JSONObject result = new JSONObject(utf8String);

            Map headers = response.headers;
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

    private void buildParts(DataOutputStream dos) throws IOException {
        Map<String, MultiPartParam> multipartParams = getMultipartParams();

        for (Map.Entry<String, MultiPartParam> multipartParam : multipartParams.entrySet()) {
            MultiPartParam param = multipartParam.getValue();
            buildStringPart(dos, multipartParam.getKey(), param);
        }

        buildDataPart(dos);

        // close multipart form data after text and file data
        dos.writeBytes(getBoundryPrefixed() + BOUNDARY_PREFIX);
        dos.writeBytes(CRLF);
    }

    private void buildStringPart(DataOutputStream dataOutputStream, String key, MultiPartParam param) throws IOException {
        dataOutputStream.writeBytes(getBoundryPrefixed());
        dataOutputStream.writeBytes(CRLF);
        dataOutputStream.writeBytes(String.format(HEADER_CONTENT_DISPOSITION + COLON_SPACE + FORM_DATA, key));
        dataOutputStream.writeBytes(CRLF);
        dataOutputStream.writeBytes(HEADER_CONTENT_TYPE + COLON_SPACE + param.contentType);
        dataOutputStream.writeBytes(CRLF);
        dataOutputStream.writeBytes(CRLF);
        dataOutputStream.writeBytes(param.value);
        dataOutputStream.writeBytes(CRLF);
    }

    // attach image if there is
    private void buildDataPart(DataOutputStream dataOutputStream) throws IOException {
        if (imageBytes == null) {
            return;
        }

        dataOutputStream.writeBytes(getBoundryPrefixed());
        dataOutputStream.writeBytes(CRLF);
        dataOutputStream.writeBytes(String.format(HEADER_CONTENT_DISPOSITION + COLON_SPACE + FORM_DATA + SEMICOLON_SPACE + FILENAME, IMAGE_PARAM, IMAGE_FILE_NAME));
        dataOutputStream.writeBytes(CRLF);
        dataOutputStream.writeBytes(HEADER_CONTENT_TYPE + COLON_SPACE + CONTENT_TYPE_OCTET_STREAM);
        dataOutputStream.writeBytes(CRLF);
        dataOutputStream.writeBytes(HEADER_CONTENT_TRANSFER_ENCODING + COLON_SPACE + BINARY);
        dataOutputStream.writeBytes(CRLF);
        dataOutputStream.writeBytes(CRLF);

        ByteArrayInputStream fileInputStream = new ByteArrayInputStream(imageBytes);
        int bytesAvailable = fileInputStream.available();

        int maxBufferSize = 1024 * 1024;
        int bufferSize = Math.min(bytesAvailable, maxBufferSize);
        byte[] buffer = new byte[bufferSize];

        int bytesRead = fileInputStream.read(buffer, 0, bufferSize);

        while (bytesRead > 0) {
            dataOutputStream.write(buffer, 0, bufferSize);
            bytesAvailable = fileInputStream.available();
            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            bytesRead = fileInputStream.read(buffer, 0, bufferSize);
        }

        dataOutputStream.writeBytes(CRLF);
    }

    public boolean isFixedStreamingMode() {
        return isFixedStreamingMode;
    }

    public void setFixedStreamingMode(boolean isFixedStreamingMode) {
        this.isFixedStreamingMode = isFixedStreamingMode;
    }

    /**
     * Get the boundry prefixed
     */
    public String getBoundryPrefixed() {
        return boundaryPrefixed;
    }

    /**
     * Get the boundry
     */
    public int getBoundry() {
        return curTime;
    }

    /**
     * Add a parameter to be sent in the multipart request
     *
     * @param name The name of the paramter
     * @param contentType The content type of the paramter
     * @param value the value of the paramter
     * @return The Multipart request for chaining calls
     */
    public MultiPartRequest addMultipartParam(String name, String contentType, String value) {
        mMultipartParams.put(name, new MultiPartParam(contentType, value));
        return this;
    }

    /**
     * Add a string parameter to be sent in the multipart request
     *
     * @param name The name of the paramter
     * @param value the value of the paramter
     * @return The Multipart request for chaining calls
     */
    public MultiPartRequest addStringParam(String name, String value) {
        mMultipartParams.put(name, new MultiPartParam("text/plain", value));
        return this;
    }

    /**
     * Get all the multipart params for this request
     *
     * @return A map of all the multipart params NOT including the file uploads
     */
    public Map<String, MultiPartParam> getMultipartParams() {
        return mMultipartParams;
    }

    @Override
    protected void deliverResponse(JSONObject jsonObject) {
        mListener.onResponse(jsonObject);
    }
}
