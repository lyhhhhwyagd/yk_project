package com.exam.service;
import com.exam.Entity.PostLikes;

import java.util.List;

public interface PostLikesService {

    List<PostLikes> selectAll();

    int insert(PostLikes postLikes);

    int deleteByAuthorIDAndPostID(String authorID, String postID);
}
