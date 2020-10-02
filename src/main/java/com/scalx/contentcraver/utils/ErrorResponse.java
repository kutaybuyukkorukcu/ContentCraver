package com.scalx.contentcraver.utils;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@NoArgsConstructor
public class ErrorResponse {

    private int statusCode;
    private String message;
    private int time;

    public ErrorResponse(String message, int time) {
        this.message = message;
        this.time = time;
    }

    public ErrorResponse(int statusCode, int time) {
        this.statusCode = statusCode;
        this.time = time;
    }

    public ErrorResponse(int statusCode, String message, int time) {
        this.statusCode = statusCode;
        this.message = message;
        this.time = time;
    }
}