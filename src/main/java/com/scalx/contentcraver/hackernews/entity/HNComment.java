package com.scalx.contentcraver.hackernews.entity;

import com.scalx.contentcraver.BaseComment;

public class HNComment extends BaseComment {

    public HNComment(
            String id,
            String storyId,
            String text,
            String user,
            String parentCommentId,
            int created) {

        super(id, storyId, text, user, parentCommentId, created);

    }
}