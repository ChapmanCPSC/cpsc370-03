package com.example.thehollowmanv.movie.models;

/**
 * Created by TheHollowManV on 10/11/2015.
 */
public class RecentMovieResultModel
{
    private String Title;
    private String Year;
    private String Rated;
    private String Runtime;
    private String Plot;
    private String Awards;
    private String Metascore;
    private String imdbRating;
    private int count;

    public RecentMovieResultModel(String title, String year, String rated, String runTime, String plot, String awards, String metaScore, String imdb, int count)
    {
        Title = title;
        Year = year;
        Rated = rated;
        Runtime = runTime;
        Plot = plot;
        Awards = awards;
        Metascore = metaScore;
        imdbRating = imdb;
        this.count = count;
    }
    public RecentMovieResultModel(String title, String year, String rated, String runTime, String plot, String awards, String metaScore, String imdb)
    {
        Title = title;
        Year = year;
        Rated = rated;
        Runtime = runTime;
        Plot = plot;
        Awards = awards;
        Metascore = metaScore;
        imdbRating = imdb;
        this.count = 1;
    }
    public String getTitle()
    {
        return Title;
    }
    public String getYear()
    {
        return Year;
    }
    public String getRated()
    {
        return Rated;
    }
    public String getRuntime()
    {
        return Runtime;
    }
    public String getPlot()
    {
        return Plot;
    }
    public String getAwards()
    {
        return Awards;
    }
    public String getMetascore()
    {
        return Metascore;
    }
    public String getImdbRating()
    {
        return imdbRating;
    }
    public int getCount()
    {
        return count;
    }

    public void setTitle(String title)
    {
        Title = title;
    }

    public void setCount(int count)
    {
        this.count = count;
    }

    public void setImdbRating(String imdbRating)
    {

        this.imdbRating = imdbRating;
    }

    public void setMetascore(String metascore)
    {

        Metascore = metascore;
    }

    public void setAwards(String awards)
    {

        Awards = awards;
    }

    public void setPlot(String plot)
    {

        Plot = plot;
    }

    public void setRuntime(String runtime)
    {

        Runtime = runtime;
    }

    public void setRated(String rated)
    {

        Rated = rated;
    }

    public void setYear(String year)
    {

        Year = year;
    }
}
