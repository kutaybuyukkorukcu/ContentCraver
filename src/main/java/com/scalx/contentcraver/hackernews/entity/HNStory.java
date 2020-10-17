package com.scalx.contentcraver.hackernews.entity;

import com.scalx.contentcraver.BaseStory;

public class HNStory extends BaseStory {

    public HNStory(String id,
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