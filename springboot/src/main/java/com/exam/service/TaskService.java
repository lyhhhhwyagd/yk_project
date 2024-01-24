package com.exam.service;

import com.exam.Entity.UserTask;

import java.util.List;

public interface TaskService {
    List<UserTask> queryUserTodolistById(Integer userID, Integer type);
    List<UserTask> queryUserTodolistByDdl(String taskDdl, Integer type, Integer UserID);
    UserTask updateTaskType(Integer taskId, Integer taskType);
    List<UserTask> insertTask(Integer UserID, String taskContent, String taskDdl);
    UserTask updateTask(Integer taskId, String taskContent);
    String deleteTask(Integer taskId);
    List<UserTask> userTodolistOrder(Integer id, Integer type);
    List<UserTask> userTodolistFinishedOrder(Integer UserID, Integer type);
}