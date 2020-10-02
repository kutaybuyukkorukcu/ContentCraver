package com.scalx.contentcraver.helper;

import com.scalx.contentcraver.BaseCard;
import com.scalx.contentcraver.BaseComment;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.internal.ResteasyClientBuilderImpl;

import javax.inject.Named;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

public interface CrawlerStrategy {

    public List<BaseCard> getArticleLinks(String articleTopic) throws IOException;

    public List<BaseComment> getArticleComments(String articleLink) throws IOException;
}
