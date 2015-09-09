package com.example.disneylander.db;

import java.util.ArrayList;
import java.util.List;

import com.example.disneylander.RideLogModel;
import com.example.disneylander.RideModel;
import com.example.disneylander.db.DbContract.RideEntry;
import com.example.disneylander.db.DbContract.RideLogEntry;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DbHelpers {

	public static SQLiteDatabase Db(Context c) {
		DbContract contract = new DbContract(c);
		return contract.getWritableDatabase();
	}

	public static void InsertRide(RideModel ride, Context c) {//insert a ride into the db
		SQLiteDatabase db = Db(c);// always start w/ this

		ContentValues cv = RideEntry.MakeContentValues(ride);
		db.insert(RideEntry.TABLE_NAME, null, cv);

		db.close();// always end w/ this
	}

	public static RideModel GetRide(int id, Context c) {//get a ride model from a ride id
		SQLiteDatabase db = Db(c);// always start w/ this

		String selection = RideEntry.COLUMN_ID + " LIKE ?";
		String[] selectionArgs = { String.valueOf(id) };

		Cursor cur = db.query(RideEntry.TABLE_NAME, RideEntry.PROJECTION, selection,
				selectionArgs, null, null, null);
		
		cur.moveToNext();

		RideModel rm = RideEntry.MakeModel(cur);
		
		cur.close();
		db.close();// always end w/ this
		
		return rm;
	}
	
	public static List<RideModel> GetRides(Context c)//get all rides in a list
	{
		List<RideModel> rideList = new ArrayList<RideModel>(); 
		
		SQLiteDatabase db = Db(c);// always start w/ this
		Cursor cur = db.query(RideEntry.TABLE_NAME, RideEntry.PROJECTION, null,
				null, null, null, null);
		
		while (cur.moveToNext())
		{
			RideModel rm = RideEntry.MakeModel(cur);
			rideList.add(rm);
		}
				
		cur.close();
		db.close();// always end w/ this
		
		return rideList;
	}
	
	public static int GetRiddenCount(RideModel rm, Context c)//get the ridden count of a ride
	{
		SQLiteDatabase db = Db(c);// always start w/ this
		
		String selection = RideLogEntry.COLUMN_RIDE_ID + " LIKE ?";
		String[] selectionArgs = { String.valueOf(rm.m_rideId) };

		Cursor cur = db.query(RideLogEntry.TABLE_NAME, RideLogEntry.PROJECTION, selection,
				selectionArgs, null, null, null);
		
		int riddenCount = cur.getCount();
		
		cur.close();
		db.close();// always end w/ this
		
		return riddenCount;
	}
	
	public static void UpdateRide(RideModel rm, Context c)//update a ride
	{
		SQLiteDatabase db = Db(c);// always start w/ this
		
		String selection = RideEntry.COLUMN_ID + " LIKE ?";
		String[] selectionArgs = { String.valueOf(rm.m_rideId) };
		
		ContentValues cv = RideEntry.MakeContentValues(rm);
		
		db.update(RideEntry.TABLE_NAME, cv, selection, selectionArgs);
		
		db.close();// always end w/ this
	}
	
	public static void AddToRideLog(RideModel rm, Context c)//add a ride to the ride log
	{
		RideLogModel rlm = new RideLogModel(rm.m_rideId);
		
		SQLiteDatabase db = Db(c);// always start w/ this
		
		ContentValues cv = RideLogEntry.MakeContentValues(rlm);
		db.insert(RideLogEntry.TABLE_NAME, null, cv);
		
		db.close();// always end w/ this
	}
	
	public static String GetMostRiddenRide(Context c)//Get the most ridden ride in the db
	{
		SQLiteDatabase db = Db(c);// always start w/ this
		
		List<RideModel> rideList = DbHelpers.GetRides(c);
		RideModel mostRiddenRideModel = new RideModel(false, 0, "temp");
		int mostRiddenCounter = 0;
		
		for (int i = 0; i < rideList.size(); i++) {
			RideModel tempRideModel = rideList.get(i);
			int tempRideCount = DbHelpers.GetRiddenCount(tempRideModel, c);
			
			if (i==0) {
				mostRiddenRideModel = rideList.get(i);
				mostRiddenCounter =  DbHelpers.GetRiddenCount(mostRiddenRideModel, c);
			}else if (tempRideCount > mostRiddenCounter) {
				mostRiddenCounter = tempRideCount;
				mostRiddenRideModel = tempRideModel;
			}
			
		}
		
		db.close();// always end w/ this
		return mostRiddenRideModel.m_rideName;
	}
	
	public static String GetTotalRiddenCount(Context c)//get the total times ridden any ride
	{
		List<RideModel> rideList = new ArrayList<RideModel>(); 
		
		SQLiteDatabase db = Db(c);// always start w/ this
		Cursor cur = db.query(RideEntry.TABLE_NAME, RideEntry.PROJECTION, null,
				null, null, null, null);
		
		while (cur.moveToNext())
		{
			RideModel rm = RideEntry.MakeModel(cur);
			rideList.add(rm);
		}
				
		cur.close();
		db.close();// always end w/ this
		
		int ridesRidden = 0;
		for (int i = 0; i < rideList.size(); i++) {
			RideModel rm = rideList.get(i);
			ridesRidden += DbHelpers.GetRiddenCount(rm, c);
		}
		
		return String.valueOf(ridesRidden);
	}
	
	public static ArrayList<RideModel> getToDoList(Context c)//get the current todolist
	{
		ArrayList<RideModel> todoList = new ArrayList<RideModel>();
		
		SQLiteDatabase db = Db(c);// always start w/ this
		
		Cursor cur = db.query(RideEntry.TABLE_NAME, RideEntry.PROJECTION, null,
				null, null, null, null);
		
		while (cur.moveToNext())
		{
			RideModel rm = RideEntry.MakeModel(cur);
			if (rm.m_isToDo) {
				todoList.add(rm);
			}
		}
				
		cur.close();
		db.close();// always end w/ this
		
		return todoList;
	}
	
}
