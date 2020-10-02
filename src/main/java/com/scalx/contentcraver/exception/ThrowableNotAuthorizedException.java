package com.scalx.contentcraver.exception;

import javax.ws.rs.WebApplicationException;

public class ThrowableNotAuthorizedException extends WebApplicationException {

    private static final long serialVersionUID = 132L;

    public ThrowableNotAuthorizedException() {
        super();
    }

    public ThrowableNotAuthorizedException(String message) {
        super(message);
    }

    public ThrowableNotAuthorizedException(Throwable e) {
        super(e);
    }

    public ThrowableNotAuthorizedException(String message, Throwable e) {
        super(message, e);
    }
}
