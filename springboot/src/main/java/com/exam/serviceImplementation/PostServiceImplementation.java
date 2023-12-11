package com.exam.serviceImplementation;

import com.exam.Entity.Post;
import com.exam.mapper.PostMapper;
import com.exam.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostServiceImplementation implements PostService{

    private final PostMapper postMapper;
    @Autowired
    public PostServiceImplementation(PostMapper postMapper){
        this.postMapper = postMapper;
    }

    public List<Post> selectAll(){
        System.out.println("正在执行查询");
        return postMapper.selectAll();
    }

    public Post selectById(String postID){
        System.out.println("正在执行查询"+postID);
        return postMapper.selectById(postID);
    }

    public int update(String postID,Post post){
        System.out.println("正在执行修改");
        return postMapper.update(postID,post);
    }

    public int insert(Post post){
        return postMapper.insert(post);
    }

    public int deleteById(String postID){
        return postMapper.deleteById(postID);
    }
}
