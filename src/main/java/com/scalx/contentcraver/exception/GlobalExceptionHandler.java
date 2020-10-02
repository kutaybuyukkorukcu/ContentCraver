package com.scalx.contentcraver.exception;

import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.RedirectionException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

@Provider
public class GlobalExceptionHandler implements ExceptionMapper<WebApplicationException> {

    @Override
    public Response toResponse(WebApplicationException exception) {

        if (exception.getClass().toString().equals(NotAuthorizedException.class.toString())) {
            return Response
                    .status(Response.Status.fromStatusCode(401))
                    .entity(exception.getMessage())
                    .build();
        }

        if (exception.getClass().toString().equals(NotFoundException.class.toString())) {
            return Response
                    .status(Response.Status.fromStatusCode(404))
                    .entity(exception.getMessage())
                    .build();
        }

        if (exception.getClass().toString().equals(ThrowableRedirectionException.class.toString())) {
            return Response
                    .status(Response.Status.fromStatusCode(302))
                    .entity("HTTP 302 Redirect")
                    .build();
        }

        if (exception.getClass().toString().equals(UnexpectedValueException.class.toString())) {
            return Response
                    .status(Response.Status.fromStatusCode(400))
                    .entity(exception.getMessage())
                    .build();
        }

        return Response
                .status(Response.Status.fromStatusCode(422))
                .entity(exception.getMessage())
                .build();
    }

}

//class NullPointerExceptionHandler implements ExceptionMapper<NullPointerException> {
//
//    @Override
//    public Response toResponse(NullPointerException exception) {
//        return Response
//                .status(Response.Status.NOT_FOUND)
//                .entity(exception.getMessage())
//                .build();
//    }
//}