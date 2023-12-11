package com.exam.controller;

import com.exam.Entity.LoginIn;
import com.exam.Entity.ApiResult;
import com.exam.service.LoginInService;
import com.exam.util.ApiResultHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class LoginInController {
    private final LoginInService loginInService;
    @Autowired
    public LoginInController(LoginInService loginInService){
        this.loginInService = loginInService;
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/loginIn")
    public ApiResult select(@RequestBody LoginIn loginIn){
        System.out.println("匹配用户");
        System.out.println(loginIn.getPassword());
        return ApiResultHandler.success(loginInService.select(loginIn));
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/loginIn/{userID}")
    public ApiResult selectById(@PathVariable("userID") String userID){
        System.out.println("根据ID查询悬赏信息");
        return ApiResultHandler.success(loginInService.selectByID(userID));
    }
}
