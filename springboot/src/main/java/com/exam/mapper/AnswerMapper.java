package com.exam.mapper;

import com.exam.Entity.Answer;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface AnswerMapper {

    @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id")
    @Insert("insert into xm04_answer(paper_id,question_id,question_type,create_time,answer_content) values(#{paperId},#{questionId},#{questionType},#{createTime},#{answerContent})")
    int addAnswer(Answer answer);

    @Select("select * from xm04_answer where question_id=#{questionId} order by id")
    List<Answer> selectByQuestionId(int questionId);
}