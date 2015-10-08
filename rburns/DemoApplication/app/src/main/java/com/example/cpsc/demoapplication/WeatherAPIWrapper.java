
package com.example.cpsc.demoapplication;

import com.example.cpsc.demoapplication.models.ForecastResultModel;
import com.example.cpsc.demoapplication.models.WeatherResultModel;
import com.google.gson.Gson;

/**
 * Created by cpsc on 9/9/15.
 */
public class WeatherAPIWrapper
{
    private static String baseUrl = "http://api.openweathermap.org/data/2.5/";

    public static WeatherResultModel GetCurrentWeather(String zip, String unitType)
    {
        String method = "weather";
        String queryString = "?zip="+zip+"&units="+unitType;
        String fullUrl = baseUrl+method+queryString;

        String response="";
        try
        {
            response = new WebRequest(fullUrl).get();
            WeatherResultModel model = new Gson().fromJson(response, WeatherResultModel.class);
            return model;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public static ForecastResultModel GetForecast(String zip)
    {
        String method = "forecast/daily";
        String queryString = "?zip="+zip+"&cnt=10";
        String fullUrl = baseUrl+method+queryString;

        String response="";
        try
        {
            response = new WebRequest(fullUrl).get();
            ForecastResultModel model = new Gson().fromJson(response, ForecastResultModel.class);
            return model;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }
}
