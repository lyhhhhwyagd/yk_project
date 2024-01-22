package com.exam.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.exam.Entity.User;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMapper extends BaseMapper<User> {
  @Select("SELECT user_type FROM user_zpq WHERE user_id = #{id}")
    String findUserTypeById(@Param("id") Integer id);
}
