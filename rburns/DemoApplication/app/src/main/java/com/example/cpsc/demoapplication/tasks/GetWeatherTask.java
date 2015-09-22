package com.example.cpsc.demoapplication.tasks;

import android.os.AsyncTask;

import com.example.cpsc.demoapplication.WeatherAPIWrapper;
import com.example.cpsc.demoapplication.fragments.WeatherFragment;
import com.example.cpsc.demoapplication.models.WeatherResultModel;

/**
 * Created by ryanb on 9/21/2015.
 */
public class GetWeatherTask extends AsyncTask<String, Void, WeatherResultModel>
{
    WeatherFragment _fragment;

    public GetWeatherTask(WeatherFragment fragment)
    {
        _fragment = fragment;
    }

    @Override
    protected void onPreExecute()
    {
        super.onPreExecute();
        _fragment.loadingStarted();
    }

    @Override
    protected WeatherResultModel doInBackground(String... params)
    {
        WeatherResultModel model = WeatherAPIWrapper.GetCurrentWeather(params[0]);
        return model;
    }

    @Override
    protected void onPostExecute(WeatherResultModel weatherResultModel)
    {
        super.onPostExecute(weatherResultModel);
        _fragment.loadingFinished(weatherResultModel);
    }
}
