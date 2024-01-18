package com.exam.service;

import com.exam.Entity.Problem;
import com.exam.mapper.ProblemMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service

public class ProblemService {
    @Resource
    private ProblemMapper problemMapper;
    //total 总数 list 数据列表
    //pageNum 当前页码  pageSize 总数
    public PageInfo<Problem> selectPages(Integer pageNum, Integer pageSize, Problem problem)
    {
        PageHelper.startPage(pageNum,pageSize);
        List<Problem> problemList=problemMapper.selectAll(problem);
        return PageInfo.of(problemList);
    }

    public void add(Problem problem) {
        problemMapper.insert(problem);
    }

    public void updateById(Problem problem) {
        problemMapper.updateById(problem);
    }

    public void deleteById(Integer id) {
        problemMapper.deleteById(id);
    }
    public   List<Problem> listAll()
    {
        List<Problem> problemList=problemMapper.listAll();
        return problemList;
    }
}
