package com.scalx.contentcraver.reddit.entity;

import com.scalx.contentcraver.BaseComment;

public class RDTComment extends BaseComment {

    public RDTComment(String id,
                      String storyId,
                      String text,
                      String user,
                      String parentCommentId,
                      int created) {

        super(id, storyId, text, user, parentCommentId, created);
    }
}