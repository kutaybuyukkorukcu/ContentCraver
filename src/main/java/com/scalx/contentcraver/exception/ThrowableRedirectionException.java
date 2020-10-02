package com.scalx.contentcraver.exception;

import javax.ws.rs.RedirectionException;
import javax.ws.rs.WebApplicationException;

public class ThrowableRedirectionException extends WebApplicationException {

    private static final long serialVersionUID = 1L;

    public ThrowableRedirectionException() {
        super();
    }

    public ThrowableRedirectionException(String message) {
        super(message);
    }

    public ThrowableRedirectionException(Throwable e) {
        super(e);
    }

    public ThrowableRedirectionException(String message, Throwable e) {
        super(message, e);
    }
}
