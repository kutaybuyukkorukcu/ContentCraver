package com.scalx.contentcraver.hackernews.entity;

import com.scalx.contentcraver.BaseComment;

public class HNComment extends BaseComment {

    public HNComment(
            String articleId,
            String commentId,
            String text,
            String user,
            String parentCommentId,
            int created) {

        super(articleId, commentId, text, user, parentCommentId, created);

    }
}