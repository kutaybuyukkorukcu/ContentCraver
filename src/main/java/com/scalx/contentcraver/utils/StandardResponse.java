package com.scalx.contentcraver.utils;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class StandardResponse {

    private int statusCode;
    private String message;
    private int time;
    private JsonNode data;

    public StandardResponse(String message, int time) {
        this.message = message;
        this.time = time;
    }

    public StandardResponse(int statusCode, int time) {
        this.statusCode = statusCode;
        this.time = time;
    }

    public StandardResponse(int statusCode, String message, int time) {
        this.statusCode = statusCode;
        this.message = message;
        this.time = time;
    }

    public StandardResponse(int statusCode, String message, int time, JsonNode data) {
        this.statusCode = statusCode;
        this.message = message;
        this.time = time;
        this.data = data;
    }
}