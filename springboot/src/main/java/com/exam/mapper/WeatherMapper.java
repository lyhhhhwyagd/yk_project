package com.exam.mapper;

import com.exam.Entity.Weather;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface WeatherMapper {
    //update/insert/delete默认返回语句影响的行数的一个整数。
    @Select("select * from weather")
    List<Weather> selectAll();

    @Select("SELECT * FROM weather WHERE City = #{City}")
    List<Weather> selectByCity(@Param("City") String City);

    @Select("SELECT * FROM weather WHERE City = #{City} AND SUBSTRING(obsTime, 1, 10) = #{Date}")
    List<Weather> selectByCityAndDate(@Param("City") String City, @Param("Data") String Date);

    @Delete("delete from weather where id = #{id}")
    int deleteById(String id);
}
