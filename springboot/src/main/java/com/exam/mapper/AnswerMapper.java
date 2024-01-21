package com.exam.mapper;

import com.exam.Entity.Answer;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface AnswerMapper {
    @Insert("insert into answer(paper_id,question_id,question_type,create_time,answer_content) values(#{paperId},#{questionId},#{questionType},#{createTime},#{answerContent})")
    int addAnswer(Answer answer);

    @Select("select * from answer where question_id=#{questionId} order by id")
    List<Answer> selectByQuestionId(int questionId);
}