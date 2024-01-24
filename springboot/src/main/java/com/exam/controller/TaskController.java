package com.exam.controller;

import com.exam.Entity.UserTask;
import com.exam.service.TaskService;
import com.exam.util.ParamsException;
import com.exam.util.ResultInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/zc/api")

public class TaskController {

    private final TaskService taskService;
    @Autowired
    public TaskController(TaskService taskService){
        this.taskService = taskService;
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/tasks")//finished
    @ResponseBody
    public ResultInfo userTask(@RequestParam("token") String token,
                               @RequestParam("UserID") Integer UserID){
        System.out.println("userTask token="+token);
        ResultInfo resultInfo = new ResultInfo();
        String[] split = token.split("=");
        String tokenId="1";
        Integer id = Integer.parseInt(tokenId);
        try{
            List<UserTask> userTask = taskService.queryUserTodolistById(UserID, 0);
            System.out.println("userTask=" + userTask);
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
    @PostMapping("/selectDdl")//finished
    @ResponseBody
    public ResultInfo userTaskDdl(@RequestParam("token") String token,
                                  @RequestParam("taskDdl") String taskDdl,
                                  @RequestParam("UserID") Integer UserID){
        System.out.println("userTaskDdl token="+token);
        System.out.println("userTaskDdl taskDdl="+taskDdl);
        ResultInfo resultInfo = new ResultInfo();
        String[] split = token.split("=");
        String tokenId="1";
        Integer id = Integer.parseInt(tokenId);
        try{
            List<UserTask> userTask = taskService.queryUserTodolistByDdl(taskDdl, 0,UserID);
            System.out.println("userTask=" + userTask);
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
    @PostMapping("/ordertasks")//finished
    @ResponseBody
    public ResultInfo userOrderTask(@RequestParam("token") String token,
                                    @RequestParam("UserID") Integer UserID){
        System.out.println("userTask"+token);
        ResultInfo resultInfo = new ResultInfo();
        String[] split = token.split("=");
        String tokenId="1";
        Integer id = Integer.parseInt(tokenId);
        try{
            List<UserTask> userTask = taskService.userTodolistOrder(UserID,0);
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
    public ResultInfo userFinished(@RequestParam("token") String token,
                                   @RequestParam("UserID") Integer UserID){
        System.out.println("userFinished"+token);
        ResultInfo resultInfo = new ResultInfo();
        String[] split = token.split("=");
        //String tokenId=split[1];
        String tokenId="1";
        Integer id = Integer.parseInt(tokenId);
        try{
            List<UserTask> userTask = taskService.queryUserTodolistById(UserID,1);
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
    public ResultInfo userOrderFinished(@RequestParam("token") String token,
                                        @RequestParam("UserID") Integer UserID){
        System.out.println("userFinished"+token);
        ResultInfo resultInfo = new ResultInfo();
        String[] split = token.split("=");
        //String tokenId=split[1];
        String tokenId="1";
        Integer id = Integer.parseInt(tokenId);
        try{
            List<UserTask> userTask = taskService.userTodolistFinishedOrder(UserID,1);
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
            UserTask userTask= taskService.updateTaskType(taskId,taskType);
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
                                 @RequestParam("taskDdl") String taskDdl,
                                 @RequestParam("UserID") Integer UserID
    ){
        System.out.println("insertTask token="+token);
        System.out.println("insertTask taskContent="+taskContent);
        System.out.println("insertTask taskDdl="+taskDdl);

        ResultInfo resultInfo = new ResultInfo();
        String[] split = token.split("=");
        String tokenId=split[1];
        Integer id = Integer.parseInt(tokenId);
        try{
            List<UserTask> userTask= taskService.insertTask(UserID, taskContent, taskDdl);
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
            UserTask userTask= taskService.updateTask(taskId,taskContent);
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
            String result= taskService.deleteTask(taskId);
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

}
