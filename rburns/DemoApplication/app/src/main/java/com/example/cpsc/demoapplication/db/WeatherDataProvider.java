package com.example.cpsc.demoapplication.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.cpsc.demoapplication.models.RecentLocationModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ryanb on 9/30/2015.
 */
public class WeatherDataProvider
{

    private static SQLiteDatabase GetDB(boolean writable, Context ctx)
    {
        DemoDatabase helper = new DemoDatabase(ctx);
        if (writable)
        {
            return helper.getWritableDatabase();
        }
        else
        {
            return helper.getReadableDatabase();
        }
    }

    public static void InsertRecentLocation(RecentLocationModel model, Context ctx)
    {
        SQLiteDatabase db = GetDB(true, ctx);

        //check if item exists
        String selection = RecentLocationEntry.COLUMN_ZIP+" = ?";
        String[] selectionArgs = new String[]{model.getZip()};

        Cursor c = db.query(RecentLocationEntry.TABLE_NAME, null, selection, selectionArgs, null, null, null);

        if (c.moveToNext())
        {
            //update
            RecentLocationModel existing = RecentLocationEntry.MakeModel(c);
            existing.setCount(existing.getCount()+1);

            ContentValues newValues = RecentLocationEntry.MakeContentValues(existing);
            db.update(RecentLocationEntry.TABLE_NAME,newValues,selection,selectionArgs);
        }
        else
        {
            ContentValues cv = RecentLocationEntry.MakeContentValues(model);
            db.insert(RecentLocationEntry.TABLE_NAME, null, cv);
        }
        c.close();
        db.close();
    }

    public static List<RecentLocationModel> GetRecentLocations(Context ctx)
    {
        SQLiteDatabase db = GetDB(false, ctx);

        List<RecentLocationModel> locations = new ArrayList<RecentLocationModel>();

        Cursor c = db.query(RecentLocationEntry.TABLE_NAME, null, null, null, null, null, RecentLocationEntry.COLUMN_COUNT+ " DESC");

        while (c.moveToNext())
        {
            RecentLocationModel location = RecentLocationEntry.MakeModel(c);
            locations.add(location);
        }

        c.close();
        db.close();

        return locations;
    }

}
