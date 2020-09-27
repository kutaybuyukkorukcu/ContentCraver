package com.scalx.contentcraver.hackernews.entity;

import com.scalx.contentcraver.BaseCard;

public class HNCard extends BaseCard {

    public HNCard(String articleId,
                  String title,
                  String mainTopic,
                  String url,
                  String author,
                  String text,
                  int upvoteCount,
                  int commentCount,
                  int created) {

        super(articleId, title, mainTopic, url, author, text, upvoteCount, commentCount, created);
    }
}