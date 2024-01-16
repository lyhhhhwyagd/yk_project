package com.exam.service;
import com.exam.Entity.Weather;

import java.util.List;

public interface WeatherService {

    List<Weather> selectAll();

    List<Weather> selectByCity(String City);

    List<Weather> selectByCityAndDate(String City,String Data);
}
