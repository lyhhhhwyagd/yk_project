package com.exam.Entity;

import cn.hutool.core.annotation.Alias;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="Anse对象", description="")
public class Anse implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId(value ="id", type = IdType.AUTO)
    @Alias("编号")
    private  Integer id;
    @ApiModelProperty(value="problemdes")
    @Alias("问题描述")
    private String problemdes;
    @ApiModelProperty(value="problemname")
    @Alias("题目")
    private String problemname;
    @ApiModelProperty(value="a")
    @Alias("A选项")
    private String a;
    @Alias("B选项")
    private String b;
    @Alias("C选项")
    private String c;
    @Alias("D选项")
    private String d;
    @Alias("答案")
    private String ans;
    @Alias("出题人")
    private String teacherid;
    @Alias("发布时间")
    private String time;
    @Alias("答案")
    private String myans;

    @Alias("用户id")
    private String userid;



}
