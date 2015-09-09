package com.example.disneylander.db;

import com.example.disneylander.RideLogModel;
import com.example.disneylander.RideModel;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class DbContract extends SQLiteOpenHelper {
	public static final int DATABASE_VERSION = 4;
	public static final String DATABASE_NAME = "disneylander.db";

	public static final String CREATE_TABLE = "CREATE TABLE ";
	public static final String DROP_TABLE = "DROP TABLE IF EXISTS ";

	public static final String TYPE_INT = " INTEGER";
	public static final String TYPE_TEXT = " TEXT";

	public static final String PRIMARY_KEY = " PRIMARY KEY";

	public static final String INT_PRIMARY_KEY = TYPE_INT + PRIMARY_KEY;

	public static final String COMMA_SEP = ",";
	public static final String SEMI_SEP = ";";
	
	 public static final String AUTOINCREMENT	= " AUTOINCREMENT";

	public DbContract(Context c) {
		super(c, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(RideEntry.CREATE_COMMAND);
		db.execSQL(RideLogEntry.CREATE_COMMAND);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

		db.execSQL(RideEntry.DELETE_COMMAND);
		db.execSQL(RideLogEntry.DELETE_COMMAND);
		
		onCreate(db);
	}

	public static class RideEntry implements BaseColumns {
		public static final String TABLE_NAME = "rides";
		public static final String COLUMN_ID = "id";
		public static final String COLUMN_IS_TODO = "is_todo";
		public static final String COLUMN_NAME = "name";

		public static final String CREATE_COMMAND = CREATE_TABLE + TABLE_NAME
				+ " (" + COLUMN_ID + TYPE_INT + PRIMARY_KEY + COMMA_SEP
				+ COLUMN_NAME + TYPE_TEXT + COMMA_SEP + COLUMN_IS_TODO
				+ TYPE_INT + ");";

		public static final String DELETE_COMMAND = DROP_TABLE + TABLE_NAME
				+ SEMI_SEP;
		public static final String[] PROJECTION = { COLUMN_ID, COLUMN_IS_TODO,
				COLUMN_NAME };

		public static ContentValues MakeContentValues(RideModel rm) {
			ContentValues values = new ContentValues();
			values.put(COLUMN_ID, rm.m_rideId);
			values.put(COLUMN_IS_TODO, rm.m_isToDo ? 1 : 0);
			values.put(COLUMN_NAME, rm.m_rideName);

			return values;
		}

		public static RideModel MakeModel(Cursor cur) {
			int id = cur.getInt(cur.getColumnIndexOrThrow(COLUMN_ID));
			int isToDo = cur.getInt(cur.getColumnIndexOrThrow(COLUMN_IS_TODO));
			String name = cur.getString(cur.getColumnIndexOrThrow(COLUMN_NAME));

			RideModel rm = new RideModel(isToDo == 1, id, name);
			return rm;
		}
	}

	public static class RideLogEntry implements BaseColumns {
		public static final String TABLE_NAME = "ride_logs";
		public static final String COLUMN_ID = "id";
		public static final String COLUMN_RIDE_ID = "ride_id";

		public static final String CREATE_COMMAND = CREATE_TABLE + TABLE_NAME
				+ " (" + COLUMN_ID + TYPE_INT + PRIMARY_KEY + AUTOINCREMENT + COMMA_SEP
				+ COLUMN_RIDE_ID + TYPE_INT + COMMA_SEP + "FOREIGN KEY("
				+ COLUMN_RIDE_ID + ") REFERENCES " + RideEntry.TABLE_NAME+"("+RideEntry.COLUMN_ID + ")"
				+ ");";

		public static final String DELETE_COMMAND = DROP_TABLE + TABLE_NAME
				+ SEMI_SEP;
		public static final String[] PROJECTION = { COLUMN_ID, COLUMN_RIDE_ID};

		public static ContentValues MakeContentValues(RideLogModel rlm) {
			ContentValues values = new ContentValues();
			values.put(COLUMN_RIDE_ID, rlm.m_rideId);

			return values;
		}

		public static RideLogModel MakeModel(Cursor cur) {
			int id = cur.getInt(cur.getColumnIndexOrThrow(COLUMN_ID));
			int rideId = cur.getInt(cur.getColumnIndexOrThrow(COLUMN_RIDE_ID));

			RideLogModel rlm = new RideLogModel(rideId);
			rlm.m_logId = id;
			return rlm;
		}
	}

}
