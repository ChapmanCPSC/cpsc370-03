package com.example.kelly.gasmeup.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.kelly.gasmeup.models.FavoriteLocationModel;
import com.example.kelly.gasmeup.models.SaveLocationModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kelly on 10/6/15.
 */
public class LocationDataProvider {

    private static SQLiteDatabase GetDB(boolean writable, Context context)
    {
        MapsDatabase helper = new MapsDatabase(context);
        if (writable)
        {
            return helper.getWritableDatabase();
        }
        else
        {
            return helper.getReadableDatabase();
        }
    }

    public static void InsertSavedLocation(SaveLocationModel model, Context context){
        SQLiteDatabase db = GetDB(true, context);

        String selection = SaveLocationEntry.COLUMN_ADDRESS + " = ?";
        String[] selectionArgs = new String[]{model.getAddress()};

        Cursor c = db.query(SaveLocationEntry.TABLE_SAVED, null, selection, selectionArgs, null, null, null);

        if(c.moveToNext()){
            SaveLocationModel existing = SaveLocationEntry.MakeModel(c);

            if(!model.getAddress().equalsIgnoreCase(existing.getAddress())){
                existing.setLocationName(model.getLocationName());
            }

            ContentValues newValues = SaveLocationEntry.MakeContentValues(existing);
            db.update(SaveLocationEntry.TABLE_SAVED, newValues, selection, selectionArgs);
        } else {
            ContentValues cv = SaveLocationEntry.MakeContentValues(model);
            db.insert(SaveLocationEntry.TABLE_SAVED, null, cv);
        }

        c.close();
        db.close();

    }

    public static void InsertFavoriteLocation(FavoriteLocationModel model, Context context){

        SQLiteDatabase db = GetDB(true, context);

        String selection = FavoriteLocationEntry.COLUMN_DESTINATION + " = ?";
        String[] selectionArgs = new String[]{model.getAddress()};

        Cursor c = db.query(FavoriteLocationEntry.TABLE_FAVORITES, null, selection, selectionArgs, null, null, null);

        if(c.moveToNext()){

            FavoriteLocationModel existing = FavoriteLocationEntry.MakeModel(c);
            existing.setCount(existing.getCount()+1);

            if(!model.getLocationName().equalsIgnoreCase(existing.getLocationName())) {
                existing.setLocationName(model.getLocationName());
            }

            ContentValues newValues = FavoriteLocationEntry.MakeContentValues(existing);
            db.update(FavoriteLocationEntry.TABLE_FAVORITES, newValues, selection, selectionArgs);

        } else {

            ContentValues cv = FavoriteLocationEntry.MakeContentValues(model);
            db.insert(FavoriteLocationEntry.TABLE_FAVORITES, null, cv);

        }

        c.close();
        db.close();

    }

    public static List<FavoriteLocationModel> GetFavoriteLocations(Context context){

        SQLiteDatabase db = GetDB(false, context);

        List<FavoriteLocationModel> locations = new ArrayList<FavoriteLocationModel>();

        Cursor c = db.query(FavoriteLocationEntry.TABLE_FAVORITES, null, null, null, null, null, FavoriteLocationEntry.COLUMN_COUNT + " DESC");

        while(c.moveToNext()){
            FavoriteLocationModel location = FavoriteLocationEntry.MakeModel(c);
            locations.add(location);
        }

        c.close();
        db.close();

        return locations;
    }

    public static List<SaveLocationModel> GetSavedLocations(Context context){

        SQLiteDatabase db = GetDB(false, context);

        List<SaveLocationModel> locations = new ArrayList<SaveLocationModel>();
        Cursor c = db.query(SaveLocationEntry.TABLE_SAVED, null, null, null, null, null, SaveLocationEntry.COLUMN_LOCATION + " ASC");

        while(c.moveToNext()){
            SaveLocationModel location = SaveLocationEntry.MakeModel(c);
            locations.add(location);
        }

        c.close();
        db.close();

        return locations;
    }
}
