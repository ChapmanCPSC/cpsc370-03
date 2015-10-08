package com.example.kelly.gasmeup.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.provider.BaseColumns;

import com.example.kelly.gasmeup.models.SaveLocationModel;

/**
 * Created by Kelly on 10/7/15.
 */
public class SaveLocationEntry implements BaseColumns {

    public static final String TABLE_SAVED = "saved";
    public static final String COLUMN_LOCATION = "location_name";
    public static final String COLUMN_ADDRESS = "saved_address";

    public static final String CREATE_TABLE_SAVED = "CREATE TABLE "
            + TABLE_SAVED + "("
            + COLUMN_LOCATION + " TEXT, "
            + COLUMN_ADDRESS + " TEXT PRIMARY KEY );";


    public static ContentValues MakeContentValues(SaveLocationModel model){
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_LOCATION, model.getLocationName());
        cv.put(COLUMN_ADDRESS, model.getAddress());
        return cv;
    }

    public static SaveLocationModel MakeModel(Cursor c){
        String locationName = c.getString(c.getColumnIndex(COLUMN_LOCATION));
        String address = c.getString(c.getColumnIndex(COLUMN_ADDRESS));
        return new SaveLocationModel(locationName, address);
    }
}
