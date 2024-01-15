package com.exam.Entity;

import lombok.Getter;

@Getter
public class Post {
    private String postID;
    private String title;
    private String content;
    private String postTime;
    private String updateTime;
    private String commentsCount;
    private String authorID;
    private String authorName;
    private String status;

    public void setPostID(String postID) {
        this.postID = postID;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public void setPostedTime(String postTime) {
        this.postTime = postTime;
    }
    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
    public void setCommentsCount(String commentsCount) {
        this.commentsCount = commentsCount;
    }
    public void setAuthorID(String authorID) {
        this.authorID = authorID;
    }
    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }
    public void setStatus(String status) {
        this.status = status;
    }
}
