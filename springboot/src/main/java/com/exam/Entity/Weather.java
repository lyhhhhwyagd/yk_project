package com.exam.Entity;

import lombok.Getter;

@Getter
public class Weather {
    private String id;
    private String City;
    private String obsTime;
    private String Temperature;
    private String Humidity;
    private String WindSpeed;
    private String WindDirection;
    private String WeatherCondition;
    private String Pressure;
    private String Visibility;

    public void setID(String id) {
        this.id = id;
    }
    public void setCity(String City) {
        this.City = City;
    }
    public void setObsTime(String obsTime) {
        this.obsTime = obsTime;
    }
    public void setTemperature(String Temperature) {
        this.Temperature = Temperature;
    }
    public void setHumidity(String Humidity) {
        this.Humidity = Humidity;
    }
    public void setWindSpeed(String WindSpeed) {
        this.WindSpeed = WindSpeed;
    }
    public void setWindDirection(String WindDirection) {
        this.WindDirection = WindDirection;
    }
    public void setWeatherCondition(String WeatherCondition) {
        this.WeatherCondition = WeatherCondition;
    }
    public void setPressure(String Pressure) {
        this.Pressure = Pressure;
    }
    public void setVisibility(String Visibility) {
        this.Visibility = Visibility;
    }
}
