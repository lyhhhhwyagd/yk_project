package com.exam.mapper;

import com.exam.Entity.Paper;
import com.exam.util.PaperStatus;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface PaperMapper {

    @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id")
    @Insert("insert into paper(user_id,title,description,start_time,end_time,status) values(#{userId},#{title},#{description},#{startTime},#{endTime},#{status})")
    int addPaper(Paper paper);

    @Update("update paper set title=#{title},description=#{description},start_time=#{startTime},end_time=#{endTime},status=#{status} where id=#{id}")
    void updatePaper(Paper paper);

    @Select("select * from paper where id=#{paperId}")
    Paper selectByPaperId(int paperId);

    @Delete("delete from paper where id=#{paperId}")
    void deletePaper(int paperId);

    @Select("select * from paper where user_id=#{userId}")
    List<Paper> getUserPapers(int userId);

    @Select("select * from paper where start_time is not null")
    List<Paper> getTimePapers();

    @Update("update paper set status=#{paperStatus} where id=#{id}")
    void changeStatus(@Param("paperStatus") PaperStatus paperStatus, @Param("id") int id);
}
