package com.exam.Entity;
import lombok.Data;

import java.util.Date;

@Data
public class UserTask {
   private Integer taskId;
   private  Integer taskUserId;
   private Integer taskType;  // 加急任务 taskType = 2
   private String taskContent;
   private String taskDdl;
   private Date taskCreateTime;
   private Date taskFinishTime;

}
