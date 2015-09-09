package com.example.disneylander;

public class RideLogModel 
{
	public int m_logId;
	public int m_rideId;
	
	//model for a ride log to be used in the database
	public RideLogModel(int rideId)
	{
		m_rideId = rideId;
	}
}