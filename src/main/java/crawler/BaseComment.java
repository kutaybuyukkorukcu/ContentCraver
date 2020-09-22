package crawler;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BaseComment {

    @JsonProperty("comment_id")
    Integer commentId;

    @JsonProperty("text")
    String text;

    @JsonProperty("user")
    String user;

    @JsonProperty("parent_comment_id")
    Integer parentCommentId;

    public Integer getCommentId() {
        return commentId;
    }

    public void setCommentId(Integer commentId) {
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

    public Integer getParentCommentId() {
        return parentCommentId;
    }

    public void setParentCommentId(Integer parentCommentId) {
        this.parentCommentId = parentCommentId;
    }
}
