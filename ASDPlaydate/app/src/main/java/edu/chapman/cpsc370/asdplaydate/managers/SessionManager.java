package edu.chapman.cpsc370.asdplaydate.managers;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Kelly on 11/19/15.
 */
public class SessionManager
{

    SharedPreferences prefs;
    Context mContext;
    SharedPreferences.Editor editor;

    private static final String PREFERENCES_NAME = "ASDPreferences";
    public static final String KEY_SESSSION_TOKEN = "session_token";
    public static final String KEY_SEARCH_RADIUS =  "search_radius";
    public static final String KEY_BROADCAST_DURATION = "broadcast_duration";
    public static final String KEY_BROADCAST_MESSAGE = "broadcast_message";
    public static final String KEY_PROMPT_BROADCAST = "prompt_broadcast";
    public static final String KEY_FROM_DIALOG = "from_dialog";

    public SessionManager(Context context)
    {
        mContext = context;
        prefs = mContext.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        editor = prefs.edit();
    }

    public void storeSessionToken(String sessionToken)
    {
        editor.putString(KEY_SESSSION_TOKEN, sessionToken);
        editor.commit();
    }

    public String getSessionToken()
    {
        String sessionToken = prefs.getString(KEY_SESSSION_TOKEN, null);
        return sessionToken;
    }

    public void storeSearchRadius(int searchRadius)
    {
        editor.putInt(KEY_SEARCH_RADIUS, searchRadius);
        editor.commit();
    }

    public int getSearchRadius()
    {
        int searchRadius = prefs.getInt(KEY_SEARCH_RADIUS, 2);
        return searchRadius;
    }

    public void storeBroadcastDuration(int duration)
    {
        editor.putInt(KEY_BROADCAST_DURATION, duration);
        editor.commit();
    }

    public int getBroadcastDuration()
    {
        int broadcastDuration = prefs.getInt(KEY_BROADCAST_DURATION, 59);
        return broadcastDuration;
    }

    public void storeBroadcastMessage(String message)
    {
        editor.putString(KEY_BROADCAST_MESSAGE, message);
        editor.commit();
    }

    public String getBroadcastMessage()
    {
        String message = prefs.getString(KEY_BROADCAST_MESSAGE, "");
        return message;
    }

    public void storeFromDialog(boolean value)
    {
        editor.putBoolean(KEY_FROM_DIALOG, value);
        editor.commit();
    }

    public boolean getFromDialog()
    {
        boolean value = prefs.getBoolean(KEY_FROM_DIALOG, false);
        return value;
    }

    public void storePromptBroadcast(boolean prompt)
    {
        editor.putBoolean(KEY_PROMPT_BROADCAST, prompt);
        editor.commit();
    }

    public boolean getPromptBroadcast()
    {
        boolean prompt;
        if(getFromDialog())
        {
            prompt = !prefs.getBoolean(KEY_PROMPT_BROADCAST,false);
        }
        else
        {
            prompt = prefs.getBoolean(KEY_PROMPT_BROADCAST,true);
        }

        return prompt;
    }

    public void clearSessionToken()
    {
        editor.clear();
        editor.commit();
    }

}
