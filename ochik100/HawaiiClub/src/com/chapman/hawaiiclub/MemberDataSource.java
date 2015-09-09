package com.chapman.hawaiiclub;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class MemberDataSource {
	
	static Cursor cursor;
	
	static CharSequence[] memberNames;
	List<String> memberList;
	List<Long> memberIdList;

	private SQLiteDatabase db;
	private DatabaseHelper dbHelper;
	
	private String[] allMembersColumns = { DatabaseHelper.TABLE_ID,
	      DatabaseHelper.KEY_MEMBER_NAME};
	private String[] allMembersPhoneColumns = { DatabaseHelper.TABLE_MEMBERSPHONE_ID, 
			DatabaseHelper.KEY_MEMBERSPHONE_MEMBERID, DatabaseHelper.KEY_MEMBERSPHONE_PHONENUMBER };
	private String[] allMembersInfoColumns = { DatabaseHelper.TABLE_MEMBERSINFO_ID,
			DatabaseHelper.KEY_MEMBERSINFO_MEMBERSID, DatabaseHelper.KEY_MEMBERSINFO_EMAIL, 
			DatabaseHelper.KEY_MEMBERSINFO_STUDENTID, DatabaseHelper.KEY_MEMBERSINFO_MAJOR };
	
	
	long membersID, membersPhoneID, membersInfoID;
	
	public static List<Member> members = new ArrayList<Member>();
	
	public MemberDataSource(Context context) {
	    dbHelper = new DatabaseHelper(context);
	  }

	public void open() throws SQLException {
		db = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}
	
	
	public Member addMember(String name, String phoneNumber, String email, String studentID, String major){
		ContentValues values = new ContentValues();
		values.put(DatabaseHelper.KEY_MEMBER_NAME, name);
		
		membersID = db.insert(DatabaseHelper.TABLE_MEMBERS, null,
		        values);
		
		
		System.out.println("MDS addMember membersID = " + membersID);
		
		Cursor cursor = db.query(DatabaseHelper.TABLE_MEMBERS,
		        allMembersColumns, 
		        DatabaseHelper.TABLE_ID + " = " + membersID, null,
		        null, null, null);
		cursor.moveToFirst();
		Member newMember = cursorToMember(cursor);
		cursor.close();
		
		addMembersPhone(membersID, phoneNumber);
		addMembersInfo(membersID, email, studentID, major);
		
		System.out.println("Member added");
		return newMember;
	}
	
	public void updateMember(long memberId, String name, String phone, String email, String studentID, String major){
		ContentValues values = new ContentValues();
		values.put(DatabaseHelper.KEY_MEMBER_NAME, name);
		
		db.update(DatabaseHelper.TABLE_MEMBERS, values, DatabaseHelper.TABLE_ID + " = " + memberId, null);
		
		updateMembersPhone(memberId, phone);
		updateMembersInfo(memberId, email, studentID, major);
		
	}
	
	public Member getMember(long memberID){
		Cursor cursor = db.query(DatabaseHelper.TABLE_MEMBERS, allMembersColumns, 
				DatabaseHelper.TABLE_ID + " = " + memberID, 
				null, null, null, null);
		if(cursor!= null)
			cursor.moveToFirst();
		
		Member member = new Member(cursor.getLong(0), cursor.getString(1));
		cursor.close();
		return member;
		
	}
	
	private Member cursorToMember(Cursor cursor) {
		
		// Converts a cursor object to a member object
		
	    Member member = new Member();
	    member.setId(cursor.getLong(0));
	    member.setName(cursor.getString(1));
	
	    return member;
	}
	
	
	public List<Member> getAllMembers() {

		// Method that returns a list of all the members in the club (of type Member)
		
	    cursor = db.query(DatabaseHelper.TABLE_MEMBERS,
	        allMembersColumns, null, null, null, null, DatabaseHelper.KEY_MEMBER_NAME + " COLLATE NOCASE");

	    cursor.moveToFirst();
	    while (!cursor.isAfterLast()) {
	      Member member = cursorToMember(cursor);
	      members.add(member);
	      cursor.moveToNext();
	    }
	    // make sure to close the cursor
	    cursor.close();
	    return members;
        
	    
    }

	public Cursor memberCursor(){
		
		Cursor cursor = db.query(DatabaseHelper.TABLE_MEMBERS,
		        new String[] {DatabaseHelper.TABLE_ID, DatabaseHelper.KEY_MEMBER_NAME}, null, null, null, null, DatabaseHelper.KEY_MEMBER_NAME + " COLLATE NOCASE");
		
		return cursor;
	}
	
	public void clickedOkay(ArrayList<Long> items, long eventId, String event, boolean replace){
		
		for(long i : items){
			System.out.println("items item: " + i);
		}
		Long[] itemsArray = items.toArray(new Long[items.size()]);
		
		// Obtain all member ids and convert to an array
		List<Long> ids = getAllMemberIds();
		Long[] membersarray = ids.toArray(new Long[ids.size()]);
		
		// If the event name contains a space in it
		CharSequence space = " ";
		if(event.contains(space)){
			event = event.replace(space, "_");
		}
		
		// Creating the attributes of the table
		final String TABLE_EVENT_NAME = "TABLE_" + event.toUpperCase();
		System.out.println("Table name: " + TABLE_EVENT_NAME);
		final String TABLE_ID = "TABLE_" + event.toUpperCase() + "_ID";
		System.out.println("Table id: " + TABLE_ID);
		final String KEY_EVENT_ID = event.toUpperCase() + "_" + event.toUpperCase() + "ID";
		System.out.println("Table column: " + KEY_EVENT_ID);
		final String KEY_EVENT_MEMBERID = event.toUpperCase() + "_MEMBERID";
		System.out.println("Table column: " + KEY_EVENT_MEMBERID);
		
		if(replace){
			
			// If updating list of members attending, delete all from table
			deleteFromTable(eventId, event);
			// Recreate table 
			final String CREATE_NEW_EVENT_ATTENDING_TABLE = DatabaseHelper.createAttendingEvent(event);
			db.execSQL(CREATE_NEW_EVENT_ATTENDING_TABLE);
			
			// Inserts into the table
			if(items.size() != 0){
				for(int i = 0; i < itemsArray.length; i++){
					//Long memberId = itemsArray[i];
					System.out.println("itemsArray[" + i+ "] = " + itemsArray[i]);
					long memberId = membersarray[itemsArray[i].intValue()];
					
					if(exists(event, memberId)){
						String query = "INSERT INTO " + TABLE_EVENT_NAME + 
								"(" + TABLE_ID + "," + KEY_EVENT_ID + "," + KEY_EVENT_MEMBERID + ")"
								+ " VALUES " + "((SELECT " + TABLE_ID + " FROM " + TABLE_EVENT_NAME + " WHERE " 
								+ KEY_EVENT_MEMBERID + " = " + memberId + "), " 
								+ eventId + ", " + memberId + ");";
						db.rawQuery(query, null);
					} else {
						ContentValues values = new ContentValues();
						values.put(KEY_EVENT_ID, eventId);
						values.put(KEY_EVENT_MEMBERID, memberId);
						long insertid = db.insert(TABLE_EVENT_NAME, null, values);
					}
					
				}
			}
			
		} else {
			
			// Creates a table for the members attending an event
			final String CREATE_EVENT_ATTENDING_TABLE = DatabaseHelper.createAttendingEvent(event);
			db.execSQL(CREATE_EVENT_ATTENDING_TABLE);
			
			if(items.size() != 0){
				for(int i = 0; i < itemsArray.length; i++){
					
					// Obtains the memberId of each person who is attending an event 
					long memberId = membersarray[itemsArray[i].intValue()];
					
					// Inserts into database 
					ContentValues values = new ContentValues();
					values.put(KEY_EVENT_ID, eventId);
					values.put(KEY_EVENT_MEMBERID, memberId);
					long insertid = db.insert(TABLE_EVENT_NAME, null, values);
					
					Cursor cursor = db.query(TABLE_EVENT_NAME, new String[] {KEY_EVENT_ID,  KEY_EVENT_MEMBERID}, TABLE_ID + " = " + insertid, null, null, null, null);
					cursor.moveToFirst();
					cursor.close();
					
				}
			}
			
		}
		
		
	}
	
	

	public void deleteFromTable(long eventId, String event){
		
		// Use this method to delete from a table using an event id 
		
		final String TABLE_EVENT_NAME = "TABLE_" + event.toUpperCase();
		final String KEY_EVENT_ID = event.toUpperCase() + "_" + event.toUpperCase() + "ID";
		db.delete(TABLE_EVENT_NAME, KEY_EVENT_ID + " = " + eventId, null);
	}
	
	public boolean exists(String event, Long memberId){
		
		// Checks if the member id parameter exists in the table 
		
		final String TABLE_EVENT_NAME = "TABLE_" + event.toUpperCase();
		final String KEY_EVENT_MEMBERID = event.toUpperCase() + "_MEMBERID";
		String query = "SELECT * FROM " + TABLE_EVENT_NAME + " WHERE " + KEY_EVENT_MEMBERID 
				+ " = " + memberId + ";";
		Cursor cursor = db.rawQuery(query, null);
		if(cursor.getCount() <=0){
			return false;
		} else {
			return true;
		}
	}
	
	public void dropMemberAttending(String event){
		
		// Deletes table
		
		final String TABLE_EVENT_NAME = "TABLE_" + event.toUpperCase();
		
		String query = "DROP TABLE IF EXISTS " + DatabaseHelper.DATABASE_NAME + "." + TABLE_EVENT_NAME + ";";
		db.rawQuery(query, null);
		System.out.println("dropped table " + TABLE_EVENT_NAME);
	}
	
	public void countAttending(String event){
		final String TABLE_EVENT_NAME = "TABLE_" + event.toUpperCase();
		final String TABLE_ID = "TABLE_" + event.toUpperCase() + "_ID";
		final String KEY_EVENT_MEMBERID = event.toUpperCase() + "_MEMBERID";
		
		Cursor cursor = db.query(TABLE_EVENT_NAME, new String[]{KEY_EVENT_MEMBERID}, null, null, null, null, null);
		System.out.println(cursor.getCount() + " is the count");
	}
	
	public List<Long> getAllMemberIds(){
		
		// Method that returns a list of MemberIds of all the members in the club, sorted in alphabetic order of the member's name
		
		List<Long> ids = new ArrayList<Long>();
		
		Cursor cursor = db.query(DatabaseHelper.TABLE_MEMBERS, new String[] {DatabaseHelper.TABLE_ID}, null, null, null, null, DatabaseHelper.KEY_MEMBER_NAME + " COLLATE NOCASE");
		
		cursor.moveToFirst();
		while(!cursor.isAfterLast()){
			ids.add(cursor.getLong(0));
			cursor.moveToNext();
		}
		cursor.close();
		return ids;
	}
		
	public List<String> getAllMembersArray() {
			
		// Method that returns a list of names of all the members in the club 
		
		memberList = new ArrayList<String>();
		memberIdList = new ArrayList<Long>();

	    cursor = db.query(DatabaseHelper.TABLE_MEMBERS,
	        allMembersColumns, null, null, null, null, DatabaseHelper.KEY_MEMBER_NAME + " COLLATE NOCASE");

	    cursor.moveToFirst();
	    while (!cursor.isAfterLast()) {
	      Member member = cursorToMember(cursor);
	      memberList.add(member.getName());
	      memberIdList.add(member.getId());
	      cursor.moveToNext();
	    }
	    
	    cursor.close();
	    return memberList;
	        
	}
	
	
	
	public ArrayList<String> getNamesAttendingArray(String event){
		
		// Method that returns an ArrayList of names attending the event provided in the parameter
		
		ArrayList<String> attending = new ArrayList<String>();
		
		final String TABLE_EVENT_NAME = "TABLE_" + event.toUpperCase();
		final String KEY_EVENT_MEMBERID = event.toUpperCase() + "_MEMBERID";
		
		String query = "SELECT " + DatabaseHelper.KEY_MEMBER_NAME + " FROM " + TABLE_EVENT_NAME + " INNER JOIN " 
				+ DatabaseHelper.TABLE_MEMBERS + " ON " + DatabaseHelper.TABLE_ID + " = " + KEY_EVENT_MEMBERID + ";";
		Cursor cursor = db.rawQuery(query, null);
		cursor.moveToFirst();
		while(!cursor.isAfterLast()){
			attending.add(cursor.getString(0));
			cursor.moveToNext();
		}
		cursor.close();
		
		//CharSequence[] cs = attending.toArray(new CharSequence[attending.size()]);
		return attending;
	}
	
	public ArrayList<String> getEmailsAttending(String event){
		
		// Returns a list of all the emails of the members attending an event 
		
		ArrayList<String> emails = new ArrayList<String>();
		
		final String TABLE_EVENT_NAME = "TABLE_" + event.toUpperCase();
		final String KEY_EVENT_MEMBERID = event.toUpperCase() + "_MEMBERID";
		
		String query = "SELECT " + DatabaseHelper.KEY_MEMBERSINFO_EMAIL + " FROM " + TABLE_EVENT_NAME + " INNER JOIN "
				+ DatabaseHelper.TABLE_MEMBERSINFO + " ON " + DatabaseHelper.KEY_MEMBERSINFO_MEMBERSID + " = " 
				+ KEY_EVENT_MEMBERID + ";";
		
		Cursor cursor = db.rawQuery(query, null);
		cursor.moveToFirst();
		while(!cursor.isAfterLast()){
			emails.add(cursor.getString(0));
			cursor.moveToNext();
		}
		cursor.close();
		
		System.out.println("email count = " + cursor.getCount());
		return emails;
	}
	
	public void getNumbersAttending(String event){
		ArrayList<String> numbers = new ArrayList<String>();
		
		final String TABLE_EVENT_NAME = "TABLE_" + event.toUpperCase();
		final String KEY_EVENT_MEMBERID = event.toUpperCase() + "_MEMBERID";
		
		String query = "SELECT " + DatabaseHelper.KEY_MEMBERSPHONE_PHONENUMBER + " FROM " + TABLE_EVENT_NAME 
				+ " INNER JOIN " + DatabaseHelper.TABLE_MEMBERSPHONE + " ON " + DatabaseHelper.KEY_MEMBERSPHONE_MEMBERID 
				+ " = " + KEY_EVENT_MEMBERID + ";";
		
		Cursor cursor = db.rawQuery(query, null);
		cursor.moveToFirst();
		while(!cursor.isAfterLast()){
			numbers.add(cursor.getString(0));
			cursor.moveToNext();
		}
		cursor.close();
	}
	
	public void deleteMember(Member member) {
		
		// Deletes member from the database 
		
	    long id = member.getId();
	    System.out.println("Member deleted with id: " + id);
	    db.delete(DatabaseHelper.TABLE_MEMBERS, DatabaseHelper.TABLE_ID
	        + " = " + id, null);
	    deleteMembersPhone(id);
	    deleteMembersInfo(id);
	}
	
	
	public MembersPhone addMembersPhone(Long memberid, String phoneNumber){
		
		ContentValues values = new ContentValues();
		values.put(DatabaseHelper.KEY_MEMBERSPHONE_MEMBERID, memberid);
		values.put(DatabaseHelper.KEY_MEMBERSPHONE_PHONENUMBER, phoneNumber);
		
		membersPhoneID = db.insert(DatabaseHelper.TABLE_MEMBERSPHONE, null, values);
		
		System.out.println("add memberphone cursor");
		Cursor cursor = db.query(DatabaseHelper.TABLE_MEMBERSPHONE, allMembersPhoneColumns, DatabaseHelper.TABLE_MEMBERSPHONE_ID + " = " + membersPhoneID,
				null, null, null, null);
		cursor.moveToFirst();
		MembersPhone membersPhone = cursorToMembersPhone(cursor);
		System.out.println("adding member id = " + cursor.getLong(1));
		cursor.close();
		return membersPhone;
	}
	
	public void updateMembersPhone(Long memberId, String phoneNumber){
		ContentValues values = new ContentValues();
		values.put(DatabaseHelper.KEY_MEMBERSPHONE_MEMBERID, memberId);
		values.put(DatabaseHelper.KEY_MEMBERSPHONE_PHONENUMBER, phoneNumber);
		
		db.update(DatabaseHelper.TABLE_MEMBERSPHONE, values, DatabaseHelper.KEY_MEMBERSPHONE_MEMBERID + " = " + memberId, null);
	}
	
	public MembersPhone getMembersPhone(long membersID){
		
		Cursor cursor = db.query(DatabaseHelper.TABLE_MEMBERSPHONE, allMembersPhoneColumns, 
				DatabaseHelper.KEY_MEMBERSPHONE_MEMBERID + " = " + membersID,
				null, null, null, null);
		if(cursor != null)
			cursor.moveToFirst();
		
		MembersPhone phone = new MembersPhone(cursor.getLong(0), cursor.getLong(1), cursor.getString(2));
		cursor.close();
		return phone;
	}
	
	public MembersPhone cursorToMembersPhone(Cursor cursor){
		
		// Converts a cursor object to a MembersPhone object
		
		MembersPhone membersPhone = new MembersPhone();
		membersPhone.setId(cursor.getLong(0));
		membersPhone.setMemberID(cursor.getLong(1));
		membersPhone.setPhoneNumber(cursor.getString(2));
		return membersPhone;
	}
	
	public void deleteMembersPhone(MembersPhone membersPhone){
		long id = membersPhone.getId();
		db.delete(DatabaseHelper.TABLE_MEMBERSPHONE, DatabaseHelper.TABLE_MEMBERSPHONE_ID + " = " + id, null);
	}
	
	public void deleteMembersPhone(long membersId){
		db.delete(DatabaseHelper.TABLE_MEMBERSPHONE, DatabaseHelper.KEY_MEMBERSPHONE_MEMBERID + " = " + membersId, null);
		System.out.println("memberphone deleted");
	}
	
	public MembersInfo addMembersInfo(long membersID, String email, String studentID, String major){
		ContentValues values = new ContentValues();
		values.put(DatabaseHelper.KEY_MEMBERSINFO_MEMBERSID, membersID);
		values.put(DatabaseHelper.KEY_MEMBERSINFO_EMAIL, email);
		values.put(DatabaseHelper.KEY_MEMBERSINFO_STUDENTID, studentID);
		values.put(DatabaseHelper.KEY_MEMBERSINFO_MAJOR, major);
		
		membersInfoID = db.insert(DatabaseHelper.TABLE_MEMBERSINFO, null, values);
		
		Cursor cursor = db.query(DatabaseHelper.TABLE_MEMBERSINFO, allMembersInfoColumns, 
				DatabaseHelper.TABLE_MEMBERSINFO_ID + " = " + membersInfoID, 
				null, null, null, null);
		cursor.moveToFirst();
		MembersInfo membersInfo = cursorToMembersInfo(cursor);
		cursor.close();
		
		return membersInfo;
	}
	
	public void updateMembersInfo(Long membersID, String email, String studentID, String major){
		
		// Updates member info
		
		ContentValues values = new ContentValues();
		values.put(DatabaseHelper.KEY_MEMBERSINFO_MEMBERSID, membersID);
		values.put(DatabaseHelper.KEY_MEMBERSINFO_EMAIL, email);
		values.put(DatabaseHelper.KEY_MEMBERSINFO_STUDENTID, studentID);
		values.put(DatabaseHelper.KEY_MEMBERSINFO_MAJOR, major);
		
		db.update(DatabaseHelper.TABLE_MEMBERSINFO, values, DatabaseHelper.KEY_MEMBERSINFO_MEMBERSID + " = " + membersID, null);
	}
	
	public MembersInfo getMembersInfo(long membersID){
	
		Cursor cursor = db.query(DatabaseHelper.TABLE_MEMBERSINFO, allMembersInfoColumns, 
				DatabaseHelper.KEY_MEMBERSINFO_MEMBERSID + " = " + membersID,
				null, null, null, null);
		if(cursor != null)
			cursor.moveToFirst();
		
		MembersInfo info = new MembersInfo(cursor.getLong(0), cursor.getLong(1), cursor.getString(2), cursor.getString(3), cursor.getString(4));
		cursor.close();
		return info;
		
	}
	
	public List<MembersInfo> getAllMembersInfo() {
		List<MembersInfo> info = new ArrayList<MembersInfo>();
		Cursor cursor = db.query(DatabaseHelper.TABLE_MEMBERSINFO, allMembersInfoColumns, null, null, null, null, null);
		
		cursor.moveToFirst();
		while(!cursor.isAfterLast()){
			MembersInfo membersInfo = cursorToMembersInfo(cursor);
			info.add(membersInfo);
			cursor.moveToNext();
		}
		
		cursor.close();
		return info;
		
	}
	
	public MembersInfo cursorToMembersInfo(Cursor cursor){
		MembersInfo membersInfo = new MembersInfo();
		membersInfo.setId(cursor.getLong(0));
		membersInfo.setMembersID(cursor.getLong(1));
		membersInfo.setEmail(cursor.getString(2));
		membersInfo.setStudentID(cursor.getString(3));
		membersInfo.setMajor(cursor.getString(4));
		return membersInfo;
	}
	
	public void deleteMembersInfo(long membersId){
		db.delete(DatabaseHelper.TABLE_MEMBERSINFO, DatabaseHelper.KEY_MEMBERSINFO_MEMBERSID + " = " + membersId, null);
		System.out.println("memberinfo deleted");
	}
	

	  
	
}
