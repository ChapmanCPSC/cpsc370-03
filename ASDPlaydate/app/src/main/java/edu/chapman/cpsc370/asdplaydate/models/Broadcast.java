package edu.chapman.cpsc370.asdplaydate.models;

import com.parse.ParseClassName;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.util.Date;

import edu.chapman.cpsc370.asdplaydate.helpers.DateHelpers;

@ParseClassName("Broadcasts")
public class Broadcast extends ParseObject
{
    public static final String ATTR_BROADCASTER = "broadcaster";
    public static final String ATTR_LOCATION = "location";
    public static final String ATTR_MESSAGE = "message";
    public static final String ATTR_EXPIRE_DATE = "expire_date";

    public Broadcast(){}

    public Broadcast(ASDPlaydateUser broadcaster, ParseGeoPoint location, String message, DateTime expireDate)
    {
        this(broadcaster,location,expireDate);
        setMessage(message);
    }

    public Broadcast(ASDPlaydateUser broadcaster, ParseGeoPoint location, DateTime expireDate)
    {
        setBroadcaster(broadcaster);
        setLocation(location);
        setExpireDate(expireDate);
    }

    public void setBroadcaster(ASDPlaydateUser broadcaster)
    {
        put(ATTR_BROADCASTER, broadcaster);
    }

    public ASDPlaydateUser getBroadcaster()
    {
        return (ASDPlaydateUser) getParseUser(ATTR_BROADCASTER);
    }

    public void setLocation(ParseGeoPoint location)
    {
        put(ATTR_LOCATION, location);
    }

    public ParseGeoPoint getLocation()
    {
        return getParseGeoPoint(ATTR_LOCATION);
    }

    public String getMessage()
    {
        return getString(ATTR_MESSAGE);
    }

    public void setMessage(String message)
    {
        put(ATTR_MESSAGE, message);
    }

    public void setExpireDate(DateTime expireDate)
    {
        put(ATTR_EXPIRE_DATE, DateHelpers.UTCDate(expireDate));
    }

    public DateTime getExpireDate()
    {
        return DateHelpers.LocalDateTime(getDate(ATTR_EXPIRE_DATE));
    }
}
