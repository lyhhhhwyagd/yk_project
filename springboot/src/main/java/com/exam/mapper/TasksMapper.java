package com.exam.mapper;

import com.exam.Entity.UserTask;
import org.apache.ibatis.annotations.*;

import java.time.LocalDateTime;
import java.util.List;


@Mapper

public interface TasksMapper {

    @Select("SELECT * FROM userTodolist WHERE taskUserId=#{taskUserId} AND taskType=#{taskType}")
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
                   @Param("taskId") Integer taskId);


}
