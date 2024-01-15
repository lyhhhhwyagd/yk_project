package com.exam.controller;


import com.exam.Entity.PostComment;
import com.exam.Entity.ApiResult;
import com.exam.service.PostCommentService;
import com.exam.util.ApiResultHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PostCommentController {

    private final PostCommentService postCommentService;
    @Autowired
    public PostCommentController(PostCommentService postCommentService){
        this.postCommentService = postCommentService;
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/postComment")
    public ApiResult selectAll(){
        return ApiResultHandler.success(postCommentService.selectAll());
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/postComment/{commentId}")
    public ApiResult selectById(@PathVariable("commentId") String commentID){
        System.out.println("根据ID查询帖子");
        return ApiResultHandler.success(postCommentService.selectById(commentID));
    }

    @CrossOrigin(origins = "*")
    @PutMapping("/postComment/{commentId}")
    public ApiResult update(@PathVariable("commentId") String commentID, @RequestBody PostComment postComment){
        System.out.println("更新指定ID的帖子");
        return ApiResultHandler.success(postCommentService.update(commentID,postComment));
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/postComment")
    public ApiResult insert(@RequestBody PostComment postComment){
        System.out.println("添加帖子");
        return ApiResultHandler.success(postCommentService.insert(postComment));
    }

    @CrossOrigin(origins = "*")
    @DeleteMapping("/postComment/{commentId}")
    public ApiResult deleteById(@PathVariable("commentId") String commentID){
        System.out.println("删除帖子");
        return ApiResultHandler.success(postCommentService.deleteById(commentID));
    }
}
