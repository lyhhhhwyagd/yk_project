package com.exam.mapper;

import com.exam.Entity.LoginIn;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
public interface LoginInMapper {
    @Select("select * from user where UserEmail = #{email} and PassWord = #{password}")
    LoginIn select(LoginIn loginIn);

    @Select("select * from user where UserID = #{userID}")
    LoginIn selectByID(String userID);
}
