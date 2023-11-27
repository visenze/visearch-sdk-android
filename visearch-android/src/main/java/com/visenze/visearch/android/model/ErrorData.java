package com.visenze.visearch.android.model;

import com.google.gson.annotations.SerializedName;

public class ErrorData {

    @SerializedName("code")
    private int code;

    @SerializedName("message")
    private String message;


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static ErrorData unknownError(String msg) {
        ErrorData error = new ErrorData();
        error.setMessage(msg);
        error.setCode(-1);
        return error;
    }
}
