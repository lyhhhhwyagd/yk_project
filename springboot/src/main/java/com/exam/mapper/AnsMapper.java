package com.exam.mapper;

import com.exam.Entity.Ans;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;


@Mapper
public interface AnsMapper {
    //    where problemName like concat('%',#{problemName},'%')  and teacherId like concat('%',#{teacherId},'%')
    @Select("select * from problem where problemname like concat('%',#{problemname},'%')  and teacherid like concat('%',#{teacherid},'%')" +
            "order by id desc")
    List<Ans> selectAll(Ans problem);

    @Insert("insert into ans (myans,userid,problemid) values (#{myans},#{userid},#{problemid})")
    void insert(Ans problem);
    //
//    IPage pageCC(IPage<Ans> page,@Param(Constants.WRAPPER ) Wrapper wrapper);
    @Update("update problem set problemname=#{problemname},problemdes=#{problemdes},teacherid=#{teacherid},a=#{a},b=#{b},c=#{c},d=#{d},ans=#{ans},time=#{time} where id=#{id}")
    void updateById(Ans problem);


    @Select("select * from ans where userid=#{userid} and problemid=#{problemid}")
    Ans selectByCondition(Ans ans);

    @Select("select * from ans")
    List<Ans> listAll();
}
