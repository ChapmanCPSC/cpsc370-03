package com.example.cpsc.lienapp.tasks;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;

import com.example.cpsc.lienapp.NameAPIWrapper;
import com.example.cpsc.lienapp.models.NameResultModel;

/**
 * Created by IsaacLien on 10/6/15.
 */
public class GetEmailTask extends AsyncTask<String, Void, Void>
{
    Context _ctx;
    public Runnable onFinish;
    public NameResultModel _result;

    public GetEmailTask(Context ctx)
    {
        _ctx = ctx;
    }

    @Override
    protected Void doInBackground(String... params)
    {
        SharedPreferences defaultPrefs = PreferenceManager.getDefaultSharedPreferences(_ctx);

        _result = NameAPIWrapper.GetNameInfo(params[0]);
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
