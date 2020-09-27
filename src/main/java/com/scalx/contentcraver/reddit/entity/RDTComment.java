package com.scalx.contentcraver.reddit.entity;

import com.scalx.contentcraver.BaseComment;

public class RDTComment extends BaseComment {

    public RDTComment(String articleId,
                      String commentId,
                      String text,
                      String user,
                      String parentCommentId,
                      int created) {

        super(articleId, commentId, text, user, parentCommentId, created);
    }
}