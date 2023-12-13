package com.exam.Entity;

import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Getter;

@Getter

public class LoginIn {
    private String userName;
    private String userID;
    private String email;
    private String password;
}
