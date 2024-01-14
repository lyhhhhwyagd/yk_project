
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

    public List<UserTask> queryUserTodolistById(Integer userID, Integer type) {
        List<UserTask> userTask = tasksMapper.queryUserTodolistById(userID, type);
        nullOrNot.istrue(userTask == null, "未找到您的任务");//判断是否存在task
        System.out.println("正在执行查询");
        System.out.println("impl userTask="+userTask);
        return userTask;
    }

    public List<UserTask> userTodolistOrder(Integer userID, Integer type) {
        List<UserTask> userTask = tasksMapper.queryUserTodolistByIdOrder(userID, type);
        nullOrNot.istrue(userTask == null, "未找到您的任务");//判断是否存在task
        return userTask;
    }

    public List<UserTask> userTodolistFinishedOrder(Integer userID, Integer type) {
        List<UserTask> userTask = tasksMapper.queryUserTodolistFinishedByIdOrder(userID, type);
        nullOrNot.istrue(userTask == null, "未找到您的任务");//判断是否存在task
        return userTask;
    }


    @Transactional
    public UserTask updateTaskType(Integer taskUserId, Integer taskId, Integer taskType) {
        UserTask userTask = tasksMapper.queryTask(taskUserId, taskId);
        nullOrNot.istrue(userTask == null, "该任务不存在");
        if (taskType == 0) {
            LocalDateTime localDateTime = LocalDateTime.now();
            tasksMapper.updateTaskType(taskUserId, taskId, 1, localDateTime);
        } else if (taskType == 1) {
            tasksMapper.updateTaskType(taskUserId, taskId, 0, null);
        }
        UserTask userTask1 = tasksMapper.queryTask(taskUserId, taskId);
        return userTask1;
    }

    @Transactional
    public List<UserTask> insertTask(Integer taskUserId, String taskContent, String taskDdl) {
        nullOrNot.istrue(taskContent == null, "内容不能为空");
        System.out.println("------------------------------------------------------------------");
        System.out.println("TaskServiceLmpl insertTask taskUserId=" + taskUserId);
        System.out.println("TaskServiceLmpl insertTask taskContent=" + taskContent);
        System.out.println("TaskServiceImplementation insertTask taskDdl=" + taskDdl);
        System.out.println("------------------------------------------------------------------");
        tasksMapper.insertTask(taskUserId, taskContent, taskDdl);
        int rowsAffected = tasksMapper.insertTask(taskUserId, taskContent, taskDdl);
        System.out.println("rowsAffected="+rowsAffected);
        if (rowsAffected > 0) {
            System.out.println("rowsAffected="+rowsAffected);
            List<UserTask> userTask = tasksMapper.queryUserTodolistById(taskUserId, 0);
            return userTask;
        } else {
            System.out.println("qqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq");
            return null;
        }
        //List<UserTask> userTask = tasksMapper.queryUserTodolistById(taskUserId, 0);
    }

    @Transactional
    public UserTask updateTask(Integer taskUserId, Integer taskId, String taskContent) {
        nullOrNot.istrue(taskContent == null, "内容不能为空");
        tasksMapper.updateTask(taskUserId, taskId, taskContent);
        UserTask userTask = tasksMapper.queryTask(taskUserId, taskId);
        return userTask;
    }

    @Transactional
    public String deleteTask(Integer taskUserId, Integer taskId) {
        UserTask userTask = tasksMapper.queryTask(taskUserId, taskId);
        nullOrNot.istrue(userTask == null, "该任务不存在");
        tasksMapper.deleteTask(taskUserId, taskId);
        return "删除成功";
    }

}



