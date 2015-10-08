package edu.chapman.martin.stationmaster.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import edu.chapman.martin.stationmaster.models.FavoriteStationModel;

/**
 * Created by Martin on 10/5/2015.
 */
public class FavoriteStationProvider {

    private static SQLiteDatabase GetDB(boolean writable, Context context){
        StationMasterDb helper = new StationMasterDb(context);

        if (writable)
        {
            return helper.getWritableDatabase();
        }
        else
        {
            return helper.getReadableDatabase();
        }
    }

    public static void InsertFavoriteStation(FavoriteStationModel model, Context ctx)
    {
        SQLiteDatabase db = GetDB(true, ctx);

        //check if item exists
        String selection = FavoriteStationEntry.COLUMN_CODE+" = ?";
        String[] selectionArgs = new String[]{model.getCode()};

        Cursor c = db.query(FavoriteStationEntry.TABLE_NAME, null, selection, selectionArgs, null, null, null);

        if (c.moveToNext())
        {
            //update
            FavoriteStationModel existing = FavoriteStationEntry.MakeModel(c);
            existing.setCount(existing.getCount() + 1);

            ContentValues newValues = FavoriteStationEntry.MakeContentValues(existing);
            db.update(FavoriteStationEntry.TABLE_NAME,newValues,selection,selectionArgs);
        }
        else
        {
            ContentValues cv = FavoriteStationEntry.MakeContentValues(model);
            db.insert(FavoriteStationEntry.TABLE_NAME, null, cv);
        }
        c.close();
        db.close();
    }

    public static List<FavoriteStationModel> GetFavoriteStations(Context ctx)
    {
        SQLiteDatabase db = GetDB(false, ctx);

        List<FavoriteStationModel> stations = new ArrayList<FavoriteStationModel>();

        Cursor c = db.query(FavoriteStationEntry.TABLE_NAME, null, null, null, null, null, FavoriteStationEntry.COLUMN_COUNT+ " DESC", String.valueOf(5));

        while (c.moveToNext())
        {
            FavoriteStationModel station = FavoriteStationEntry.MakeModel(c);
            stations.add(station);
        }

        c.close();
        db.close();

        return stations;
    }
}
