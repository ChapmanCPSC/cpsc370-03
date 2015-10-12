
package com.example.cpsc.demoapplication;

import com.example.cpsc.demoapplication.models.ForecastResultModel;
import com.example.cpsc.demoapplication.models.WeatherResultModel;
import com.google.gson.Gson;

/**
 * Created by cpsc on 9/9/15.
 */
public class WeatherAPIWrapper
{
    private static String BASE_URL = "http://api.openweathermap.org/data/2.5/";
    private static final String API_KEY = "1a917c08876fba7a288b0989bcaf6d50";

    public static WeatherResultModel GetCurrentWeather(String zip, String unitType)
    {
        String method = "weather";
        String queryString = "?APPID="+API_KEY+"&zip="+zip+"&units="+unitType;
        String fullUrl = BASE_URL +method+queryString;

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
        String queryString = "?APPID="+API_KEY+"&zip="+zip+"&cnt=10";
        String fullUrl = BASE_URL +method+queryString;

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
