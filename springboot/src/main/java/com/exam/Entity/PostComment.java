package com.exam.Entity;

import lombok.Getter;

@Getter
public class PostComment {
    private String commentID;
    private String authorID;
    private String postID;
    private String postAuthorID;
    private String content;
    private String postTime;
    private String updateTime;
    private String status;

    public void setCommentID(String commentID) {
        this.commentID = commentID;
    }
    public void setAuthorID(String authorID) {
        this.authorID = authorID;
    }
    public void setPostID(String postID) {
        this.postID = postID;
    }
    public void setPostAuthorID(String postAuthorID) {
        this.postAuthorID = postAuthorID;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public void setPostTime(String postTime) {
        this.postTime = postTime;
    }
    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
    public void setStatus(String status) {
        this.status = status;
    }
}
