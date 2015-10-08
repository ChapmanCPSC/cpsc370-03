package edu.chapman.martin.stationmaster.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.provider.BaseColumns;

import edu.chapman.martin.stationmaster.models.FavoriteStationModel;

/**
 * Created by Martin on 10/5/2015.
 */
public class FavoriteStationEntry implements BaseColumns
{
    public static final String TABLE_NAME = "favorite_stations";

    public static final String COLUMN_CODE = "code";
    public static final String COLUMN_COUNT = "count";

    public static final String CREATE_COMMAND = "CREATE TABLE "
            +TABLE_NAME
            +"("
            + COLUMN_CODE
            +" TEXT PRIMARY KEY, "
            +COLUMN_COUNT
            +" INT NOT NULL);";

    public static ContentValues MakeContentValues(FavoriteStationModel model)
    {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_CODE, model.getCode());
        cv.put(COLUMN_COUNT, model.getCount());

        return cv;
    }

    public static FavoriteStationModel MakeModel(Cursor c)
    {
        String code = c.getString(c.getColumnIndex(COLUMN_CODE));
        int count = c.getInt(c.getColumnIndex(COLUMN_COUNT));

        return new FavoriteStationModel(code, count);
    }
}

