package com.exam.controller;


import com.exam.Entity.PostComment;
import com.exam.Entity.PostLikes;
import com.exam.Entity.ApiResult;
import com.exam.service.PostLikesService;
import com.exam.util.ApiResultHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PostLikesController {

    private final PostLikesService postLikesService;
    @Autowired
    public PostLikesController(PostLikesService postLikesService){
        this.postLikesService = postLikesService;
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/postLikes")
    public ApiResult selectAll(){
        return ApiResultHandler.success(postLikesService.selectAll());
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/postLikes")
    public ApiResult insert(@RequestBody PostLikes postLikes){
        System.out.println("添加点赞");
        return ApiResultHandler.success(postLikesService.insert(postLikes));
    }

    @CrossOrigin(origins = "*")
    @DeleteMapping("/postLikes/{authorId}/{postID}")
    public ApiResult deleteByAuthorIDAndPostID(@PathVariable("authorId") String authorID, @PathVariable("postID") String postID){
        System.out.println("删除点赞");
        return ApiResultHandler.success(postLikesService.deleteByAuthorIDAndPostID(authorID,postID));
    }
}
