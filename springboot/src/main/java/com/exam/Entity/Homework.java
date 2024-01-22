package com.exam.Entity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.sql.Timestamp;

import javax.persistence.Column;


@Data

@TableName("homework")
public class Homework {
    @TableId(type = IdType.AUTO)
     @Column(name = "id")
    private Long id;
    @Column(name = "title")
    private String title;
    @Column(name = "description")
    private String description;
    @Column(name = "deadline")
    private Timestamp deadline;
    @Column(name = "teacher")
    private String teacher;
    @Column(name = "if_submit")
    private Boolean ifSubmit;
    // 其他属性...
}
