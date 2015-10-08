package com.example.cpsc.demoapplication.services;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;

import com.example.cpsc.demoapplication.WeatherAPIWrapper;
import com.example.cpsc.demoapplication.models.ForecastResultModel;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

/**
 * Created by ryanb on 10/7/2015.
 */
public class RainCheckService extends IntentService
{

    private static final int INTERVAL = 60000;

    public RainCheckService()
    {
        super("RainCheckService");
    }

    @Override
    protected void onHandleIntent(Intent intent)
    {
        Log.i("TEST", "Service Run");

        ForecastResultModel forecast = WeatherAPIWrapper.GetForecast("92866");

        for (ForecastResultModel.ForecastItem item : forecast.list)
        {
            for (ForecastResultModel.WeatherItem weather : item.weather)
            {
                if (weather.main.equals("Rain"))
                {
                    showRainNofification(item, forecast.city.name);
                    return;
                }
            }
        }
    }

    public static void StartChecker(Context ctx)
    {
        PendingIntent pi = PendingIntent.getService(ctx,0,new Intent(ctx, RainCheckService.class),0);

        AlarmManager am = (AlarmManager) ctx.getSystemService(Context.ALARM_SERVICE);
        am.setInexactRepeating(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime(),INTERVAL,pi);
    }

    private void showRainNofification(ForecastResultModel.ForecastItem item, String name)
    {
        NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        DateTime date = new DateTime(item.dt*1000);
        String display = new LocalDate().withDayOfWeek(date.getDayOfWeek()).dayOfWeek().getAsText();

        Notification.Builder notif = new Notification.Builder(this)
                .setSmallIcon(android.R.drawable.ic_menu_compass)
                .setContentTitle("It's going to rain")
                .setContentText(String.format("on %s in %s", display, name));

        nm.notify(1, notif.build());
    }


}
