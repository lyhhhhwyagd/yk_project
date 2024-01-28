package com.exam.service;
import com.exam.Entity.Weather;

import java.util.List;

public interface WeatherService {

    List<Weather> selectAll();

    List<Weather> selectByCity(String City);

    Weather selectByWeatherID(String id);

    int modifyByWeatherID(String id, Weather weather);

    List<Weather> selectByCityAndDate(String City,String Data);

    int deleteById(String id);
}
