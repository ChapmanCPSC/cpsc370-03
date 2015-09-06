package com.alecrichter.textalarm;

import android.app.Activity;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import java.util.Timer;
import java.util.TimerTask;


public class AlarmActivity extends Activity {

    public static Alarm currentAlarm;
    private MediaPlayer alarmPlayer;
    private Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Wake up window from lock
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON|
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD|
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED|
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        setContentView(R.layout.activity_alarm);

        //Button listener to stop alarm
        Button alarmBtn = (Button) findViewById(R.id.alarm_button);
        alarmBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                stopAlarm();
            }
        });

        //Start alarm
        playAlarm();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        View decorView = getWindow().getDecorView();
        if (hasFocus) {
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);}
    }

    public void stopAlarm() {

        //Stop timer thread
        timer.cancel();

        //Turn alarm off
        alarmPlayer.stop();
        AlarmReceiver.alarmActive = false;

        if(Build.VERSION.SDK_INT < 21)
            finish();
        else
            finishAndRemoveTask();
    }

    private void playAlarm() {

        Uri alert;
        String alarmTone = currentAlarm.getUri();

        //If no alarm has been chosen, get default alarm or ringtone
        if(alarmTone != null) {
            alert = Uri.parse(alarmTone);
        }
        else if(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM) != null) {
            alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        }
        else {
            alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
        }

        //Initialize player
        alarmPlayer = new MediaPlayer();
        alarmPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);

        //Get volume setting
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        float volume = (float) prefs.getInt("pref_alarm_volume", 75) / 100;
        alarmPlayer.setVolume(volume, volume);

        try {
            alarmPlayer.setDataSource(this, alert);
            alarmPlayer.setLooping(true);
            alarmPlayer.prepare();
        } catch (Exception e) {
            e.printStackTrace();
        }

        checkAlarmDuration(prefs);
        alarmPlayer.start();
    }

    private void checkAlarmDuration(SharedPreferences prefs) {

        //Get alarm duration setting
        final int duration = prefs.getInt("pref_alarm_duration", 0);

        timer = new Timer();

        //If not set to unlimited, turn alarm off after the given duration
        if(duration != 0) {

            //Increase counter every minute until duration is reached
            timer.scheduleAtFixedRate(new TimerTask() {
                int counter = -1;
                @Override
                public void run() {
                    counter++;
                    if(counter >= (duration))
                        stopAlarm();
                }
            }, 1000, 60000);

        }
    }

}
