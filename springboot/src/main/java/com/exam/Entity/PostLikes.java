package com.exam.Entity;

import lombok.Getter;

@Getter
public class PostLikes {
    private String ID;
    private String authorID;
    private String postID;
    private String createTime;


    public void setID(String ID) {
        this.ID = ID;
    }
    public void setAuthorID(String authorID) {
        this.authorID = authorID;
    }
    public void setPostID(String postID) {
        this.postID = postID;
    }
    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

}
