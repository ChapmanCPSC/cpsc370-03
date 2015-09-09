package com.chapman.hawaiiclub;

public class Member {
	
	private long id;
	private String name;
	
	public Member(){
		
	}
	
	public Member(long id, String name){
		this.id = id;
		this.name = name;
	}
	
	public void setId(long id){
		this.id = id;
	}
	
	public long getId(){
		return id;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public String getName(){
		return name;
	}
	
	public String toString(){
		return name.toUpperCase();
	}

}
