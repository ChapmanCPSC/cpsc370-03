package com.example.thehollowmanv.movie.tasks;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;

import com.example.thehollowmanv.movie.MainActivity;
import com.example.thehollowmanv.movie.MovieAPIWrapper;
import com.example.thehollowmanv.movie.models.MovieResultModel;

/**
 * Created by TheHollowManV on 9/23/2015.
 */
public class GetMovieTask extends AsyncTask<String,Void,Void>
{
    Context _context;
    public Runnable onFinish;
    public MovieResultModel _result;
    public GetMovieTask(Context context)
    {
        _context = context;
    }


    @Override
    protected Void doInBackground(String... params)
    {
        SharedPreferences defaultPrefs = PreferenceManager.getDefaultSharedPreferences(_context);
        _result = MovieAPIWrapper.GetMovie(params[0]);
        return null;
    }

    @Override
    protected void onPostExecute(Void params)
    {
        super.onPostExecute(params);
        if(onFinish!=null)
        {
            onFinish.run();
        }
    }
}
