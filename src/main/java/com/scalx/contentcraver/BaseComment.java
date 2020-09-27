package com.scalx.contentcraver;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

public class BaseComment {

    @JsonProperty("article_id")
    private String articleId;

    @JsonProperty("comment_id")
    private String commentId;

    @JsonProperty("text")
    private String text;

    @JsonProperty("user")
    private String user;

    @JsonProperty("parent_comment_id")
    private String parentCommentId;

    @JsonProperty("created")
    private int created;

    public BaseComment(String articleId, String commentId, String text, String user, String parentCommentId, int created) {
        this.articleId = articleId;
        this.commentId = commentId;
        this.text = text;
        this.user = user;
        this.parentCommentId = parentCommentId;
        this.created = created;
    }

    @Override
    public String toString() {
        return "BaseComment{" +
                "commentId=" + commentId +
                ", text='" + text + '\'' +
                ", user='" + user + '\'' +
                ", parentCommentId=" + parentCommentId + '\'' +
                ", created=" + Instant.ofEpochSecond(created) + '\'' +
                '}';
    }

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
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