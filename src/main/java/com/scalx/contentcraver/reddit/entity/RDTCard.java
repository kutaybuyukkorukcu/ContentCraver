package com.scalx.contentcraver.reddit.entity;

import com.scalx.contentcraver.BaseCard;

// Reddit
public class RDTCard extends BaseCard {

    public RDTCard(String articleId,
                   String title,
                   String mainTopic,
                   String url,
                   String author,
                   String text,
                   int upvoteCount,
                   int numberOfComments,
                   int created) {

        super(articleId, title, mainTopic, url, author, text, upvoteCount, numberOfComments, created);
    }
}
