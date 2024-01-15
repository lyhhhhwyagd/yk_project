package com.exam.serviceImplementation;

import com.exam.Entity.PostComment;
import com.exam.mapper.PostCommentMapper;
import com.exam.service.PostCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostCommentServiceImplementation implements PostCommentService{

    private final PostCommentMapper postCommentMapper;
    @Autowired
    public PostCommentServiceImplementation(PostCommentMapper postCommentMapper){
        this.postCommentMapper = postCommentMapper;
    }

    public List<PostComment> selectAll(){
        System.out.println("正在执行查询");
        return postCommentMapper.selectAll();
    }

    public PostComment selectById(String commentID){
        System.out.println("正在执行查询"+commentID);
        return postCommentMapper.selectById(commentID);
    }

    public int update(String commentID,PostComment postComment){
        System.out.println("正在执行修改");
        return postCommentMapper.update(commentID,postComment);
    }

    public int insert(PostComment postComment){
        return postCommentMapper.insert(postComment);
    }

    public int deleteById(String commentID){
        return postCommentMapper.deleteById(commentID);
    }
}
