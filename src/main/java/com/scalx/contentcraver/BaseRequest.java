package com.scalx.contentcraver;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class BaseRequest {

    @JsonProperty("link")
    private String link;

    @JsonProperty("topic")
    private String topic;

    @Override
    public String toString() {
        return "BaseRequest{" +
                "link='" + link + '\'' +
                ", topic='" + topic + '\'' +
                '}';
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }
}
