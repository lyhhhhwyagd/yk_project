package com.exam.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.exam.Entity.Exam;
import com.exam.mapper.ExamMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;

@Service
public class ExamService {
    @Autowired
    private ExamMapper examMapper;
    public List<Exam> examList(){
        System.out.println("开始查询");
        return examMapper.selectList();
    }
    public void examAdd(Exam exam){//添加
        examMapper.insert(exam);
    }

    public void examDelete(String id){//删除by ID
        examMapper.deleteById(id);
    }

    public void updateById(Exam exam) {
        examMapper.updateById(exam);
    }
}
