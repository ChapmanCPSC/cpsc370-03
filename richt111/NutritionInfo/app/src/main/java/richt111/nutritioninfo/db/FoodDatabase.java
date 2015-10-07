package richt111.nutritioninfo.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class FoodDatabase extends SQLiteOpenHelper {

    private static final String DB_NAME = "FoodDB.db";
    private static final int DB_VERSION = 1;

    public FoodDatabase(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(FoodItemEntry.CREATE_COMMAND);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // Drop previous table
        db.execSQL("DROP TABLE " + FoodItemEntry.TABLE_NAME + ";");

        // Recreate table
        onCreate(db);
    }
}
