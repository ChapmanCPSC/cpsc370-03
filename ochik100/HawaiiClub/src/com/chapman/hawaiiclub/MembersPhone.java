package com.chapman.hawaiiclub;

public class MembersPhone {
	
	private long id, memberID;
	private String phoneNumber;
	
	public MembersPhone(){
		
	}
	
	public MembersPhone(long id, long memberID, String phoneNumber){
		this.id = id;
		this.memberID = memberID;
		this.phoneNumber = phoneNumber;
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getMemberID() {
		return memberID;
	}
	public void setMemberID(long memberID) {
		this.memberID = memberID;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	

}
