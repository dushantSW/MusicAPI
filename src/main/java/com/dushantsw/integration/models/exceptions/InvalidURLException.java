package com.dushantsw.integration.models.exceptions;

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
