package crawler;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public class BaseCard {

    @JsonProperty("title")
    String title;

    @JsonProperty("main_topic")
    String mainTopic;

    @JsonProperty("url")
    String url;

    @JsonProperty("author")
    String author;

    @JsonProperty("article_link")
    String articleLink;

    @JsonProperty("selftext")
    String selfText;

    @JsonProperty("ups")
    Integer upvotes;

    @JsonProperty("num_comments")
    Integer numberOfComments;

    public BaseCard(String title, String mainTopic, String url, String author, String articleLink,
                    String selfText, Integer upvotes, Integer numberOfComments) {
        this.title = title;
        this.mainTopic = mainTopic;
        this.url = url;
        this.author = author;
        this.articleLink = articleLink;
        this.selfText = selfText;
        this.upvotes = upvotes;
        this.numberOfComments = numberOfComments;
    }

    @Override
    public String toString() {
        return "BaseCard{" +
                "title='" + title + '\'' +
                ", mainTopic='" + mainTopic + '\'' +
                ", url='" + url + '\'' +
                ", author='" + author + '\'' +
                ", articleLink='" + articleLink + '\'' +
                ", selfText='" + selfText + '\'' +
                ", upvotes=" + upvotes +
                ", numberOfComments=" + numberOfComments +
                '}';
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

    public String getArticleLink() {
        return articleLink;
    }

    public void setArticleLink(String articleLink) {
        this.articleLink = articleLink;
    }

    public String getSelfText() {
        return selfText;
    }

    public void setSelfText(String selfText) {
        this.selfText = selfText;
    }

    public Integer getUpvotes() {
        return upvotes;
    }

    public void setUpvotes(Integer upvotes) {
        this.upvotes = upvotes;
    }

    public Integer getNumberOfComments() {
        return numberOfComments;
    }

    public void setNumberOfComments(Integer numberOfComments) {
        this.numberOfComments = numberOfComments;
    }
}
