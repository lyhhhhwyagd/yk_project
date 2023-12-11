package com.exam.service;
import com.exam.Entity.Post;

import java.util.List;

public interface PostService {

    List<Post> selectAll();

    Post selectById(String postID);

    int update(String postID,Post post);

    int insert(Post post);

    int deleteById(String postID);
}
