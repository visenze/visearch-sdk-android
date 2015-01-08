package com.visenze.visearch.android.util;

import com.visenze.visearch.android.ViSearchException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;


public class AuthGenerator {

    public static Map<String, String> getAuthParam(String accessKey, String secretKey) {
        Map<String, String> parameterMap = new HashMap<String, String>();
        String nonce = generateNonce();
        long date = System.currentTimeMillis() / 1000L;
        StringBuilder sigStr = new StringBuilder();
        sigStr.append(secretKey);
        sigStr.append(nonce);
        sigStr.append(date);

        parameterMap.put("access_key", accessKey);
        parameterMap.put("nonce", nonce);
        parameterMap.put("date", date + "");
        try {
            parameterMap.put("sig", hmacEncode(sigStr.toString(), secretKey));
        } catch (Exception e) {
            throw new ViSearchException("Exception in getAuthParam: " + e.toString());
        }
        return parameterMap;
    }

    private static String generateNonce() {
        SecureRandom sr = new SecureRandom();
        byte[] bytes = new byte[16];
        sr.nextBytes(bytes);
        return encodeHex(bytes);
    }

    private static String hmacEncode(String baseString, String key) throws NoSuchAlgorithmException, InvalidKeyException, IllegalStateException,
            UnsupportedEncodingException {
        Mac mac = Mac.getInstance("HmacSHA1");
        SecretKeySpec secret = new SecretKeySpec(key.getBytes(), mac.getAlgorithm());
        mac.init(secret);
        byte[] digest = mac.doFinal(baseString.getBytes());
        return encodeHex(digest);
    }

    public static String encodeHex(byte[] bytes) {
        StringBuilder builder = new StringBuilder();
        for (byte b : bytes) {
            builder.append(Integer.toHexString(Byte.valueOf(b).intValue()));
        }
        return builder.toString();
    }
}