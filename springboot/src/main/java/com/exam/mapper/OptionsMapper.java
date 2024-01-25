package com.exam.mapper;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface OptionsMapper {

    @Options(useGeneratedKeys = true, keyColumn = "question_id", keyProperty = "questionId")
    @Insert("insert into xm04_options(question_id,sequence,content) values(#{questionId},#{sequence},#{content})")
    int addOption(com.exam.Entity.Options Options);

    @Select("select * from xm04_options where question_id=#{questionId} and sequence=#{sequence}")
    com.exam.Entity.Options findOption(com.exam.Entity.Options Options);

    @Update("update xm04_options set content=#{content} where question_id=#{questionId} and sequence=#{sequence}")
    void updateOption(com.exam.Entity.Options Options);

    @Delete("delete from xm04_options where question_id=#{questionId}")
    void deleteByQuestionId(Integer questionId);

    @Select("select * from xm04_options where question_id=#{questionId} order by sequence")
    List<com.exam.Entity.Options> selectByQuestionId(int questionId);
}
