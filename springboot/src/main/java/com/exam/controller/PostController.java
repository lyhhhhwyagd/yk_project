package com.exam.controller;


import com.exam.Entity.Post;
import com.exam.Entity.ApiResult;
import com.exam.service.PostService;
import com.exam.util.ApiResultHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class PostController {

    private final PostService postService;
    @Autowired
    public PostController(PostService postService){
        this.postService = postService;
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/post")
    public ApiResult selectAll(){
        System.out.println("查询全部帖子");
        return ApiResultHandler.success(postService.selectAll());
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/post/{PostId}")
    public ApiResult selectById(@PathVariable("PostId") String postsID){
        System.out.println("根据ID查询帖子");
        return ApiResultHandler.success(postService.selectById(postsID));
    }

    @CrossOrigin(origins = "*")
    @PutMapping("/post/{PostId}")
    public ApiResult update(@PathVariable("PostId") String postsID, @RequestBody Post post){
        System.out.println("更新指定ID的帖子");
        return ApiResultHandler.success(postService.update(postsID,post));
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/post")
    public ApiResult insert(@RequestBody Post post){
        System.out.println("添加帖子");
        System.out.println(post.getPostID());
        System.out.println(post.getPostTime());
        return ApiResultHandler.success(postService.insert(post));
    }

    @CrossOrigin(origins = "*")
    @DeleteMapping("/post/{PostId}")
    public ApiResult deleteById(@PathVariable("PostId") String postsID){
        System.out.println("删除帖子");
        return ApiResultHandler.success(postService.deleteById(postsID));
    }
}
