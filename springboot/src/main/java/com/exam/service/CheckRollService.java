package com.exam.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.exam.Entity.CheckRoll;
import com.exam.mapper.CheckRollMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;

@Service
public class CheckRollService {
    @Autowired
    private CheckRollMapper checkRollMapper;
    public List<CheckRoll> checkRollList(){
        System.out.println("开始查询");
        return checkRollMapper.selectList();
    }
}
