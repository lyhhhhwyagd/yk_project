package com.exam.service;

import com.exam.entity.Course;
import com.exam.mapper.CourseMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service

public class CourseService {
    @Resource
    private CourseMapper courseMapper;
    //total 总数 list 数据列表
    //pageNum 当前页码  pageSize 总数
    public PageInfo<Course> selectPage(Integer pageNum,Integer pageSize,Course course)
    {
          PageHelper.startPage(pageNum,pageSize);
          List<Course> courseList=courseMapper.selectAll(course);
          return PageInfo.of(courseList);
    }

    public void add(Course course) {
        courseMapper.insert(course);
    }

    public void updateById(Course course) {
        courseMapper.updateById(course);
    }

    public void deleteById(Integer id) {
        courseMapper.deleteById(id);
    }
    public   List<Course> listAll()
    {
        List<Course> courseList=courseMapper.listAll();
        return courseList;
    }
}
