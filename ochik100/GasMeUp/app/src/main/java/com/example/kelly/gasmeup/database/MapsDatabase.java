package com.example.kelly.gasmeup.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import javax.xml.parsers.SAXParser;

/**
 * Created by Kelly on 10/7/15.
 */
public class MapsDatabase extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "MapsDB.db";
    private static final int DATABASE_VERSION = 1;

    public MapsDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(FavoriteLocationEntry.CREATE_TABLE_FAVORITES);
        db.execSQL(SaveLocationEntry.CREATE_TABLE_SAVED);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL(getDropCommand(FavoriteLocationEntry.TABLE_FAVORITES));
        db.execSQL(getDropCommand(SaveLocationEntry.TABLE_SAVED));
        onCreate(db);

    }

    private String getDropCommand(String tableName)
    {
        return "DROP TABLE "+tableName;
    }
}
