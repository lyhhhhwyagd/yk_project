package com.exam.mapper;

import com.exam.Entity.Question;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface QuestionMapper {

    @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id")
    @Insert("insert into xm04_question(paper_id) values(#{paperId})")
    int addQuestion(Question question);

    @Update("update xm04_question set type=#{type},title=#{title} where id=#{id}")
    void updateQuestion(Question question);

    @Select("select * from xm04_question where id=#{id}")
    Question selectByQuestionId(int id);

    @Delete("delete from xm04_question where id=#{questionId}")
    void deleteQuestion(int questionId);

    @Delete("delete from xm04_question where paper_id=#{paperId}")
    void deleteByPaperId(int paperId);

    @Select("select * from xm04_question where paper_id=#{paperId} order by id")
    List<Question> selectByPaperId(int paperId);
}
