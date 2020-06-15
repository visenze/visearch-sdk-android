package com.visenze.visearch.android.util;

import java.io.UnsupportedEncodingException;

public class StringUtils {
    public static final String UTF_8 = "UTF-8";

    public static boolean isEmpty(String str) {
        return !isNotEmpty(str);
    }

    public static boolean isNotEmpty(String str) {
        return str != null && str.length() > 0 ;
    }

    public static String getUtf8String(byte[] responseData) throws UnsupportedEncodingException {
        return new String(responseData, UTF_8);
    }
}
