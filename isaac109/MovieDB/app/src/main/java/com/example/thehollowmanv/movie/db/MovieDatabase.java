package com.example.thehollowmanv.movie.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by TheHollowManV on 10/11/2015.
 */
public class MovieDatabase extends SQLiteOpenHelper
{
    private static final String DB_NAME = "MovieDB.db";
    private static final int DB_VERSION = 3;

    public MovieDatabase(Context con)
    {
        super(con, DB_NAME, null, DB_VERSION);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL(getDropCommand(RecentMovieEntry.TABLE_NAME));
        onCreate(db);
    }

    private String getDropCommand(String tableName)
    {
        return "DROP TABLE "+tableName;
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        Log.e("here", "here");
        db.execSQL(RecentMovieEntry.CREATE_COMMAND);
    }
}
