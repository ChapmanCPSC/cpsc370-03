package com.example.cpsc.demoapplication.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.provider.BaseColumns;

import com.example.cpsc.demoapplication.models.RecentLocationModel;

/**
 * Created by ryanb on 9/30/2015.
 */
public class RecentLocationEntry implements BaseColumns
{
    public static final String TABLE_NAME = "recent_locations";

    public static final String COLUMN_ZIP = "zip";
    public static final String COLUMN_COUNT = "count";

    public static final String CREATE_COMMAND = "CREATE TABLE "
            +TABLE_NAME
            +"("
            +COLUMN_ZIP
            +" TEXT PRIMARY KEY, "
            +COLUMN_COUNT
            +" INT NOT NULL);";

    public static ContentValues MakeContentValues(RecentLocationModel model)
    {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_ZIP, model.getZip());
        cv.put(COLUMN_COUNT, model.getCount());

        return cv;
    }

    public static RecentLocationModel MakeModel(Cursor c)
    {
        String zip = c.getString(c.getColumnIndex(COLUMN_ZIP));
        int count = c.getInt(c.getColumnIndex(COLUMN_COUNT));

        return new RecentLocationModel(zip, count);
    }
}
