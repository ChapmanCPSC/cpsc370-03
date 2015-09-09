package com.alecrichter.textalarm;

import android.app.Activity;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;

public class AlarmTonePicker extends Activity {

    private Alarm alarm;
    private MainActivity activity;

    AlarmTonePicker(Alarm alarm, MainActivity activity) {
        this.alarm = alarm;
        this.activity = activity;
    }

    public void pickAlarmTone() {

        activity.setCurrentSelection(alarm);

        String currentTone = alarm.getUri();
        Uri currentUri;

        //Set current alarm to chosen tone or to default
        if(currentTone != null) {
            currentUri = Uri.parse(currentTone);
        }
        else {
            currentUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        }

        Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_ALARM);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "Select alarm tone");
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_SILENT, false);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT, false);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, currentUri);
        activity.startActivityForResult(intent, 2);
    }

}
