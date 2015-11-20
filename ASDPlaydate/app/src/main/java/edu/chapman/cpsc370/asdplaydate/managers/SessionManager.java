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

    public void clearSessionToken()
    {
        editor.clear();
        editor.commit();
    }

}
