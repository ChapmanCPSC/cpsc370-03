package com.chapman.hawaiiclub;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class EventDataSource {
	
	private SQLiteDatabase database;
	private DatabaseHelper dbHelper;
	private String[] allColumns = {DatabaseHelper.TABLE_EVENTS_ID, DatabaseHelper.KEY_EVENTS_EVENT, 
			DatabaseHelper.KEY_EVENTS_DATE, DatabaseHelper.KEY_EVENTS_TIME, DatabaseHelper.KEY_EVENTS_PLACE};
	
	public EventDataSource(Context context){
		dbHelper = new DatabaseHelper(context);
	}
	
	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}
	
	public void close() {
		dbHelper.close();
	}
	
	public Event addEvent(String eventName, String eventDate, String eventTime, String eventPlace){
		
		// Adding an event to the table 
		
		ContentValues values = new ContentValues();
		if(eventName.contains(" ")){
			eventName = eventName.replace(" ", "_");
			
			values.put(DatabaseHelper.KEY_EVENTS_EVENT, eventName);
			values.put(DatabaseHelper.KEY_EVENTS_DATE, eventDate);
			values.put(DatabaseHelper.KEY_EVENTS_TIME, eventTime);
			values.put(DatabaseHelper.KEY_EVENTS_PLACE, eventPlace);
		} else {
			values.put(DatabaseHelper.KEY_EVENTS_EVENT, eventName);
			values.put(DatabaseHelper.KEY_EVENTS_DATE, eventDate);
			values.put(DatabaseHelper.KEY_EVENTS_TIME, eventTime);
			values.put(DatabaseHelper.KEY_EVENTS_PLACE, eventPlace);
			
		}
		
		
		
		long insertEventId = database.insert(DatabaseHelper.TABLE_EVENTS, null, values);
		
		Cursor cursor = database.query(DatabaseHelper.TABLE_EVENTS, allColumns, DatabaseHelper.TABLE_EVENTS_ID + " = " + insertEventId,
				null, null, null, null);
		cursor.moveToFirst();
		Event newEvent = cursorToEvent(cursor);
		cursor.close();
		
		return newEvent;
	}
	
	public void updateEvent(long eventId, String eventName, String eventDate, String eventTime, String eventPlace){
		
		// Updates the data of the event
		
		ContentValues values = new ContentValues();
		values.put(DatabaseHelper.KEY_EVENTS_EVENT, eventName);
		values.put(DatabaseHelper.KEY_EVENTS_DATE, eventDate);
		values.put(DatabaseHelper.KEY_EVENTS_TIME, eventTime);
		values.put(DatabaseHelper.KEY_EVENTS_PLACE, eventPlace);
		
		database.update(DatabaseHelper.TABLE_EVENTS, values, DatabaseHelper.TABLE_EVENTS_ID + " = " + eventId, null);
		
	}
	
	public Event getEvent(long eventsID){
		
		// Obtains an Event object from an EventId
		
		Cursor cursor = database.query(DatabaseHelper.TABLE_EVENTS, 
				allColumns, DatabaseHelper.TABLE_EVENTS_ID + " = " + eventsID, 
				null, null, null, null);
		if(cursor != null)
			cursor.moveToFirst();
		
		Event event = new Event(cursor.getLong(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4));
		cursor.close();
		return event;
	}
	
	public long getEventId(String event){
		
		// Gets an EventId by name of event 
		
		Cursor cursor = database.query(DatabaseHelper.TABLE_EVENTS, new String[] {DatabaseHelper.TABLE_EVENTS_ID}, DatabaseHelper.KEY_EVENTS_EVENT  + " = " +  event, null, null, null, null);
		if(cursor != null)
			cursor.moveToFirst();
		
		Long eventId = cursor.getLong(0);
		return eventId;
	}
	
	public void deleteEvent(Event event){
		
		// Deletes an event from the Event Table by object Event
		
		long id = event.getId();
		AddEvent.eventsList.remove(event);
		database.delete(DatabaseHelper.TABLE_EVENTS, DatabaseHelper.TABLE_EVENTS_ID + " = " + id, null);
		
	}
	
	public void deleteEvent(long id){
		
		// Deletes an event from the Event Table by the EventId
		
		database.delete(DatabaseHelper.TABLE_EVENTS, DatabaseHelper.TABLE_EVENTS_ID + " = " + id, null);
	}
	
	public List<Event> getAllEvents() {
		
		// Returns a list of all events
		
		List<Event> events = new ArrayList<Event>();
		Cursor cursor = database.query(DatabaseHelper.TABLE_EVENTS, allColumns, null, null, null, null, null);
		
		cursor.moveToFirst();
		while(!cursor.isAfterLast()){
			Event event = cursorToEvent(cursor);
			events.add(event);
			cursor.moveToNext();
		}
		
		cursor.close();
		return events;
	}
	
	public Event cursorToEvent(Cursor cursor){
		
		// Converts a cursor to an Event object 
		
		Event event = new Event();
		event.setId(cursor.getLong(0));
		event.setEventName(cursor.getString(1));
		event.setEventDate(cursor.getString(2));
		event.setEventTime(cursor.getString(3));
		event.setEventPlace(cursor.getString(4));
		return event;
	}

}
