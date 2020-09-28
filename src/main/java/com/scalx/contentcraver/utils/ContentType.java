package com.scalx.contentcraver.utils;

public enum ContentType {

    REDDIT("REDDIT"),
    HACKERNEWS("HACKERNEWS");

    private String contentType;

    ContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getContentType() {
        return contentType;
    }
}