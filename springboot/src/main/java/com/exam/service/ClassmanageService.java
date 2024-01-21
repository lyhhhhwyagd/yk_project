package com.exam.service;

import com.exam.Entity.Classmanage;

import java.util.List;

public interface ClassmanageService {
    /*
    List<UserTask> queryUserTodolistById(Integer userID, Integer type);
    UserTask updateTaskType(Integer taskUserId, Integer taskId, Integer taskType);
    List<UserTask> insertTask(Integer taskUserId, String taskContent, String taskDdl);
    UserTask updateTask(Integer taskUserId, Integer taskId, String taskContent);
    String deleteTask(Integer taskUserId, Integer taskId);
    List<UserTask> userTodolistOrder(Integer id, Integer type);
    List<UserTask> userTodolistFinishedOrder(Integer userID, Integer type);
     */
    List<Classmanage> queryClassmanageByuserId(Integer userID);
    Classmanage updateclassmanage(Integer id, String studentId, String studentName, Double attendanceRate, Integer dailyScore);

    String deleteStudentclassmanage(Integer id);

    String insertclassmanage(String studentId, String studentName, Double attendanceRate, Integer dailyScore, Integer userId);

}