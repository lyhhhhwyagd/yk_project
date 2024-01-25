package com.exam.mapper;

import com.exam.Entity.Classmanage;
import org.apache.ibatis.annotations.*;

import java.util.List;


@Mapper

public interface ClassmanageMapper {
    @Select("SELECT * FROM xm04_classmanage WHERE userId=#{userId}")
    List<Classmanage> queryClassmanageByuserId(@Param("userId") Integer userId);

    @Select("SELECT * FROM xm04_classmanage WHERE studentName=#{studentName}")
    List<Classmanage> queryClassmanageBystudentName(@Param("studentName") String studentName);

    @Update("UPDATE xm04_classmanage SET studentId=#{studentId},studentName=#{studentName},attendanceRate=#{attendanceRate},dailyScore=#{dailyScore} WHERE classManageId=#{classManageId}")
    int updateclassmanage(@Param("classManageId") Integer classManageId,
                          @Param("studentId") String studentId,
                          @Param("studentName") String studentName,
                          @Param("attendanceRate") Double attendanceRate,
                          @Param("dailyScore") Integer dailyScore);

    @Select("SELECT * FROM xm04_classmanage WHERE classManageId=#{classManageId}")
    Classmanage queryClassmanageById(@Param("classManageId") Integer classManageId);

    @Select("SELECT * FROM xm04_classmanage WHERE studentId=#{studentId}")
    Classmanage queryClassmanageBystudentId(@Param("studentId") String studentId);

    @Delete("DELETE FROM xm04_classmanage WHERE classManageId=#{classManageId}")
    int deleteStudentclassmanage(@Param("classManageId") Integer classManageId);

    @Options(useGeneratedKeys = true, keyColumn = "classManageId", keyProperty = "classManageId")
    @Insert("INSERT INTO xm04_classmanage (studentId, studentName, attendanceRate, dailyScore, userId) VALUES (#{studentId}, #{studentName}, #{attendanceRate}, #{dailyScore}, #{userId})")
    /*
    int insertclassmanage(@Param("studentId") String studentId,
                          @Param("studentName") String studentName,
                          @Param("attendanceRate") Double attendanceRate,
                          @Param("dailyScore") Integer dailyScore,
                          @Param("userId") Integer userId);
                          不知道什么原因，似乎和版本有问题
                          已经检查： 数据库设置问题（主键，主键自增）
                                    实体书写问题，已经显示指明主键
                                    考虑多参数传递问题，但现有文档指向直接写明独有的id是通用的方案，project.projectId会被多对象影响
     */
    int insertclassmanage(Classmanage classmanage);



}
