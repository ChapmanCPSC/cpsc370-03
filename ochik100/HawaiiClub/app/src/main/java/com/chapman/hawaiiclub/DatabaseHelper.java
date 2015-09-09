package com.chapman.hawaiiclub;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper{
	SQLiteDatabase db;
	// Database version
	private static final int DATABASE_VERSION = 1;
	
	// Database name
	public static final String DATABASE_NAME = "HawaiiClubDB";
	
	// Table names
	public static final String TABLE_MEMBERS = "Members";
	public static final String TABLE_MEMBERSPHONE = "MembersPhone";
	public static final String TABLE_MEMBERSINFO = "MembersInfo";
	public static final String TABLE_EVENTS = "Events";
	
	// ID
	public static final String TABLE_ID = "_id";
	public static final String TABLE_EVENTS_ID = "_EventsID";
	public static final String TABLE_MEMBERSPHONE_ID = "_MembersPhoneID";
	public static final String TABLE_MEMBERSINFO_ID = "_MembersInfoID";
	
	// Members Table attribute
	public static final String KEY_MEMBER_NAME = "Name";
	
	// MembersPhone Table attributes
	public static final String KEY_MEMBERSPHONE_MEMBERID = "_MembersID";
	public static final String KEY_MEMBERSPHONE_PHONENUMBER = "PhoneNumber";
	
	// MembersInfo Table attributes
	public static final String KEY_MEMBERSINFO_MEMBERSID = "_MembersID";
	public static final String KEY_MEMBERSINFO_EMAIL = "Email";
	public static final String KEY_MEMBERSINFO_STUDENTID = "StudentID";
	public static final String KEY_MEMBERSINFO_MAJOR = "Major";
	
	// Events Table attributes
	public static final String KEY_EVENTS_EVENT = "Events";
	public static final String KEY_EVENTS_DATE = "Date";
	public static final String KEY_EVENTS_TIME = "Time";
	public static final String KEY_EVENTS_PLACE = "Place";
	
	// Create Members table
	private static final String CREATE_TABLE_MEMBERS = "CREATE TABLE " 
			+ TABLE_MEMBERS + "(" 
			+ TABLE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " 
			+ KEY_MEMBER_NAME + " TEXT" + ");";
	
	// Create Events table
	private static final String CREATE_TABLE_EVENTS = "CREATE TABLE " 
			+ TABLE_EVENTS 
			+ "(" + TABLE_EVENTS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " 
			+ KEY_EVENTS_EVENT + " TEXT, " 
			+ KEY_EVENTS_DATE + " TEXT, " 
			+ KEY_EVENTS_TIME + " TEXT, "
			+ KEY_EVENTS_PLACE + " TEXT " + ");";
	
	// Create MembersPhone table
	private static final String CREATE_TABLE_MEMBERSPHONE = "CREATE TABLE " 
			+ TABLE_MEMBERSPHONE
			+ "(" + TABLE_MEMBERSPHONE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ KEY_MEMBERSPHONE_MEMBERID + " INTEGER, "
			+ KEY_MEMBERSPHONE_PHONENUMBER + " TEXT" + ");";
	
	// Create MembersInfo table
	private static final String CREATE_TABLE_MEMBERSINFO = "CREATE TABLE " 
			+ TABLE_MEMBERSINFO
			+ "(" + TABLE_MEMBERSINFO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " 
			+ KEY_MEMBERSINFO_MEMBERSID + " INTEGER, " 
			+ KEY_MEMBERSINFO_EMAIL + " TEXT, "
			+ KEY_MEMBERSINFO_STUDENTID + " TEXT, " 
			+ KEY_MEMBERSINFO_MAJOR + " TEXT " + ");";
	
	
	
	public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
	
	public static String createAttendingEvent(String event){
		final String TABLE_EVENT_NAME = "TABLE_" + event.toUpperCase();
		
		System.out.println("creating table called = " + TABLE_EVENT_NAME);
		final String TABLE_ID = "TABLE_" + event.toUpperCase() + "_ID";
		final String KEY_EVENT_ID = event.toUpperCase() + "_" + event.toUpperCase() + "ID";
		final String KEY_EVENT_MEMBERID = event.toUpperCase() + "_MEMBERID";
		
		final String CREATE_EVENT_ATTENDING_TABLE = "CREATE TABLE IF NOT EXISTS "
				+ TABLE_EVENT_NAME + "(" + TABLE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ KEY_EVENT_ID + " INTEGER, "
				+ KEY_EVENT_MEMBERID + " INTEGER " + ");";
		return CREATE_EVENT_ATTENDING_TABLE;
		
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		
		// Creates the main tables for the database
		db.execSQL(CREATE_TABLE_MEMBERS);
		db.execSQL(CREATE_TABLE_MEMBERSPHONE);
		db.execSQL(CREATE_TABLE_MEMBERSINFO);
		db.execSQL(CREATE_TABLE_EVENTS);
		
		// Creates the tables for keeping track of members attending events
		String[] eventsArray = AddEvent.eventsList.toArray(new String[AddEvent.eventsList.size()]);
		for(int i = 0; i < AddEvent.eventsList.size(); i++){
			db.execSQL(createAttendingEvent(eventsArray[0]));
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_MEMBERS );
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_MEMBERSPHONE);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_MEMBERSINFO);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENTS );
		
		String[] eventsArray = AddEvent.eventsList.toArray(new String[AddEvent.eventsList.size()]);
		for(int i = 0; i < AddEvent.eventsList.size(); i++){
			db.execSQL("DROP TABLE IF EXISTS TABLE_" + eventsArray[0].toUpperCase());
		}
		
		
		
		onCreate(db);
	}
	
	

}
