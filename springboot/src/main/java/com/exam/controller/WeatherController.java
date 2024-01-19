package com.exam.controller;


import com.exam.Entity.Post;
import com.exam.Entity.ApiResult;
import com.exam.service.WeatherService;
import com.exam.util.ApiResultHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class WeatherController {

    private final WeatherService weatherService;
    @Autowired
    public WeatherController(WeatherService weatherService){
        this.weatherService = weatherService;
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/weather")
    public ApiResult selectAll(){
        System.out.println("查询全部天气数据");
        return ApiResultHandler.success(weatherService.selectAll());
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/weather_city/{City}")
    public ApiResult selectByCity(@PathVariable("City") String City){
        System.out.println("根据城市查询天气");
        return ApiResultHandler.success(weatherService.selectByCity(City));
    }
    @CrossOrigin(origins = "*")
    @GetMapping("/weather_city_date/{City,Date}")
    public ApiResult selectByCityAndDate(@PathVariable("City") String City,String Date){
        System.out.println("根据城市和日期查询天气");
        return ApiResultHandler.success(weatherService.selectByCityAndDate(City,Date));
    }

    @CrossOrigin(origins = "*")
    @DeleteMapping("/weather/{id}")
    public ApiResult deleteById(@PathVariable("id") String id){
        System.out.println("删除天气信息");
        return ApiResultHandler.success(weatherService.deleteById(id));
    }
}
