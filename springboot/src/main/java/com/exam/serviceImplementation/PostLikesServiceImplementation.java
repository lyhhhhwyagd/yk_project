package com.exam.serviceImplementation;

import com.exam.Entity.PostLikes;
import com.exam.mapper.PostLikesMapper;
import com.exam.service.PostLikesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostLikesServiceImplementation implements PostLikesService{

    private final PostLikesMapper postLikesMapper;
    @Autowired
    public PostLikesServiceImplementation(PostLikesMapper postLikesMapper){
        this.postLikesMapper = postLikesMapper;
    }

    public List<PostLikes> selectAll(){
        System.out.println("正在执行查询");
        return postLikesMapper.selectAll();
    }

    public int insert(PostLikes postLikes){
        return postLikesMapper.insert(postLikes);
    }

    public int deleteByAuthorIDAndPostID(String authorID,String postID){
        return postLikesMapper.deleteByAuthorIDAndPostID(authorID, postID);
    }
}
