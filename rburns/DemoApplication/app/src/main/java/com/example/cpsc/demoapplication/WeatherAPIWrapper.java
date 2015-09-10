package com.example.cpsc.demoapplication;

/**
 * Created by cpsc on 9/9/15.
 */
public class WeatherAPIWrapper
{
    private static String baseUrl = "http://api.openweathermap.org/data/2.5/";

    public static String GetCurrentWeather(String zip)
    {
        String method = "weather";
        String queryString = "?zip="+zip;
        String fullUrl = baseUrl+method+queryString;

        String response="";
        try
        {
            response = new WebRequest(fullUrl).get();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return response;
    }
}
