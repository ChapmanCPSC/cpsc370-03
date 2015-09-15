package com.example.cpsc.demoapplication;

import com.example.cpsc.demoapplication.models.WeatherResultModel;

import junit.framework.TestCase;

/**
 * Created by cpsc on 9/9/15.
 */
public class WeatherAPIWrapperTest extends TestCase
{
    public void testGetCurrentWeather() throws Exception
    {
        WeatherResultModel model = WeatherAPIWrapper.GetCurrentWeather("92866");
        System.out.println(String.format("Temp:%s\nHi:%s\nLow:%s\nArea:%s", model.main.temp, model.main.temp_max, model.main.temp_min, model.name));
    }
}