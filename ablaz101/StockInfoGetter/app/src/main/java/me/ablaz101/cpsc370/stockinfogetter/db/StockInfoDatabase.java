package me.ablaz101.cpsc370.stockinfogetter.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Xavi on 4/10/15.
 */
public class StockInfoDatabase extends SQLiteOpenHelper
{
    private static final String DB_NAME = "StockInfoDB.db";
    private static final int DB_VERSION = 1;

    public StockInfoDatabase(Context ctx)
    {
        super(ctx, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(PreferredStockEntry.CREATE_COMMAND);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        // Drop all tables
        db.execSQL(getDropCommand(PreferredStockEntry.TABLE_NAME));

        // Create them again
        onCreate(db);
    }

    private String getDropCommand(String tableName)
    {
        return "DROP TABLE IF EXISTS " + tableName;
    }

}
