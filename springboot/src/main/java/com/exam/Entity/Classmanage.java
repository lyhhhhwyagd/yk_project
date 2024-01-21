package com.exam.Entity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;


@Data
public class Classmanage {
    @TableId(value = "classmanageId",type = IdType.AUTO)
    private Integer classManageId;
    private String studentId;
    private String studentName;
    private Double attendanceRate;
    private Integer dailyScore;
    private Integer userId;
}
