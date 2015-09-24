package com.example.cpsc.demoapplication.tasks;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;

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
        SharedPreferences defaultPrefs = PreferenceManager.getDefaultSharedPreferences(_fragment.getActivity());
        String units = defaultPrefs.getString("temp_units", "imperial");

        WeatherResultModel model = WeatherAPIWrapper.GetCurrentWeather(params[0], units);
        return model;
    }

    @Override
    protected void onPostExecute(WeatherResultModel weatherResultModel)
    {
        super.onPostExecute(weatherResultModel);
        _fragment.loadingFinished(weatherResultModel);
    }
}
