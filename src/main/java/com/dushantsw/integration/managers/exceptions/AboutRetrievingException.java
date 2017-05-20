package com.dushantsw.integration.managers.exceptions;

import com.mashape.unirest.http.exceptions.UnirestException;

public class AboutRetrievingException extends UnirestException{
    public AboutRetrievingException(Exception e) {
        super(e);
    }

    public AboutRetrievingException(String msg) {
        super(msg);
    }

    public AboutRetrievingException(String msg, Throwable cause) {
        super(msg);
        this.initCause(cause);
    }
}
