package com.alecrichter.textalarm;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;


public class AlarmDatabase extends SQLiteOpenHelper {

    static String databaseName = "alarmdb.db";
    static int databaseVersion = 4;

    public AlarmDatabase(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, databaseName, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table Alarms(Id integer primary key, Name text, Type text, ContentO text," +
                " ContentF text, Activated text, AlarmToneUri text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table Alarms");
        onCreate(db);
    }

    public void addAlarm(Alarm alarm) {

        //Reference to db
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        //Values to be inserted
        values.put("Name", alarm.getTitle());
        values.put("Type", alarm.getType());
        values.put("ContentO", alarm.getContentO());
        values.put("ContentF", alarm.getContentF());
        values.put("Activated", alarm.getActivated());
        values.put("AlarmToneUri", alarm.getUri());

        //Add to db
        db.insert("Alarms", null, values);
        db.close();
    }

    public Alarm getAlarm(String id) {

        //Reference to db
        SQLiteDatabase db = this.getWritableDatabase();

        //Column names
        String[] columns = {"Name", "Type", "ContentO", "ContentF", "Activated", "AlarmToneUri"};

        //Find alarm by id
        Cursor cursor = db.query(true, "Alarms", columns, "Id=?", new String[] {id}, null, null, null, null, null);

        if(cursor.getCount() != 0) {
            cursor.moveToFirst();

            //Create new film object
            Alarm alarm = new Alarm(cursor.getString(0), cursor.getString(1), cursor.getString(2),
                    cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6));

            db.close();
            return alarm;
        }

        else {
            db.close();
            return null;
        }

    }

    public List<Alarm> getAllAlarms() {

        List<Alarm> alarmList = new ArrayList<Alarm>();

        //Reference to db
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery("select * from Alarms", null);

        //Get elements from each row, add to list
        if(cursor.moveToFirst()) {
            do {
                Alarm alarm = new Alarm(cursor.getString(0), cursor.getString(1), cursor.getString(2),
                        cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6));

                alarmList.add(alarm);
            } while(cursor.moveToNext());
        }

        db.close();

        return alarmList;
    }

    public void deleteAlarm(Alarm alarm) {

        //Reference to db
        SQLiteDatabase db = this.getWritableDatabase();

        //Delete from db
        db.delete("Alarms", "Id=?", new String[] {alarm.getId()});
        db.close();
    }

    public void editAlarmToggle(Alarm alarm) {

        //Reference to db
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        //Get new value
        values.put("Activated", alarm.getActivated());

        //Update db entry
        db.update("Alarms", values, "Id=?", new String[] {alarm.getId()});
        db.close();

    }

    public void editAlarmTone(Alarm alarm) {

        //Reference to db
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        //Get new value
        values.put("AlarmToneUri", alarm.getUri());

        //Update db entry
        db.update("Alarms", values, "Id=?", new String[] {alarm.getId()});
        db.close();
    }

}

