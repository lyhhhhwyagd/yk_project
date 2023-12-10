package com.exam.mapper;

import com.exam.Entity.CheckRoll;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CheckRollMapper{
    @Select("select student_id, student_name from teach_student")
    List<CheckRoll> selectList();
}
