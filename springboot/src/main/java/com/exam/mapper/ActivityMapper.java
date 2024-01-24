package com.exam.mapper;
import com.exam.Entity.Activity;
import com.exam.Entity.ActivityS;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ActivityMapper {
    @Select("select * from activities")
    List<Activity> selectList();

    @Options(useGeneratedKeys = true,keyProperty = "ActivityID")
    @Insert("insert into activities(ActivityID,ActivityName,ActivityDate,ActivityDuration,ActivityContent,Remarks)" +
            " values (#{ActivityID},#{ActivityName},#{ActivityDate},#{ActivityDuration},#{ActivityContent},#{Remarks})")
    void insert(Activity activity);

    @Delete("delete  from activities where ActivityID=#{ActivityID}")
    void deleteById(String id);

    @Update("update activities set ActivityName=#{ActivityName},ActivityDate=#{ActivityDate},ActivityDuration=#{ActivityDuration},ActivityContent=#{ActivityContent}," +
            "Remarks=#{Remarks} where ActivityID=#{ActivityID}")
    void updateById(Activity activity);

    @Select("SELECT " +
            "    YEAR(ActivityDate) AS Year, " +
            "    COUNT(*) AS Times, " +
            "    SUM(ActivityDuration) AS Duration " +
            "FROM activities " +
            "GROUP BY YEAR(ActivityDate) " +
            "ORDER BY Year;")
    List<ActivityS> statistic();
}
