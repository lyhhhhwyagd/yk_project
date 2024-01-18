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
@ApiModel(value="Ans对象", description="")
public class Ans implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId(value ="id", type = IdType.AUTO)
    @Alias("编号")
    private  Integer id;
    @ApiModelProperty(value="myans")
    @Alias("答案")
    private String myans;
    @ApiModelProperty(value="userid")
    @Alias("用户id")
    private String userid;
    @ApiModelProperty(value="problemid")
    @Alias("题目编号")
    private Integer problemid;




}
