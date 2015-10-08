package com.example.cpsc.demoapplication.models;

/**
 * Created by ryanb on 10/7/2015.
 */
public class ForecastResultModel
{
    public City city;
    public ForecastItem[] list;

    public class City
    {
        public String name;
    }

    public class ForecastItem
    {
        public long dt;
        public WeatherItem[] weather;
    }

    public class WeatherItem
    {
        public String main;
    }
}
