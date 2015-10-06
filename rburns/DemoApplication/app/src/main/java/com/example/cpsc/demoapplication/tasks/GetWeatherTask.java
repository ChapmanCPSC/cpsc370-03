package com.example.cpsc.demoapplication.tasks;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;

import com.example.cpsc.demoapplication.WeatherAPIWrapper;
import com.example.cpsc.demoapplication.fragments.WeatherFragment;
import com.example.cpsc.demoapplication.models.WeatherResultModel;

/**
 * Created by ryanb on 9/21/2015.
 */
public class GetWeatherTask extends AsyncTask<String, Void, Void>
{
    Context _ctx;
    public Runnable onFinish;
    public WeatherResultModel _result;

    public GetWeatherTask(Context ctx)
    {
        _ctx = ctx;
    }

    @Override
    protected Void doInBackground(String... params)
    {
        SharedPreferences defaultPrefs = PreferenceManager.getDefaultSharedPreferences(_ctx);
        String units = defaultPrefs.getString("temp_units", "imperial");

        _result = WeatherAPIWrapper.GetCurrentWeather(params[0], units);
        return null;
    }

    @Override
    protected void onPostExecute(Void params)
    {
        super.onPostExecute(params);
        if (onFinish!=null)
        {
            onFinish.run();
        }
    }
}
