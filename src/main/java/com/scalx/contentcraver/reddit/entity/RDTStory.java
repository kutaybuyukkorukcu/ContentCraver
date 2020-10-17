package com.scalx.contentcraver.reddit.entity;

import com.scalx.contentcraver.BaseStory;

// Reddit
public class RDTStory extends BaseStory {

    public RDTStory(String id,
                    String title,
                    String mainTopic,
                    String url,
                    String author,
                    String text,
                    int upvoteCount,
                    int commentCount,
                    int created) {

        super(id, title, mainTopic, url, author, text, upvoteCount, commentCount, created);
    }
}
