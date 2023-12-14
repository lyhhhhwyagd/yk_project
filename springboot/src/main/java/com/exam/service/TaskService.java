package com.exam.service;

import com.exam.Entity.UserTask;

import java.util.List;

public interface TaskService {
    List<UserTask> queryUserTodolistById(Integer userID, Integer type);
    UserTask updateTaskType(Integer taskUserId, Integer taskId, Integer taskType);
    List<UserTask> insertTask(Integer taskUserId, String taskContent, String taskDdl);
    UserTask updateTask(Integer taskUserId, Integer taskId, String taskContent);
    String deleteTask(Integer taskUserId, Integer taskId);
    List<UserTask> userTodolistOrder(Integer id, Integer type);
    List<UserTask> userTodolistFinishedOrder(Integer userID, Integer type);
}