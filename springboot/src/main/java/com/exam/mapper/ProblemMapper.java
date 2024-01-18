package com.exam.mapper;

import com.exam.Entity.Problem;
import org.apache.ibatis.annotations.*;

import java.util.List;


@Mapper
public interface ProblemMapper {
//    where problemName like concat('%',#{problemName},'%')  and teacherId like concat('%',#{teacherId},'%')
    @Select("select * from problem where problemname like concat('%',#{problemname},'%')  and teacherid like concat('%',#{teacherid},'%')" +
            "order by id desc")
    List<Problem> selectAll(Problem problem);

    @Insert("insert into problem (problemname,problemdes,teacherid,a,b,c,d,ans,time) values (#{problemname},#{problemdes},#{teacherid},#{a},#{b},#{c},#{d},#{ans},#{time})")
    void insert(Problem problem);
//
//    IPage pageCC(IPage<Problem> page,@Param(Constants.WRAPPER ) Wrapper wrapper);
    @Update("update problem set problemname=#{problemname},problemdes=#{problemdes},teacherid=#{teacherid},a=#{a},b=#{b},c=#{c},d=#{d},ans=#{ans},time=#{time} where id=#{id}")
    void updateById(Problem problem);

    @Delete("delete  from  problem where id=#{id}")
    void deleteById(Integer id);

    @Select("select * from problem ")
    List<Problem> listAll();
}
