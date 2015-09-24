package com.example.thehollowmanv.movie.tasks;

import android.os.AsyncTask;

import com.example.thehollowmanv.movie.MainActivity;
import com.example.thehollowmanv.movie.MovieAPIWrapper;
import com.example.thehollowmanv.movie.models.MovieResultModel;

/**
 * Created by TheHollowManV on 9/23/2015.
 */
public class GetMovieTask extends AsyncTask<String,Void,MovieResultModel>
{
    MainActivity _activity;
    public GetMovieTask(MainActivity activity)
    {
        _activity = activity;
    }

    @Override
    protected void onPreExecute()
    {
        super.onPreExecute();
        _activity.loadingStarted();
    }

    @Override
    protected MovieResultModel doInBackground(String... params)
    {
        MovieResultModel model = MovieAPIWrapper.GetMovie(params[0]);
        return model;
    }

    @Override
    protected void onPostExecute(MovieResultModel movieResultModel)
    {
        super.onPostExecute(movieResultModel);
        _activity.loadingFinished(movieResultModel);
    }
}
