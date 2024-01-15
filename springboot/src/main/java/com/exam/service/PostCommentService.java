package com.exam.service;
import com.exam.Entity.PostComment;

import java.util.List;

public interface PostCommentService {

    List<PostComment> selectAll();

    PostComment selectById(String commentID);

    int update(String commentID,PostComment postComment);

    int insert(PostComment postComment);

    int deleteById(String commentID);
}
