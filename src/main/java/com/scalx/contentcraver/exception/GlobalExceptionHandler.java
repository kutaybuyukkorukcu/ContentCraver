package com.scalx.contentcraver.exception;

import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.RedirectionException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

@Provider
public class GlobalExceptionHandler implements ExceptionMapper<IOException> {

    @Override
    public Response toResponse(IOException exception) {
        return Response
                .status(Response.Status.BAD_REQUEST)
                .entity(exception.getMessage())
                .build();
    }
}

@Provider
class NullPointerExceptionHandler implements ExceptionMapper<NullPointerException> {

    @Override
    public Response toResponse(NullPointerException exception) {
        return Response
                .status(Response.Status.NOT_FOUND)
                .entity(exception.getMessage())
                .build();
    }
}

@Provider
class RedirectExceptionHandler implements ExceptionMapper<RedirectionException> {

    @Override
    public Response toResponse(RedirectionException exception) {
        return Response
                .status(Response.Status.fromStatusCode(302))
                .entity(exception.getMessage())
                .build();
    }
}

@Provider
class NotFoundExceptionHandler implements ExceptionMapper<NotFoundException> {

    @Override
    public Response toResponse(NotFoundException exception) {
        return Response
                .status(Response.Status.NOT_FOUND)
                .entity(exception.getMessage())
                .build();
    }
}

@Provider
class NotAuthorizedExceptionHandler implements ExceptionMapper<NotAuthorizedException> {

    @Override
    public Response toResponse(NotAuthorizedException exception) {
        return Response
                .status(Response.Status.NOT_FOUND)
                .entity(exception.getMessage())
                .build();
    }
}