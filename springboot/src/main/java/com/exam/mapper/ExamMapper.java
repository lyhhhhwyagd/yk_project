package com.exam.mapper;
import com.exam.Entity.Exam;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ExamMapper {
    @Select("select * from exam_manage")
    List<Exam> selectList();

    @Options(useGeneratedKeys = true,keyProperty = "examCode")
    @Insert("insert into exam_manage(examCode,description,source,paperId,examDate,totalTime,grade,term,major,institute,totalScore,type,tips)" +
            " values (#{examCode},#{description},#{source},#{paperId},#{examDate},#{totalTime},#{grade}" +
            ",#{term},#{major},#{institute},#{totalScore},#{type},#{tips})")
    void insert(Exam exam);

    @Delete("delete  from exam_manage where examCode=#{examCode}")
    void deleteById(String id);

    @Update("update exam_manage set description=#{description},source=#{source},paperId=#{paperId},examDate=#{examDate},totalTime=#{totalTime},grade=#{grade},term=#{term}," +
            "major=#{major},institute=#{institute},totalScore=#{totalScore},type=#{type},tips=#{tips} where examCode=#{examCode}")
    void updateById(Exam exam);
}
