package com.exam.mapper;
import com.exam.Entity.Exam;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ExamMapper {
    @Select("select * from exam_manage")
    List<Exam> selectList();
}
