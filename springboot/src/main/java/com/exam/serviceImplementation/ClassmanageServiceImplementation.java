
package com.exam.serviceImplementation;

import com.exam.Entity.Classmanage;
import com.exam.mapper.ClassmanageMapper;
import com.exam.service.ClassmanageService;
import com.exam.util.nullOrNot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ClassmanageServiceImplementation implements ClassmanageService {

    private final ClassmanageMapper classmanageMapper;

    @Autowired
    public ClassmanageServiceImplementation(ClassmanageMapper classmanageMapper) {
        this.classmanageMapper = classmanageMapper;
    }

    public List<Classmanage> queryClassmanageByuserId(Integer userId) {
        List<Classmanage> classmanage = classmanageMapper.queryClassmanageByuserId(userId);
        nullOrNot.istrue(classmanage == null, "未找到您的任务");//判断是否存在task
        System.out.println("正在执行查询");
        System.out.println("impl classmanage="+classmanage);
        return classmanage;
    }

    @Transactional
    public Classmanage updateclassmanage(Integer id, String studentId, String studentName, Double attendanceRate, Integer dailyScore) {
        nullOrNot.istrue(attendanceRate == null || dailyScore == null, "内容不能为空");
        classmanageMapper.updateclassmanage(id, studentId, studentName,attendanceRate,dailyScore);
        Classmanage classmanage = classmanageMapper.queryClassmanageById(id);
        return classmanage;
    }

    @Transactional
    public String deleteStudentclassmanage(Integer id) {
        Classmanage classmanage = classmanageMapper.queryClassmanageById(id);
        System.out.println("删除之前classmanage="+classmanage);
        nullOrNot.istrue(classmanage == null, "该学生不存在");
        classmanageMapper.deleteStudentclassmanage(id);
        Classmanage classmanagenew = classmanageMapper.queryClassmanageById(id);
        System.out.println("删除之后classmanagenew="+classmanagenew);
        if(classmanagenew == null)
            return "删除成功";
        else
            return "删除失败";
    }

    @Transactional
    public String insertclassmanage(String studentId, String studentName, Double attendanceRate, Integer dailyScore, Integer userId) {
        nullOrNot.istrue(studentId == null || studentName == null || attendanceRate == null || dailyScore == null, "内容不能为空");
        System.out.println("------------------------------------------------------------------");
        System.out.println("ClassmanageServiceLmpl insertclassmanage studentId=" + studentId);
        System.out.println("ClassmanageServiceLmpl insertclassmanage studentName=" + studentName);
        System.out.println("ClassmanageServiceImplementation insertclassmanage attendanceRate=" + attendanceRate);
        System.out.println("------------------------------------------------------------------");
        Classmanage classmanage1 = classmanageMapper.queryClassmanageBystudentId(studentId);
        System.out.println("是否存在学号重复classmanage="+classmanage1);
        if(classmanage1 != null)
        {
            return "已经存在该学号学生，学生学号不能重复";
        }else{
            Classmanage classmanagenew = new Classmanage();
            classmanagenew.setAttendanceRate(attendanceRate);
            classmanagenew.setUserId(userId);
            classmanagenew.setStudentId(studentId);
            classmanagenew.setStudentName(studentName);
            classmanagenew.setDailyScore(dailyScore);
            //classmanageMapper.insertclassmanage(studentId, studentName, attendanceRate, dailyScore, userId);
            System.out.println("构造的classmanagenew= "+classmanagenew);
            classmanageMapper.insertclassmanage(classmanagenew);
            Classmanage classmanage = classmanageMapper.queryClassmanageBystudentId(studentId);
           System.out.println("添加之后查询学生 classmanage="+classmanage);
           if(classmanage != null)
                return "添加学生成功";
            else
                return "添加学生失败";
        }

    }


/*
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
 */
}





