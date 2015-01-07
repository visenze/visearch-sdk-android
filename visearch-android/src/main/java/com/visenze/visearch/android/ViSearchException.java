package com.visenze.visearch.android;


public class ViSearchException extends RuntimeException {

    public ViSearchException(String message) {
        super(message);
    }

    public ViSearchException(String message, Throwable e) {
        super(message, e);
    }

}
