package com.chapman.hawaiiclub;

public class Event {
	
	private long id;
	private String eventName, eventDate, eventTime, eventPlace;
	
	public Event(){
		
	}
	
	public Event(long id, String eventName, String eventDate, String eventTime, String eventPlace){
		this.id = id;
		this.eventName = eventName;
		this.eventDate = eventDate;
		this.eventTime = eventTime;
		this.eventPlace = eventPlace;
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getEventName() {
		return eventName;
	}
	public void setEventName(String eventName) {
		this.eventName = eventName;
	}
	public String getEventDate() {
		return eventDate;
	}
	public void setEventDate(String eventDate) {
		this.eventDate = eventDate;
	}
	public String getEventTime() {
		return eventTime;
	}
	public void setEventTime(String eventTime) {
		this.eventTime = eventTime;
	}
	public String getEventPlace() {
		return eventPlace;
	}
	public void setEventPlace(String eventPlace) {
		this.eventPlace = eventPlace;
	}
	public String toString()
	{
		return eventName.toUpperCase();
	}
	

}
