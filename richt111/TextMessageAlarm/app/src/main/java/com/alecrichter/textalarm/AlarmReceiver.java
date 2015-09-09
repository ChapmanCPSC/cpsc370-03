package com.alecrichter.textalarm;

import android.app.AlarmManager;
import android.app.KeyguardManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.PowerManager;
import android.preference.PreferenceManager;
import android.util.Log;

public class AlarmReceiver extends BroadcastReceiver {

    static boolean alarmActive;

	@Override
	public void onReceive(Context context, Intent intent) {
		
		PowerManager power = (PowerManager) context.getSystemService(Context.POWER_SERVICE);

        //Check if locked
        KeyguardManager kgMgr = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
        boolean locked = kgMgr.inKeyguardRestrictedInputMode();

        //Check prefs for lock screen only
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        boolean lockScreenOnly = prefs.getBoolean("pref_lock_screen_only", true);

        //Only start alarm activity if screen is locked or setting is disabled
        if((!lockScreenOnly || (lockScreenOnly && locked)) && !alarmActive) {

            alarmActive = true;

            //Get lock
            PowerManager.WakeLock wake = power.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "TextMessageAlarm");
            wake.acquire();

            Intent alarmIntent = new Intent(context, AlarmActivity.class);
            alarmIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(alarmIntent);

            //Release lock
            wake.release();
        }

	}
	
	public void setAlarm(Context context) {
		
		AlarmManager alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		Intent intent = new Intent(context, AlarmReceiver.class);
		PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, 0);
		alarm.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), pi);
	}

}
