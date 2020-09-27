package com.scalx.contentcraver.utils;

import com.scalx.contentcraver.BaseCard;
import com.scalx.contentcraver.BaseComment;

import javax.inject.Named;
import java.io.IOException;
import java.util.List;

public interface Strategy {

    public List<BaseCard> getArticleLinks(String articleTopic) throws IOException;

    public List<BaseComment> getArticleComments(String articleLink) throws IOException;
}
