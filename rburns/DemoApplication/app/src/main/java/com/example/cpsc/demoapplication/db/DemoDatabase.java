package com.example.cpsc.demoapplication.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by ryanb on 9/30/2015.
 */
public class DemoDatabase extends SQLiteOpenHelper
{
    private static final String DB_NAME = "DemoDB.db";
    private static final int DB_VERSION = 1;

    public DemoDatabase(Context ctx)
    {
        super(ctx, DB_NAME,null,DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(RecentLocationEntry.CREATE_COMMAND);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        //drop all tables
        db.execSQL(getDropCommand(RecentLocationEntry.TABLE_NAME));
        //create them again
        onCreate(db);
    }

    private String getDropCommand(String tableName)
    {
        return "DROP TABLE "+tableName;
    }
}
