/*
 * Martin Caestecker
 * CPSC 370
 * Fluger
 * Version 1.0
 * 12/17/2014
 */

package com.chapman.yourmoney.adapters;

/*
 * implementation found at http://www.tutorialspoint.com/android/android_sqlite_database.htm
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class TransactionDbHelper extends SQLiteOpenHelper {

	//define database and table information
	public static final String DATABASE_NAME = "yourmoney.db";
	
	   public static final String TRANS_TABLE_NAME = "transactions";
	   public static final String TRANS_COLUMN_ID = "_id";
	   public static final String TRANS_COLUMN_DATE = "transDate";
	   public static final String TRANS_COLUMN_DESC = "transDescription";
	   public static final String TRANS_COLUMN_AMT = "transAmount";
	   public static final String TRANS_COLUMN_TYPE = "transType";
	   public static final String TRANS_COLUMN_CAT = "transCategory";
	   public static final String TRANS_COLUMN_EXPORT = "transExported";
	   
	   public static final String CAT_TABLE_NAME = "categories";
	   public static final String CAT_COLUMN_ID = "_id";
	   public static final String CAT_COLUMN_NAME = "categoryName";
	   public static final String CAT_COLUMN_TYPE = "categoryType";
	   public static final String CAT_COLUMN_EXPORT = "categoryExported";
	   
	   static String TAG = "DbHelper";

	   public TransactionDbHelper(Context context)
	   {
	      super(context, DATABASE_NAME , null, 1);
	      Log.v(TAG, "TransactionDbHelper instantiated");
	   }

	   @Override
	   public void onCreate(SQLiteDatabase db) {
	      //create tables
	      db.execSQL(
	      "create table " + TRANS_TABLE_NAME +
	      " (" + TRANS_COLUMN_ID + " integer primary key, " + TRANS_COLUMN_DATE + " text, " + TRANS_COLUMN_DESC + " text, " +
	    		  TRANS_COLUMN_AMT + " double, " + TRANS_COLUMN_TYPE + " integer, " + TRANS_COLUMN_CAT + " string, " + 
	    		  TRANS_COLUMN_EXPORT + " integer)"
	      );
	      
	      db.execSQL(
	      "create table " + CAT_TABLE_NAME + 
	      " (" + CAT_COLUMN_ID + " integer primary key, " + CAT_COLUMN_NAME + " text, " + CAT_COLUMN_TYPE + " integer, " +
	    		  CAT_COLUMN_EXPORT + " integer)"
	      );
	   }

	   @Override
	   public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	      //on upgrade, drop tables
		  //will be updated in future release
	      db.execSQL("DROP TABLE IF EXISTS " + TRANS_TABLE_NAME);
	      db.execSQL("DROP TABLE IF EXISTS " + CAT_TABLE_NAME);
	      onCreate(db);
	   }
	   
	   @Override
	public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		//on downgrade, drop tables
		//will be updated in future release
		super.onDowngrade(db, oldVersion, newVersion);
		db.execSQL("DROP TABLE IF EXISTS " + TRANS_TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS " + CAT_TABLE_NAME);
	    onCreate(db);
	}
	   
	   @Override
	public void onOpen(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		super.onOpen(db);
	}
	   
	   public boolean insertTransaction  (String date, String desc, double amt, String cat, int type)
	   {
		  SQLiteDatabase db = this.getWritableDatabase();
	      ContentValues contentValues = new ContentValues();

	      contentValues.put(TRANS_COLUMN_DATE, date);
	      contentValues.put(TRANS_COLUMN_DESC, desc);
	      contentValues.put(TRANS_COLUMN_AMT, amt);
	      contentValues.put(TRANS_COLUMN_TYPE, type);
	      contentValues.put(TRANS_COLUMN_CAT, cat);
	      contentValues.put(TRANS_COLUMN_EXPORT, 0);

	      db.insert(TRANS_TABLE_NAME, null, contentValues);
	      Log.v(TAG, "Transaction Inserted!");
	      return true;
	   }
	   
	   public boolean insertCategory (String name, int type){
		   SQLiteDatabase db = this.getWritableDatabase();
		   ContentValues contentValues = new ContentValues();
		   
		   contentValues.put(CAT_COLUMN_NAME, name);
		   contentValues.put(CAT_COLUMN_TYPE, type);
		   contentValues.put(CAT_COLUMN_EXPORT, 0);
		   
		   db.insert(CAT_TABLE_NAME, null, contentValues);
		   return true;
	   }
	   
	   public String[] getAllCategories(int type){
		   SQLiteDatabase db = this.getReadableDatabase();
		   Cursor res =  db.rawQuery( "select " + CAT_COLUMN_NAME + " from " + CAT_TABLE_NAME + " where " + CAT_COLUMN_TYPE + " ="+type+" order by " + CAT_COLUMN_NAME + " asc", null );
		   String[] allCats = new String[res.getCount()];
		   
		   res.moveToFirst();
		   
		   for(int i = 0; i < res.getCount(); i++){
			   allCats[i] = res.getString(res.getColumnIndex(CAT_COLUMN_NAME));
			   Log.v(TAG, res.getString(res.getColumnIndex(CAT_COLUMN_NAME)) + " added to array");
			   res.moveToNext();
		   }
		   return allCats;
	   }
	   
	   public Cursor getTransaction(int id){
		  SQLiteDatabase db = this.getReadableDatabase();
	      Cursor res =  db.rawQuery( "select * from " + TRANS_TABLE_NAME + " where " + TRANS_COLUMN_ID + " ="+id+"", null );
	      return res;
	   }
	   
	   public Cursor getAllTransactions(int type){
		   SQLiteDatabase db = this.getReadableDatabase();
		   Cursor res = db.rawQuery("SELECT * FROM " + TRANS_TABLE_NAME + " WHERE " + TRANS_COLUMN_TYPE + " = " + type + " ORDER BY " + TRANS_COLUMN_DATE + " DESC", null);
		   return res;
	   }
	   
	   public Cursor getTransactionsFromRange(int type, String sqlStartDate, String sqlEndDate){
		   SQLiteDatabase db = this.getReadableDatabase();
		   Cursor res = db.rawQuery("SELECT * FROM " + TRANS_TABLE_NAME + " WHERE " + TRANS_COLUMN_TYPE + " = " + type + " AND " + TRANS_COLUMN_DATE + " >= date('" + sqlStartDate + "') AND " + TRANS_COLUMN_DATE + " <= date('" + sqlEndDate + "') ORDER BY " + TRANS_COLUMN_DATE + " DESC", null);
		   return res;
	   }
	   
	   public double getTotal(int transType, int year){
		   Log.v(TAG, "entered getTotal()");
		   SQLiteDatabase db = this.getReadableDatabase();
		   Cursor res = null;
		   
		   String sqlJan1 = Integer.toString(year) + "-01-01";
		   
		   //get transactions
		   res = db.rawQuery( "select * from " + TRANS_TABLE_NAME + " where " + TRANS_COLUMN_TYPE + " ="+ transType + " and " + TRANS_COLUMN_DATE + " >= date('" + sqlJan1 + "')", null);
		   
		   res.moveToFirst();
		   double total = 0.0;
		   
		   //total all transaction amounts and return value
		   for(int i = 0; i < res.getCount(); i++){
			   double amt = res.getDouble(res.getColumnIndex(TRANS_COLUMN_AMT));
			   total += amt;
			   res.moveToNext();
		   }
		   return total;
	   }
	   
	   public int numberOfTransactions(){
		  SQLiteDatabase db = this.getReadableDatabase();
	      int numRows = (int) DatabaseUtils.queryNumEntries(db, TRANS_TABLE_NAME);
	      return numRows;
	   }
	   
	   public int numberOfCategories(){
		  SQLiteDatabase db = this.getReadableDatabase();
	      int numRows = (int) DatabaseUtils.queryNumEntries(db, CAT_TABLE_NAME);
	      return numRows;
		   }
	   
	   public boolean updateTransaction (Integer id, String date, String desc, double amt, String cat, int type)
	   {
		  SQLiteDatabase db = this.getWritableDatabase();
	      ContentValues contentValues = new ContentValues();
	      contentValues.put(TRANS_COLUMN_DATE, date);
	      contentValues.put(TRANS_COLUMN_DESC, desc);
	      contentValues.put(TRANS_COLUMN_AMT, amt);
	      contentValues.put(TRANS_COLUMN_CAT, cat);
	      contentValues.put(TRANS_COLUMN_TYPE, type);
	      db.update(TRANS_TABLE_NAME, contentValues, TRANS_COLUMN_ID + " = ? ", new String[] { Integer.toString(id) } );
	      return true;
	   }
	   
	   public boolean updateCategory (Integer id, String name, int type)
	   {
	      SQLiteDatabase db = this.getWritableDatabase();
	      ContentValues contentValues = new ContentValues();
	      contentValues.put(CAT_COLUMN_NAME, name);
	      contentValues.put(CAT_COLUMN_TYPE, type);
	      db.update(CAT_TABLE_NAME, contentValues, CAT_COLUMN_ID + " = ? ", new String[] { Integer.toString(id) } );
	      return true;
	      
	   }

	   public Integer deleteTransaction (Integer id)
	   {
	      SQLiteDatabase db = this.getWritableDatabase();
	      return db.delete(TRANS_TABLE_NAME, TRANS_COLUMN_ID + " = ? ", new String[] { Integer.toString(id) });
	   }
	   
	   public Integer deleteCategory (Integer id)
	   {
	      SQLiteDatabase db = this.getWritableDatabase();
	      return db.delete(CAT_TABLE_NAME, CAT_COLUMN_ID + " = ? ", new String[] { Integer.toString(id) });
	   }
	   
	   public Integer deleteCategory (String name)
	   {
	      SQLiteDatabase db = this.getWritableDatabase();
	      return db.delete(CAT_TABLE_NAME, CAT_COLUMN_NAME + " = ? ", new String[] { name });
	   }
	  
	}