package com.example.cpsc.lienapp.db;

/**
 * Created by IsaacLien on 10/6/15.
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class EmailDatabase extends SQLiteOpenHelper
{
    private static final String DB_NAME = "DemoDB.db";
    private static final int DB_VERSION = 1;

    public EmailDatabase(Context ctx)
    {
        super(ctx, DB_NAME,null,DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(RecentEmailEntry.CREATE_COMMAND);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        //drop all tables
        db.execSQL(getDropCommand(RecentEmailEntry.TABLE_NAME));
        //create them again
        onCreate(db);
    }

    private String getDropCommand(String tableName)
    {
        return "DROP TABLE "+tableName;
    }
}

