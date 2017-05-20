package com.dushantsw.integration.managers.exceptions;

import com.mashape.unirest.http.exceptions.UnirestException;

/**
 * <code>MusicBrainzException</code>
 *
 * @author dushantsw
 */
public class MusicBrainzException extends UnirestException{
    public MusicBrainzException(Exception e) {
        super(e);
    }

    public MusicBrainzException(String msg) {
        super(msg);
    }
}
