package com.example.disneylander;

public class RideModel 
{
	public boolean m_isToDo;
	public int m_rideId;
	public String m_rideName;
	
	//Model for the a ride to be used in the database
	public RideModel(boolean isToDo, int rideId, String rideName)
	{
		m_isToDo = isToDo;
		m_rideId = rideId;
		m_rideName = rideName;
	}
}
