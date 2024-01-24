
package com.exam.serviceImplementation;

import com.exam.Entity.UserTask;
import com.exam.mapper.TasksMapper;
import com.exam.service.TaskService;
import com.exam.util.nullOrNot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TaskServiceImplementation implements TaskService {

    private final TasksMapper tasksMapper;

    @Autowired
    public TaskServiceImplementation(TasksMapper tasksMapper) {
        this.tasksMapper = tasksMapper;
    }

    public List<UserTask> queryUserTodolistById(Integer UserID, Integer type) {
        List<UserTask> userTask = tasksMapper.queryUserTodolistById(UserID, type);
        nullOrNot.istrue(userTask == null, "未找到您的任务");//判断是否存在task
        System.out.println("正在执行查询");
        System.out.println("impl userTask="+userTask);
        return userTask;
    }

    public List<UserTask> queryUserTodolistByDdl(String taskDdl, Integer type, Integer UserID) {
        List<UserTask> userTask = tasksMapper.queryUserTodolistByDdl(taskDdl, type, UserID);
        nullOrNot.istrue(userTask == null, "未找到您的任务");//判断是否存在task
        System.out.println("正在执行查询");
        System.out.println("impl queryUserTodolistByDdl userTask="+userTask);
        return userTask;
    }

    public List<UserTask> userTodolistOrder(Integer UserID, Integer type) {
        List<UserTask> userTask = tasksMapper.queryUserTodolistByIdOrder(UserID, type);
        nullOrNot.istrue(userTask == null, "未找到您的任务");//判断是否存在task
        return userTask;
    }

    public List<UserTask> userTodolistFinishedOrder(Integer UserID, Integer type) {
        List<UserTask> userTask = tasksMapper.queryUserTodolistFinishedByIdOrder(UserID, type);
        nullOrNot.istrue(userTask == null, "未找到您的任务");//判断是否存在task
        return userTask;
    }


    @Transactional
    public UserTask updateTaskType( Integer taskId, Integer taskType) {
        UserTask userTask = tasksMapper.queryTask(taskId);
        nullOrNot.istrue(userTask == null, "该任务不存在");
        if (taskType == 0) {
            LocalDateTime localDateTime = LocalDateTime.now();
            tasksMapper.updateTaskType(taskId, 1, localDateTime);
        } else if (taskType == 1) {
            tasksMapper.updateTaskType(taskId, 0, null);
        }
        UserTask userTask1 = tasksMapper.queryTask(taskId);
        return userTask1;
    }

    @Transactional
    public List<UserTask> insertTask(Integer UserID, String taskContent, String taskDdl) {
        nullOrNot.istrue(taskContent == null, "内容不能为空");
        System.out.println("------------------------------------------------------------------");
        System.out.println("TaskServiceLmpl insertTask taskUserId=" + UserID);
        System.out.println("TaskServiceLmpl insertTask taskContent=" + taskContent);
        System.out.println("TaskServiceImplementation insertTask taskDdl=" + taskDdl);
        System.out.println("------------------------------------------------------------------");
        UserTask userTasknew = new UserTask();
        userTasknew.setUserID(UserID);
        userTasknew.setTaskContent(taskContent);
        userTasknew.setTaskDdl(taskDdl);

        int rowsAffected = tasksMapper.insertTask(userTasknew);
        System.out.println("rowsAffected="+rowsAffected);
        if (rowsAffected > 0) {
            System.out.println("rowsAffected="+rowsAffected);
            List<UserTask> userTask = tasksMapper.queryUserTodolistById(UserID, 0);
            return userTask;
        } else {
            System.out.println("qqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq");
            return null;
        }
        //List<UserTask> userTask = tasksMapper.queryUserTodolistById(taskUserId, 0);
    }

    @Transactional
    public UserTask updateTask(Integer taskId, String taskContent) {
        nullOrNot.istrue(taskContent == null, "内容不能为空");
        tasksMapper.updateTask(taskId, taskContent);
        UserTask userTask = tasksMapper.queryTask(taskId);
        return userTask;
    }

    @Transactional
    public String deleteTask(Integer taskId) {
        UserTask userTask = tasksMapper.queryTask(taskId);
        nullOrNot.istrue(userTask == null, "该任务不存在");
        tasksMapper.deleteTask(taskId);
        return "删除成功";
    }

}



