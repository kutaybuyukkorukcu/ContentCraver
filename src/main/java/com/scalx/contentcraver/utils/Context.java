package com.scalx.contentcraver.utils;

import com.scalx.contentcraver.BaseCard;
import com.scalx.contentcraver.BaseComment;

import java.io.IOException;
import java.util.List;

public class Context {

    private Strategy strategy;

    public void setStrategy(Strategy strategy) {
        this.strategy = strategy;
    }

    public List<BaseCard> getArticleLinks(String subreddit) throws IOException {
        return strategy.getArticleLinks(subreddit);
    }

    public List<BaseComment> getArticleComments(String articleLink) throws IOException {
        return strategy.getArticleComments(articleLink);
    }
}