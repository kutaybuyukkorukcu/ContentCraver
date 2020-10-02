package com.scalx.contentcraver.helper;

import com.scalx.contentcraver.BaseCard;
import com.scalx.contentcraver.BaseComment;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import java.io.IOException;
import java.util.List;

@ApplicationScoped
@Named
public class Context {

    private CrawlerStrategy crawlerStrategy;

    public void setCrawlerStrategy(CrawlerStrategy crawlerStrategy) {
        this.crawlerStrategy = crawlerStrategy;
    }

    public List<BaseCard> getArticleLinks(String subreddit) throws IOException {
        return crawlerStrategy.getArticleLinks(subreddit);
    }

    public List<BaseComment> getArticleComments(String articleLink) throws IOException {
        return crawlerStrategy.getArticleComments(articleLink);
    }
}