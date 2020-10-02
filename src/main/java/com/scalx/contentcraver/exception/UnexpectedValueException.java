package com.scalx.contentcraver.exception;

import net.bytebuddy.implementation.bytecode.Throw;

import javax.ws.rs.WebApplicationException;

public class UnexpectedValueException extends WebApplicationException {

    private static final long serialVersionUID = 421L;

    public UnexpectedValueException() {
        super();
    }

    public UnexpectedValueException(String message) {
        super(message);
    }

    public UnexpectedValueException(Throwable e) {
        super(e);
    }

    public UnexpectedValueException(String message, Throwable e) {
        super(message, e);
    }

}