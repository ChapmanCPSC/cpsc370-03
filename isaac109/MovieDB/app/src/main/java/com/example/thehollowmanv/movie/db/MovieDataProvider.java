package com.example.thehollowmanv.movie.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.thehollowmanv.movie.models.RecentMovieResultModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by TheHollowManV on 10/11/2015.
 */
public class MovieDataProvider
{
    private static SQLiteDatabase GetDB(boolean writable, Context context)
    {
        MovieDatabase helper = new MovieDatabase(context);
        if(writable)
        {
            return helper.getWritableDatabase();
        }
        else
        {
            return helper.getReadableDatabase();
        }
    }
    public static void InsertRecentMovie(RecentMovieResultModel model, Context context)
    {
        SQLiteDatabase db = GetDB(true,context);

        String selection = RecentMovieEntry.COLUMN_TITLE+" = ?";
        String[] selectionArgs = new String[]{model.getTitle()};
        Cursor c = db.query(RecentMovieEntry.TABLE_NAME, null, selection, selectionArgs, null, null, null);
        if(c.moveToNext())
        {
            RecentMovieResultModel existing = RecentMovieEntry.MakeModel(c);
            existing.setCount(existing.getCount()+1);

            ContentValues newValues = RecentMovieEntry.MakeContentValues(existing);
            db.update(RecentMovieEntry.TABLE_NAME, newValues,selection,selectionArgs);
        }
        else
        {
            ContentValues cv = RecentMovieEntry.MakeContentValues(model);
            db.insert(RecentMovieEntry.TABLE_NAME,null,cv);
        }
        c.close();
        db.close();
    }
    public static List<RecentMovieResultModel> GetRecentMovies(Context context)
    {
        SQLiteDatabase db = GetDB(false, context);
        List<RecentMovieResultModel> movies = new ArrayList<RecentMovieResultModel>();
        Cursor c = db.query(RecentMovieEntry.TABLE_NAME, null, null, null, null, null, RecentMovieEntry.COLUMN_COUNT+" DESC");
        while (c.moveToNext())
        {
            RecentMovieResultModel movie = RecentMovieEntry.MakeModel(c);
            movies.add(movie);
        }
        c.close();
        db.close();
        return movies;
    }
}
