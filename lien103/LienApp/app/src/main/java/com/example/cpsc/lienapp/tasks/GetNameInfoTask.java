package com.example.cpsc.lienapp.tasks;

import android.os.AsyncTask;

import com.example.cpsc.lienapp.NameAPIWrapper;
import com.example.cpsc.lienapp.fragments.NameSearchFragment;
import com.example.cpsc.lienapp.models.NameResultModel;


public class GetNameInfoTask extends AsyncTask<String, Void, NameResultModel>
{
    NameSearchFragment _fragment;

    public GetNameInfoTask(NameSearchFragment fragment)
    {
        _fragment = fragment;
    }

    @Override
    protected void onPreExecute()
    {
        super.onPreExecute();
        _fragment.loadingStarted();
    }

    @Override
    protected NameResultModel doInBackground(String... params)
    {
        NameResultModel model = NameAPIWrapper.GetNameInfo(params[0]);
        return model;
    }

    @Override
    protected void onPostExecute(NameResultModel nameResultModel)
    {
        super.onPostExecute(nameResultModel);
        _fragment.loadingFinished(nameResultModel);
    }
}
