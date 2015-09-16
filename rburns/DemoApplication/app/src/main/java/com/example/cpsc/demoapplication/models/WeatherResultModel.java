package com.example.cpsc.demoapplication.models;

/**
 * Created by cpsc on 9/16/15.
 */
public class WeatherResultModel
{
    public String name;
    public WeatherMainModel main;

    public class WeatherMainModel
    {
        public float temp;
        public float temp_min;
        public float temp_max;
    }
}
