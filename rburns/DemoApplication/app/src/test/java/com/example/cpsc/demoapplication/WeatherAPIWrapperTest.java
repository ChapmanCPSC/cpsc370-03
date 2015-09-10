package com.example.cpsc.demoapplication;

import junit.framework.TestCase;

/**
 * Created by cpsc on 9/9/15.
 */
public class WeatherAPIWrapperTest extends TestCase
{
    public void testGetCurrentWeather() throws Exception
    {
        System.out.println(WeatherAPIWrapper.GetCurrentWeather("92866"));
    }
}