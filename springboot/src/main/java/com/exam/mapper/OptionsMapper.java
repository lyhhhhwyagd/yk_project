package com.exam.mapper;

        import org.apache.ibatis.annotations.*;
        import org.springframework.stereotype.Repository;

        import java.util.List;

@Mapper
@Repository
public interface OptionsMapper {

    @Insert("insert into options(question_id,sequence,content) values(#{questionId},#{sequence},#{content})")
    int addOption(com.exam.Entity.Options Options);

    @Select("select * from options where question_id=#{questionId} and sequence=#{sequence}")
    com.exam.Entity.Options findOption(com.exam.Entity.Options Options);

    @Update("update options set content=#{content} where question_id=#{questionId} and sequence=#{sequence}")
    void updateOption(com.exam.Entity.Options Options);

    @Delete("delete from options where question_id=#{questionId}")
    void deleteByQuestionId(Integer questionId);

    @Select("select * from options where question_id=#{questionId} order by sequence")
    List<com.exam.Entity.Options> selectByQuestionId(int questionId);
}
