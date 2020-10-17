package com.scalx.contentcraver;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;

public class BaseComment {

    @JsonProperty("id")
    private String id;

    @JsonProperty("story_id")
    private String storyId;

    @JsonProperty("text")
    private String text;

    @JsonProperty("user")
    private String user;

    @JsonProperty("parent_comment_id")
    private String parentCommentId;

    @JsonProperty("created")
    private int created;

    public BaseComment(String storyId, String commentId, String text, String user, String parentCommentId, int created) {
        this.storyId = storyId;
        this.id = id;
        this.text = text;
        this.user = user;
        this.parentCommentId = parentCommentId;
        this.created = created;
    }

    @Override
    public String toString() {
        return "BaseComment{" +
                "id=" + id +
                ", text='" + text + '\'' +
                ", user='" + user + '\'' +
                ", parentCommentId=" + parentCommentId + '\'' +
                ", created=" + Instant.ofEpochSecond(created) + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String commentId) {
        this.id = commentId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getParentCommentId() {
        return parentCommentId;
    }

    public void setParentCommentId(String parentCommentId) {
        this.parentCommentId = parentCommentId;
    }
}