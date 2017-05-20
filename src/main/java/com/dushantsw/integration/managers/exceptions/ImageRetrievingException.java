package com.dushantsw.integration.managers.exceptions;

import com.mashape.unirest.http.exceptions.UnirestException;

public class ImageRetrievingException extends UnirestException {
    public ImageRetrievingException(String message) {
        super(message);
    }
    public ImageRetrievingException(Exception cause) { super(cause); }
    public ImageRetrievingException(String message, Throwable e) {
        super(message);
        this.initCause(e);
    }
}
