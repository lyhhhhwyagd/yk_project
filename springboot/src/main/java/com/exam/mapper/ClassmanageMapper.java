package com.exam.mapper;

import com.exam.Entity.Classmanage;
import org.apache.ibatis.annotations.*;

import java.util.List;


@Mapper

public interface ClassmanageMapper {
    @Select("SELECT * FROM classmanage WHERE userId=#{userId}")
    List<Classmanage> queryClassmanageByuserId(@Param("userId") Integer userId);

    @Update("UPDATE classmanage SET studentId=#{studentId},studentName=#{studentName},attendanceRate=#{attendanceRate},dailyScore=#{dailyScore} WHERE classManageId=#{classManageId}")
    int updateclassmanage(@Param("classManageId") Integer classManageId,
                          @Param("studentId") String studentId,
                          @Param("studentName") String studentName,
                          @Param("attendanceRate") Double attendanceRate,
                          @Param("dailyScore") Integer dailyScore);

    @Select("SELECT * FROM classmanage WHERE classManageId=#{classManageId}")
    Classmanage queryClassmanageById(@Param("classManageId") Integer classManageId);

    @Select("SELECT * FROM classmanage WHERE studentId=#{studentId}")
    Classmanage queryClassmanageBystudentId(@Param("studentId") String studentId);

    @Delete("DELETE FROM classmanage WHERE classManageId=#{classManageId}")
    int deleteStudentclassmanage(@Param("classManageId") Integer classManageId);

    @Options(useGeneratedKeys = true, keyColumn = "classManageId", keyProperty = "classManageId")
    @Insert("INSERT INTO classmanage (studentId, studentName, attendanceRate, dailyScore, userId) VALUES (#{studentId}, #{studentName}, #{attendanceRate}, #{dailyScore}, #{userId})")
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



    /*
    @Select("SELECT * FROM classmanage WHERE taskUserId=#{taskUserId} AND taskType=#{taskType}")
    List<UserTask> queryUserTodolistById(@Param("taskUserId") Integer taskUserId,
                                         @Param("taskType") Integer taskType);

    @Select("SELECT * FROM userTodolist WHERE taskUserId=#{taskUserId} AND taskType=#{taskType} ORDER BY taskDdl ASC")
    List<UserTask> queryUserTodolistByIdOrder(@Param("taskUserId") Integer taskUserId,
                                              @Param("taskType") Integer taskType);

    @Select("SELECT * FROM userTodolist WHERE taskUserId=#{taskUserId} AND taskType=#{taskType} ORDER BY taskFinishTime DESC")
    List<UserTask> queryUserTodolistFinishedByIdOrder(@Param("taskUserId") Integer taskUserId,
                                                      @Param("taskType") Integer taskType);

    @Update("UPDATE userTodolist SET taskType=#{taskType}, taskFinishTime=#{finishTime} WHERE taskUserId=#{taskUserId} AND taskId=#{taskId}")
    int updateTaskType(@Param("taskUserId") Integer taskUserId,
                       @Param("taskId") Integer taskId,
                       @Param("taskType") Integer taskType,
                       @Param("finishTime") LocalDateTime finishTime);

    @Select("SELECT * FROM userTodolist WHERE taskUserId=#{taskUserId} AND taskId=#{taskId}")
    UserTask queryTask(@Param("taskUserId") Integer taskUserId,
                       @Param("taskId") Integer taskId);

    @Insert("INSERT INTO userTodolist (taskUserId, taskContent, taskType, taskCreateTime, taskFinishTime, taskDdl) VALUES (#{taskUserId}, #{taskContent}, 0, NOW(), NULL, #{taskDdl})")
    @Options(useGeneratedKeys = true, keyProperty = "taskId")
    int insertTask(@Param("taskUserId") Integer taskUserId,
                   @Param("taskContent") String taskContent,
                   @Param("taskDdl") String taskDdl);

    @Update("UPDATE userTodolist SET taskContent=#{taskContent} WHERE taskUserId=#{taskUserId} AND taskId=#{taskId}")
    int updateTask(@Param("taskUserId") Integer taskUserId,
                   @Param("taskId") Integer taskId,
                   @Param("taskContent") String taskContent);

    @Delete("DELETE FROM userTodolist WHERE taskUserId=#{taskUserId} AND taskId=#{taskId}")
    int deleteTask(@Param("taskUserId") Integer taskUserId,
                   @Param("taskId") Integer taskId);*/


}
