package com.scalx.contentcraver.helper;

import com.scalx.contentcraver.BaseStory;
import com.scalx.contentcraver.BaseComment;

import java.io.IOException;
import java.util.List;

public interface CrawlerStrategy {

    public List<BaseStory> getStoryLinks(String articleTopic) throws IOException;

    public List<BaseComment> getStoryComments(String articleLink) throws IOException;
}
