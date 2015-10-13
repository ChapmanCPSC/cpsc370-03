package com.example.thehollowmanv.movie.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.provider.BaseColumns;

import com.example.thehollowmanv.movie.models.RecentMovieResultModel;

/**
 * Created by TheHollowManV on 10/11/2015.
 */
public class RecentMovieEntry implements BaseColumns
{
    public static final String TABLE_NAME = "recent_movies";

    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_YEAR = "year";
    public static final String COLUMN_RATED = "rated";
    public static final String COLUMN_RUNTIME = "runtime";
    public static final String COLUMN_PLOT = "plot";
    public static final String COLUMN_AWARDS = "awards";
    public static final String COLUMN_METASCORE = "meta_score";
    public static final String COLUMN_IMDB = "imdb_rating";
    public static final String COLUMN_COUNT = "count";

    public static final String CREATE_COMMAND = "CREATE TABLE "+TABLE_NAME+" ("+COLUMN_TITLE+" TEXT PRIMARY KEY, "+COLUMN_YEAR+" TEXT NOT NULL, "+COLUMN_RATED+" TEXT NOT NULL, "+COLUMN_RUNTIME+" TEXT NOT NULL, "+COLUMN_PLOT+" TEXT NOT NULL, "+COLUMN_AWARDS+" TEXT NOT NULL, "+ COLUMN_METASCORE+" TEXT NOT NULL, "+ COLUMN_IMDB + " TEXT NOT NULL, "+ COLUMN_COUNT+ " INT NOT NULL);";

    public static ContentValues MakeContentValues(RecentMovieResultModel model)
    {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_TITLE, model.getTitle());
        cv.put(COLUMN_YEAR, model.getYear());
        cv.put(COLUMN_RATED, model.getRated());
        cv.put(COLUMN_RUNTIME, model.getRuntime());
        cv.put(COLUMN_PLOT, model.getPlot());
        cv.put(COLUMN_AWARDS, model.getAwards());
        cv.put(COLUMN_METASCORE, model.getMetascore());
        cv.put(COLUMN_IMDB, model.getImdbRating());
        cv.put(COLUMN_COUNT, model.getCount());

        return cv;
    }
    public static RecentMovieResultModel MakeModel(Cursor c)
    {
        String Title = c.getString(c.getColumnIndex(COLUMN_TITLE));
        String Year = c.getString(c.getColumnIndex(COLUMN_YEAR));
        String Rated = c.getString(c.getColumnIndex(COLUMN_RATED));
        String Runtime = c.getString(c.getColumnIndex(COLUMN_RUNTIME));
        String Plot = c.getString(c.getColumnIndex(COLUMN_PLOT));
        String Awards = c.getString(c.getColumnIndex(COLUMN_AWARDS));
        String Metascore = c.getString(c.getColumnIndex(COLUMN_METASCORE));
        String imdbRating = c.getString(c.getColumnIndex(COLUMN_IMDB));
        int count = c.getInt(c.getColumnIndex(COLUMN_COUNT));
        return new RecentMovieResultModel(Title,Year,Rated,Runtime,Plot,Awards,Metascore,imdbRating,count);
    }
}
