package com.exam.serviceImplementation;

import com.exam.Entity.Weather;
import com.exam.mapper.WeatherMapper;
import com.exam.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WeatherServiceImplementation implements WeatherService{

    private final WeatherMapper weatherMapper;
    @Autowired
    public WeatherServiceImplementation(WeatherMapper weatherMapper){
        this.weatherMapper = weatherMapper;
    }

    public List<Weather> selectAll(){
        System.out.println("正在执行查询");
        return weatherMapper.selectAll();
    }

    public List<Weather> selectByCity(String City){
        return weatherMapper.selectByCity(City);
    }

    public List<Weather> selectByCityAndDate(String City,String Date){
        return weatherMapper.selectByCityAndDate(City,Date);
    }
}
