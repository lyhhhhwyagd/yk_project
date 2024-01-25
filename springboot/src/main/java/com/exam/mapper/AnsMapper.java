package com.exam.mapper;

import com.exam.Entity.Ans;
import com.exam.Entity.Anse;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Service;

import java.util.List;


@Mapper
public interface AnsMapper {
    //    where problemName like concat('%',#{problemName},'%')  and teacherId like concat('%',#{teacherId},'%')
    @Select("select * from xm04_problem where problemname like concat('%',#{problemname},'%')  and teacherid like concat('%',#{teacherid},'%')" +
            "order by id desc")
    List<Ans> selectAll(Ans problem);

    @Insert("insert into xm04_ans (myans,userid,problemid) values (#{myans},#{userid},#{problemid})")
    void insert(Ans problem);
    //
//    IPage pageCC(IPage<Ans> page,@Param(Constants.WRAPPER ) Wrapper wrapper);
    @Update("update xm04_problem set problemname=#{problemname},problemdes=#{problemdes},teacherid=#{teacherid},a=#{a},b=#{b},c=#{c},d=#{d},ans=#{ans},time=#{time} where id=#{id}")
    void updateById(Ans problem);


    @Select("select * from xm04_ans where userid=#{userid} and problemid=#{problemid}")
    Ans selectByCondition(Ans ans);

    @Select("select * from xm04_ans")
    List<Ans> listAll();

    @Select("select problem.id,problemdes,problemname,a,b,c,d,ans,teacherid,time,myans,userid from xm04_ans,xm04_problem where ans.problemid=problem.id and ans.userid=#{userid}")
    List<Anse> selects(Anse anse);
}
