package com.alecrichter.textalarm;

import android.content.Context;
import android.util.Log;

import java.util.List;


public class TextChecker {

    TextChecker(String number, String message, Context context) {

        AlarmDatabase db = new AlarmDatabase(context, AlarmDatabase.databaseName, null, AlarmDatabase.databaseVersion);
        List<Alarm> alarmList = db.getAllAlarms();

        //Check number and message against each alarm in the database
        for(int i = 0; i < alarmList.size(); i++) {

            String type = alarmList.get(i).getType();
            String contentO = alarmList.get(i).getContentO();
            String contentF = alarmList.get(i).getContentF();
            String activated = alarmList.get(i).getActivated();

            if((((type.equals("phrase") && contentF.equals("not_case_sensitive")
                    && message.toLowerCase().contains(contentO.toLowerCase()))
                    || (type.equals("phrase") && message.contains(contentO))
                    || (type.equals("contact") && contentF.equals(number)))
                    && activated.equals("true"))) {

                //Set current alarm
                AlarmActivity.currentAlarm = alarmList.get(i);

                //Start alarm if match is found, break loop
                AlarmReceiver alarmReceiver = new AlarmReceiver();
                alarmReceiver.setAlarm(context);
                break;
            }
            else {
                //Log.d("TextChecker", "No match");
            }

        }
    }


}
