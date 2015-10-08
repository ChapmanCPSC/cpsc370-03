package com.example.kelly.gasmeup.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.provider.BaseColumns;

import com.example.kelly.gasmeup.models.FavoriteLocationModel;

/**
 * Created by Kelly on 10/7/15.
 */
public class FavoriteLocationEntry implements BaseColumns {

    public static final String TABLE_FAVORITES = "favorites";
    public static final String COLUMN_ORIGIN = "location_origin";
    public static final String COLUMN_DESTINATION = "location_destination";
    public static final String COLUMN_COUNT = "count";

    public static final String CREATE_TABLE_FAVORITES = "CREATE TABLE "
            + TABLE_FAVORITES + "("
            + COLUMN_ORIGIN + " TEXT, "
            + COLUMN_DESTINATION + " TEXT PRIMARY KEY, "
            + COLUMN_COUNT + " INT NOT NULL);";

    public static ContentValues MakeContentValues(FavoriteLocationModel model){
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_ORIGIN, model.getLocationName());
        cv.put(COLUMN_DESTINATION, model.getAddress());
        cv.put(COLUMN_COUNT, model.getCount());
        return cv;
    }

    public static FavoriteLocationModel MakeModel(Cursor c){
        String locationName = c.getString(c.getColumnIndex(COLUMN_ORIGIN));
        String address = c.getString(c.getColumnIndex(COLUMN_DESTINATION));
        int count = c.getInt(c.getColumnIndex(COLUMN_COUNT));
        return new FavoriteLocationModel(locationName, address, count);
    }
}
