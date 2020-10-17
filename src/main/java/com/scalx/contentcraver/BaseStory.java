package com.scalx.contentcraver;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import java.util.List;

public class BaseStory {

    @JsonProperty("id")
    String id;

    @JsonProperty("title")
    String title;

    @JsonProperty("main_topic")
    String mainTopic;

    @JsonProperty("url")
    String url;

    @JsonProperty("author")
    String author;

    @JsonProperty("text")
    String text;

    @JsonProperty("upvote_count")
    int upvoteCount;

    @JsonProperty("comment_count")
    int commentCount;

    @JsonProperty("created")
    int created;

    public BaseStory(String id, String title, String mainTopic, String url, String author, String text,
                     int upvoteCount, int commentCount, int created) {
        this.id = id;
        this.title = title;
        this.mainTopic = mainTopic;
        this.url = url;
        this.author = author;
        this.text = text;
        this.upvoteCount = upvoteCount;
        this.commentCount = commentCount;
        this.created = created;
    }

    @Override
    public String toString() {
        return "BaseStory{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", mainTopic='" + mainTopic + '\'' +
                ", url='" + url + '\'' +
                ", author='" + author + '\'' +
                ", text='" + text + '\'' +
                ", totalScore=" + upvoteCount + '\'' +
                ", numberOfComments=" + commentCount + '\'' +
                ", created=" + created + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String articleId) {
        this.id = id;
    }

    public int getCreated() {
        return created;
    }

    public void setCreated(int created) {
        this.created = created;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMainTopic() {
        return mainTopic;
    }

    public void setMainTopic(String mainTopic) {
        this.mainTopic = mainTopic;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getUpvoteCount() {
        return upvoteCount;
    }

    public void setUpvoteCount(Integer upvoteCount) {
        this.upvoteCount = upvoteCount;
    }

    public Integer getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(Integer CommentCount) {
        this.commentCount = commentCount;
    }
}