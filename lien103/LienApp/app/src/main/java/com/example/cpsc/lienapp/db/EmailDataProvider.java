package com.example.cpsc.lienapp.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import com.example.cpsc.lienapp.db.RecentEmailModel;

/**
 * Created by IsaacLien on 10/6/15.
 */
public class EmailDataProvider
{
    private static SQLiteDatabase GetDB(boolean writable, Context ctx)
    {
        EmailDatabase helper = new EmailDatabase(ctx);
        if (writable)
        {
            return helper.getWritableDatabase();
        }
        else
        {
            return helper.getReadableDatabase();
        }
    }

    public static void InsertRecentEmail(RecentEmailModel model, Context ctx)
    {
        SQLiteDatabase db = GetDB(true, ctx);

        //check if item exists
        String selection = RecentEmailEntry.COLUMN_EMAIL+" = ?";
        String[] selectionArgs = new String[]{model.getEmailAddress()};

        Cursor c = db.query(RecentEmailEntry.TABLE_NAME, null, selection, selectionArgs, null, null, null);

        if (c.moveToNext())
        {
            //update
            RecentEmailModel existing = RecentEmailEntry.MakeModel(c);
            existing.setCount(existing.getCount()+1);

            ContentValues newValues = RecentEmailEntry.MakeContentValues(existing);
            db.update(RecentEmailEntry.TABLE_NAME,newValues,selection,selectionArgs);
        }
        else
        {
            ContentValues cv = RecentEmailEntry.MakeContentValues(model);
            db.insert(RecentEmailEntry.TABLE_NAME, null, cv);
        }
        c.close();
        db.close();
    }

    public static List<RecentEmailModel> GetRecentEmails(Context ctx)
    {
        SQLiteDatabase db = GetDB(false, ctx);

        List<RecentEmailModel> emails = new ArrayList<RecentEmailModel>();

        Cursor c = db.query(RecentEmailEntry.TABLE_NAME, null, null, null, null, null, RecentEmailEntry.COLUMN_COUNT+ " DESC");

        while (c.moveToNext())
        {
            RecentEmailModel email = RecentEmailEntry.MakeModel(c);
            emails.add(email);
        }

        c.close();
        db.close();

        return emails;
    }
}
