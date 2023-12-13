package com.exam.Entity;

import cn.hutool.core.annotation.Alias;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author jobob
 * @since 2023-12-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="Course对象", description="")
public class Course implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value ="id", type = IdType.AUTO)
    @Alias("编号")
    private  Integer id;
    @ApiModelProperty(value="courseId")
    @Alias("课程号")
    private String courseId;
    @ApiModelProperty(value="courseName")
    @Alias("课程名")
    private String courseName;
    @ApiModelProperty(value="teacherId")
    @Alias("教师id")
    private String teacherId;
    @Alias("学分")
    private Integer bonus;
    @Alias("学院")
    private String dep;
    @Alias("班级号")
    private Integer classId;





}
