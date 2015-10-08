package com.example.cpsc.lienapp.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.provider.BaseColumns;

/**
 * Created by IsaacLien on 10/6/15.
 */
public class RecentEmailEntry implements BaseColumns
{
    public static final String TABLE_NAME = "email_addresses";

    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_COUNT = "count";

    public static final String CREATE_COMMAND = "CREATE TABLE "
            +TABLE_NAME
            +"("
            +COLUMN_EMAIL
            +" TEXT PRIMARY KEY, "
            +COLUMN_COUNT
            +" INT NOT NULL);";

    public static ContentValues MakeContentValues(RecentEmailModel model)
    {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_EMAIL, model.getEmailAddress());
        cv.put(COLUMN_COUNT, model.getCount());

        return cv;
    }

    public static RecentEmailModel MakeModel(Cursor c)
    {
        String email = c.getString(c.getColumnIndex(COLUMN_EMAIL));
        int count = c.getInt(c.getColumnIndex(COLUMN_COUNT));

        return new RecentEmailModel(email, count);
    }
}
