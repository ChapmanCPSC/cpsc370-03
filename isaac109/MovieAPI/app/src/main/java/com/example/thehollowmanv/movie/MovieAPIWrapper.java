package com.example.thehollowmanv.movie;

import com.example.thehollowmanv.movie.models.MovieResultModel;
import com.google.gson.Gson;

import java.io.IOException;

/**
 * Created by TheHollowManV on 9/22/2015.
 */
public class MovieAPIWrapper
{
    private static String BaseUrl = "http://www.omdbapi.com/?";
    public static MovieResultModel GetMovie(String title)
    {
        String newTitle = title.replace(' ','+');
        String queryString = "t="+newTitle+"&y=&plot=short&r=json";
        String fullURL = BaseUrl+queryString;
        String response = "";
        try
        {
            response = new WebRequest(fullURL).get();
            MovieResultModel model = new Gson().fromJson(response, MovieResultModel.class);
            return model;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }
}
