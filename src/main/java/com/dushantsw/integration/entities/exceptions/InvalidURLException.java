package com.dushantsw.integration.entities.exceptions;

/**
 * <code>InvalidURLException</code>
 *
 * @author dushantsw
 */
public class InvalidURLException extends Throwable {
    public InvalidURLException() {
        super("invalid_url");
    }
}
