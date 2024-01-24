package com.exam.mapper;

import com.exam.Entity.UserTask;
import org.apache.ibatis.annotations.*;

import java.time.LocalDateTime;
import java.util.List;


@Mapper

public interface TasksMapper {

    @Select("SELECT * FROM userTodolist WHERE UserID=#{UserID} AND taskType=#{taskType}")
    List<UserTask> queryUserTodolistById(@Param("UserID") Integer UserID,
                                         @Param("taskType") Integer taskType);

    @Select("SELECT * FROM userTodolist WHERE taskDdl < #{taskDdl} AND taskType = #{taskType} AND UserID = #{UserID}")
    List<UserTask> queryUserTodolistByDdl(@Param("taskDdl") String taskDdl,
                                          @Param("taskType") Integer taskType,
                                          @Param("UserID") Integer UserID);


    @Select("SELECT * FROM userTodolist WHERE UserID=#{UserID} AND taskType=#{taskType} ORDER BY taskDdl ASC")
    List<UserTask> queryUserTodolistByIdOrder(@Param("UserID") Integer UserID,
                                              @Param("taskType") Integer taskType);

    @Select("SELECT * FROM userTodolist WHERE userID=#{userID} AND taskType=#{taskType} ORDER BY taskFinishTime DESC")
    List<UserTask> queryUserTodolistFinishedByIdOrder(@Param("userID") Integer userID,
                                                      @Param("taskType") Integer taskType);

    @Update("UPDATE userTodolist SET taskType=#{taskType}, taskFinishTime=#{finishTime} WHERE taskId=#{taskId}")
    int updateTaskType(
            @Param("taskId") Integer taskId,
            @Param("taskType") Integer taskType,
            @Param("finishTime") LocalDateTime finishTime);

    @Select("SELECT * FROM userTodolist WHERE taskId=#{taskId}")
    UserTask queryTask(
            @Param("taskId") Integer taskId);

    @Insert("INSERT INTO userTodolist ( taskContent, taskType, taskCreateTime, taskFinishTime, taskDdl, UserID) VALUES ( #{taskContent}, 0, NOW(), NULL, #{taskDdl}, #{UserID})")
    @Options(useGeneratedKeys = true, keyProperty = "taskId")
        //int insertTask(@Param("taskUserId") Integer taskUserId,
        //               @Param("taskContent") String taskContent,
        //              @Param("taskDdl") String taskDdl);
    int insertTask(UserTask userTask);
    @Update("UPDATE userTodolist SET taskContent=#{taskContent} WHERE taskId=#{taskId}")
    int updateTask(
            @Param("taskId") Integer taskId,
            @Param("taskContent") String taskContent);

    @Delete("DELETE FROM userTodolist WHEREtaskId=#{taskId}")
    int deleteTask(
            @Param("taskId") Integer taskId);


}
