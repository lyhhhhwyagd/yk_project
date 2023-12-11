package com.exam.serviceImplementation;

import com.exam.Entity.LoginIn;
import com.exam.mapper.LoginInMapper;
import com.exam.service.LoginInService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoginInServiceImplementation implements LoginInService{

    private final LoginInMapper loginInMapper;
    @Autowired
    public LoginInServiceImplementation(LoginInMapper loginInMapper){
        this.loginInMapper = loginInMapper;
    }

    public LoginIn select(LoginIn loginIn){
        System.out.println("正在执行查询");
        return loginInMapper.select(loginIn);
    }
    public LoginIn selectByID(String userID){
        System.out.println("正在根据id执行查询");
        return loginInMapper.selectByID(userID);
    }
}
