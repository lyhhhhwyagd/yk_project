package com.exam.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.exam.Entity.Course;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import org.apache.ibatis.annotations.*;

import java.util.List;


/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author jobob
 * @since 2023-12-02
 */
@Mapper
public interface CourseMapper {

    @Select("select * from xm04_course where course_name like concat('%',#{courseName},'%')  and dep like concat('%',#{dep},'%') " +
            " order by id desc")
    List<Course> selectAll(Course course);

    @Insert("insert into xm04_course (course_name,course_id,teacher_id,bonus,dep,class_id)values (#{courseName},#{courseId},#{teacherId},#{bonus},#{dep},#{classId})")
    void insert(Course course);
//    IPage pageC(IPage<Course> page);
//
//    IPage pageCC(IPage<Course> page,@Param(Constants.WRAPPER ) Wrapper wrapper);
    @Update("update xm04_course set course_name=#{courseName},course_id=#{courseId},teacher_id=#{teacherId},bonus=#{bonus},dep=#{dep},class_id=#{classId} where id=#{id}")
      void updateById(Course course);

    @Delete("delete  from  xm04_course where id=#{id}")
    void deleteById(Integer id);

    @Select("select * from xm04_course ")
    List<Course> listAll();
}
