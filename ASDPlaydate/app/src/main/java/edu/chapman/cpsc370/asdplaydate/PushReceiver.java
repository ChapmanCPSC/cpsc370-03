package edu.chapman.cpsc370.asdplaydate;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.parse.ParsePushBroadcastReceiver;

import org.joda.time.DateTime;
import org.json.JSONObject;

import java.util.List;

import edu.chapman.cpsc370.asdplaydate.activities.ChatActivity;
import edu.chapman.cpsc370.asdplaydate.activities.SettingsActivity;
import edu.chapman.cpsc370.asdplaydate.models.Message;


public class PushReceiver extends ParsePushBroadcastReceiver
{

    private static final String TAG = "PushDebug";

    @Override
    protected void onPushOpen(Context context, Intent intent)
    {
        Bundle bundle = intent.getExtras();
        for(String key : bundle.keySet())
        {
            Object value = bundle.get(key);
            Log.d(TAG, String.format("%s %s (%s)", key, value.toString(), value.getClass().getName()));
        }
        Intent i = new Intent(context, ChatActivity.class);
        i.putExtras(intent.getExtras());
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }


    @Override
    protected void onPushReceive(Context context, Intent intent)
    {
        //super.onPushReceive(context, intent);

        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(Integer.MAX_VALUE);
        Log.d("topActivity", "CURRENT Activity ::" + tasks.get(0).topActivity.getClassName());
        boolean isRunning = false;
        for(ActivityManager.RunningTaskInfo task : tasks)
        {
            if(task.topActivity.getClassName().equalsIgnoreCase("edu.chapman.cpsc370.asdplaydate.activities.ChatActivity"))
            {
                Log.d(TAG, "true" + task.topActivity.getClassName());
                isRunning = true;
            }
        }
        if(!isRunning)
        {
            super.onPushReceive(context,intent);
        }
        else
        {
            Bundle bundle = intent.getExtras();
            for(String key : bundle.keySet())
            {
                Object value = bundle.get(key);
                Log.d(TAG, String.format("%s %s (%s)", key, value.toString(), value.getClass().getName()));
            }
            Object data = bundle.get("com.parse.Data");
            String sData = data.toString();
            try
            {
                JSONObject main = new JSONObject(sData);
                //JSONObject messageObj = main.getJSONObject("alert");
                //Log.d(TAG + "1",messageObj.getString("alert").toString());
                Log.d(TAG + "1", main.getString("alert").toString());
                sData = main.getString("alert").toString();
            }catch(Exception e)
            {
                e.printStackTrace();
            }

            for(int i = 12; i < sData.length(); i++)
            {
                if(sData.charAt(i) == ':')
                {
                    sData = sData.substring(i+1);
                    break;
                }
            }
            Log.d(TAG, sData);
            Message newMessage = new Message();
            newMessage.setText(sData);
            newMessage.setTimestamp(DateTime.now());
            ChatActivity.chatActivity.displayMessage(newMessage);
        }
    }

}
