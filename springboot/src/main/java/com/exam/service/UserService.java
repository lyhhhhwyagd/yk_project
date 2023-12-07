package com.exam.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.exam.Entity.User;
import com.exam.mapper.UserMapper;
import com.exam.util.ExportExcelUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.LinkedHashMap;
import java.util.List;
@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    public List<User> userList(){//查所有的
        return userMapper.selectList(new QueryWrapper<>());
    }
    public void userAdd(User user){//添加
        userMapper.insert(user);
    }
    public void userDelete(Integer id){//删除by ID
        userMapper.deleteById(id);
    }
    public void userUpdate(User user){
        UpdateWrapper<User> wrapper = new UpdateWrapper<>();
        wrapper.eq("user_id", user.getUserId());
        userMapper.update(user, wrapper);
    }

    public byte[] downloadUser() {
        List<User> users = userList();
        LinkedHashMap<String, String> fieldMap = new LinkedHashMap<>();
        fieldMap.put("userId", "用户ID");
        fieldMap.put("userName", "用户名");
        fieldMap.put("userType","用户类型");
        fieldMap.put("createTime","创建时间");
        fieldMap.put("password","密码");
        fieldMap.put("userEmail","邮箱");
        fieldMap.put("userPhone","手机");
        return ExportExcelUtils.export("用户列表", users, fieldMap); // 在出现异常时返回一个空数组
    }
    public User getUserById(Integer id) {
        return userMapper.selectById(id);
    }
}

