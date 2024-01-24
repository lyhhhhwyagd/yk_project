package com.exam.controller;

import com.exam.Entity.Classmanage;
import com.exam.service.ClassmanageService;
import com.exam.util.ParamsException;
import com.exam.util.ResultInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Classmanage")

public class ClassmanageController {

    private final ClassmanageService classmanageService;
    @Autowired
    public ClassmanageController(ClassmanageService classmanageService){
        this.classmanageService = classmanageService;
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/getclassmanage")//finished
    @ResponseBody
    public ResultInfo classmanage(@RequestParam("token") String token){
        System.out.println("userTask token="+token);
        ResultInfo resultInfo = new ResultInfo();
        String[] split = token.split("=");
        String tokenId="1";
        Integer id = Integer.parseInt(tokenId);
        try{
            List<Classmanage> classmanage = classmanageService.queryClassmanageByuserId(id);
            System.out.println("classmanage=" + classmanage);
            resultInfo.setResult(classmanage);
        }catch(ParamsException p){
            resultInfo.setCode(p.getCode());
            resultInfo.setMsg(p.getMsg());
            p.printStackTrace();
        }catch (Exception e){
            resultInfo.setCode(500);
            resultInfo.setMsg("查找失败");
        }
        return resultInfo;
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/getclassmanageBystudentName")//finished
    @ResponseBody
    public ResultInfo classmanageBystudentName(@RequestParam("studentName") String studentName){
        System.out.println("classmanageBystudentName studentName="+studentName);
        ResultInfo resultInfo = new ResultInfo();
        //String[] split = token.split("=");
        //String tokenId="1";
        //Integer id = Integer.parseInt(tokenId);
        try{
            List<Classmanage> classmanage = classmanageService.queryClassmanageBystudentName(studentName);
            System.out.println("classmanage=" + classmanage);
            resultInfo.setResult(classmanage);
        }catch(ParamsException p){
            resultInfo.setCode(p.getCode());
            resultInfo.setMsg(p.getMsg());
            p.printStackTrace();
        }catch (Exception e){
            resultInfo.setCode(500);
            resultInfo.setMsg("查找失败");
        }
        return resultInfo;
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/updateclassmanage")
    @ResponseBody
    public ResultInfo updateclassmanage(@RequestParam("token") String token, @RequestParam("classManageId") Integer classManageId,
                                        @RequestParam("studentId") String studentId, @RequestParam("studentName") String studentName,
                                        @RequestParam("attendanceRate") Double attendanceRate,
                                        @RequestParam("dailyScore") Integer dailyScore){
        System.out.println("updateclassmanage token"+token);
        System.out.println("updateclassmanage studentId"+studentId);
        System.out.println("updateclassmanage studentName"+studentName);

        ResultInfo resultInfo = new ResultInfo();
        //String[] split = token.split("=");
        //String tokenId=split[1];
        //Integer id = Integer.parseInt(tokenId);
        try{
            Classmanage classmanage= classmanageService.updateclassmanage(classManageId,studentId,studentName,attendanceRate,dailyScore);
            System.out.println("updateclassmanage classmanage="+classmanage);
            resultInfo.setResult(classmanage);
        }catch (ParamsException p){
            resultInfo.setCode(p.getCode());
            resultInfo.setMsg(p.getMsg());
            p.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
            resultInfo.setCode(500);
            resultInfo.setMsg("任务更新失败");
        }
        return  resultInfo;
    }
    @CrossOrigin(origins = "*")
    @PostMapping("/deleteStudent")
    @ResponseBody
    public ResultInfo deleteStudentclassmanage(@RequestParam("token") String token,
                                               @RequestParam("classManageId") Integer classManageId){
        System.out.println("deleteStudentclassmanage token="+token);
        ResultInfo resultInfo=new ResultInfo();
        String[] split = token.split("=");
        String tokenId=split[1];
        //Integer id = Integer.parseInt(tokenId);
        try{
            String result= classmanageService.deleteStudentclassmanage(classManageId);
            resultInfo.setResult(result);
        }catch (ParamsException p){
            resultInfo.setCode(p.getCode());
            resultInfo.setMsg(p.getMsg());
            p.printStackTrace();
        }catch (Exception e){
            resultInfo.setCode(500);
            resultInfo.setMsg("任务删除失败");
            e.printStackTrace();
        }
        return  resultInfo;
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/addclassmanage")
    @ResponseBody
    public ResultInfo insertclassmanage(@RequestParam("token") String token,
                                        @RequestParam("studentId") String studentId, @RequestParam("studentName") String studentName,
                                        @RequestParam("attendanceRate") Double attendanceRate,
                                        @RequestParam("dailyScore") Integer dailyScore,
                                        @RequestParam("userId") Integer userId){
        System.out.println("insertclassmanage token="+token);
        //System.out.println("insertclassmanage id="+id);
        System.out.println("insertclassmanage studentId="+studentId);
        System.out.println("insertclassmanage studentName="+studentName);
        System.out.println("insertclassmanage userId="+userId);
        ResultInfo resultInfo = new ResultInfo();
        //String[] split = token.split("=");
        //String tokenId=split[1];
        //Integer id = Integer.parseInt(tokenId);
        try{
            String result= classmanageService.insertclassmanage(studentId, studentName, attendanceRate, dailyScore, userId);
            System.out.println("insertclassmanage result="+result);
            resultInfo.setResult(result);
        }catch (ParamsException p){
            resultInfo.setCode(p.getCode());
            resultInfo.setMsg(p.getMsg());
            p.printStackTrace();
        }catch (Exception e){
            System.out.println("********************************************");
            e.printStackTrace();
            resultInfo.setCode(500);
            resultInfo.setMsg("任务添加失败");
        }
        return  resultInfo;
    }

    /*
    @CrossOrigin(origins = "*")
    @PostMapping("/ordertasks")//finished
    @ResponseBody
    public ResultInfo userOrderTask(@RequestParam("token") String token){
        System.out.println("userTask"+token);
        ResultInfo resultInfo = new ResultInfo();
        String[] split = token.split("=");
        String tokenId="1";
        Integer id = Integer.parseInt(tokenId);
        try{
            List<UserTask> userTask = taskService.userTodolistOrder(id,0);
           resultInfo.setResult(userTask);
        }catch(ParamsException p){
            resultInfo.setCode(p.getCode());
            resultInfo.setMsg(p.getMsg());
            p.printStackTrace();
        }catch (Exception e){
            resultInfo.setCode(500);
            resultInfo.setMsg("查找失败");
        }
        return resultInfo;
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/finished")//finished
    @ResponseBody
    public ResultInfo userFinished(@RequestParam("token") String token){
        System.out.println("userFinished"+token);
        ResultInfo resultInfo = new ResultInfo();
        String[] split = token.split("=");
        //String tokenId=split[1];
        String tokenId="1";
        Integer id = Integer.parseInt(tokenId);
        try{
            List<UserTask> userTask = taskService.queryUserTodolistById(id,1);
            resultInfo.setResult(userTask);
        }catch(ParamsException p){
            resultInfo.setCode(p.getCode());
            resultInfo.setMsg(p.getMsg());
            p.printStackTrace();
        }catch (Exception e){
            resultInfo.setCode(500);
            resultInfo.setMsg("查找失败");
        }
        return resultInfo;
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/orderfinished")//finished
    @ResponseBody
    public ResultInfo userOrderFinished(@RequestParam("token") String token){
        System.out.println("userFinished"+token);
        ResultInfo resultInfo = new ResultInfo();
        String[] split = token.split("=");
        //String tokenId=split[1];
        String tokenId="1";
        Integer id = Integer.parseInt(tokenId);
        try{
            List<UserTask> userTask = taskService.userTodolistFinishedOrder(id,1);
            resultInfo.setResult(userTask);
        }catch(ParamsException p){
            resultInfo.setCode(p.getCode());
            resultInfo.setMsg(p.getMsg());
            p.printStackTrace();
        }catch (Exception e){
            resultInfo.setCode(500);
            resultInfo.setMsg("查找失败");
        }
        return resultInfo;
    }


    @CrossOrigin(origins = "*")
    @PostMapping("/change") //用小写
    @ResponseBody
    public ResultInfo updateTaskTye(@RequestParam("token") String token
            ,@RequestParam("taskId") Integer taskId
            ,@RequestParam("taskType") Integer taskType
            ){
        System.out.println("updateTaskTye"+token);
        System.out.println("updateTaskTye taskType"+taskType);
        ResultInfo resultInfo = new ResultInfo();
        String[] split = token.split("=");
        String tokenId=split[1];
        Integer id = Integer.parseInt(tokenId);
       // System.out.println(taskId);
        try{
            UserTask userTask= taskService.updateTaskType(id,taskId,taskType);
            resultInfo.setResult(userTask);
        }catch (ParamsException p){
            resultInfo.setCode(p.getCode());
            resultInfo.setMsg(p.getMsg());
            p.printStackTrace();
        }catch (Exception e){
            resultInfo.setCode(500);
            resultInfo.setMsg("信息更新失败");
        }
        return  resultInfo;
    }


    @CrossOrigin(origins = "*")
    @PostMapping("/insert")
    @ResponseBody
    public ResultInfo insertTask(@RequestParam("token") String token,
                                 @RequestParam("taskContent") String taskContent,
                                 @RequestParam("taskDdl") String taskDdl
                                                                                    ){
        System.out.println("insertTask token="+token);
        System.out.println("insertTask taskContent="+taskContent);
        System.out.println("insertTask taskDdl="+taskDdl);

        ResultInfo resultInfo = new ResultInfo();
        String[] split = token.split("=");
        String tokenId=split[1];
        Integer id = Integer.parseInt(tokenId);
        try{
            List<UserTask> userTask= taskService.insertTask(id, taskContent, taskDdl);
            resultInfo.setResult(userTask);
        }catch (ParamsException p){
            resultInfo.setCode(p.getCode());
            resultInfo.setMsg(p.getMsg());
            p.printStackTrace();
        }catch (Exception e){
            resultInfo.setCode(500);
            resultInfo.setMsg("任务添加失败");
        }
        return  resultInfo;
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/update")
    @ResponseBody
    public  ResultInfo updateTask(@RequestParam("token") String token,
                                  @RequestParam("taskId") Integer taskId, @RequestParam("taskContent") String taskContent){
        System.out.println("updateTask"+token);
        ResultInfo resultInfo = new ResultInfo();
        String[] split = token.split("=");
        String tokenId=split[1];
        Integer id = Integer.parseInt(tokenId);
        try{
            UserTask userTask= taskService.updateTask(id,taskId,taskContent);
            resultInfo.setResult(userTask);
        }catch (ParamsException p){
            resultInfo.setCode(p.getCode());
            resultInfo.setMsg(p.getMsg());
            p.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
            resultInfo.setCode(500);
            resultInfo.setMsg("任务更新失败");
        }
        return  resultInfo;
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/delete")
    @ResponseBody
    public ResultInfo deleteTask(@RequestParam("token") String token,
                                 @RequestParam("taskId") Integer taskId){
        System.out.println("deleteTask"+token);
        ResultInfo resultInfo=new ResultInfo();
        String[] split = token.split("=");
        String tokenId=split[1];
        Integer id = Integer.parseInt(tokenId);
        try{
            String result= taskService.deleteTask(id,taskId);
            resultInfo.setResult(result);
        }catch (ParamsException p){
            resultInfo.setCode(p.getCode());
            resultInfo.setMsg(p.getMsg());
            p.printStackTrace();
        }catch (Exception e){
            resultInfo.setCode(500);
            resultInfo.setMsg("任务删除失败");
            e.printStackTrace();
        }
        return  resultInfo;
    }

     */

}
