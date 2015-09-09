package com.chapman.hawaiiclub;

public class MembersInfo {
	
	private long id, membersID;
	private String email, studentID, major;
	
	public MembersInfo(){
		
	}
	
	public MembersInfo(long id, long membersId, String email, String studentID, String major){
		this.id = id;
		this.membersID = membersId;
		this.email = email;
		this.studentID = studentID;
		this.major = major;
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getMembersID() {
		return membersID;
	}
	public void setMembersID(long membersID) {
		this.membersID = membersID;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getStudentID() {
		return studentID;
	}
	public void setStudentID(String studentID) {
		this.studentID = studentID;
	}
	public String getMajor() {
		return major;
	}
	public void setMajor(String major) {
		this.major = major;
	}

}
