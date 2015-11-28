package edu.chapman.cpsc370.asdplaydate;

import android.content.Context;
import android.content.Intent;

import com.parse.ParsePushBroadcastReceiver;


public class PushReceiver extends ParsePushBroadcastReceiver
{

    @Override
    protected void onPushReceive(Context context, Intent intent)
    {
        super.onPushReceive(context, intent);

        // TODO: Make notifications "silent" if the correct chat window is open (and update the chat with the new message)
    }
}
