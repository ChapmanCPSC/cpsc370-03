package edu.chapman.cpsc370.asdplaydate.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import org.joda.time.DateTime;

import edu.chapman.cpsc370.asdplaydate.helpers.DateHelpers;

@ParseClassName("Conversations")
public class Conversation extends ParseObject
{
    public static final String ATTR_INITIATOR = "initiator";
    public static final String ATTR_RECEIVER = "receiver";
    public static final String ATTR_STATUS = "status";
    public static final String ATTR_EXPIRE_DATE = "expire_date";

    public Conversation(){}

    public Conversation(ASDPlaydateUser initiator, ASDPlaydateUser receiver, Status status, DateTime expireDate)
    {
        setInitiator(initiator);
        setReceiver(receiver);
        setStatus(status);
        setExpireDate(expireDate);
    }

    public void setInitiator(ASDPlaydateUser initiator)
    {
        put(ATTR_INITIATOR, initiator);
    }

    public ASDPlaydateUser getInitiator()
    {
        return (ASDPlaydateUser) getParseUser(ATTR_INITIATOR);
    }

    public void setReceiver(ASDPlaydateUser receiver)
    {
        put(ATTR_RECEIVER, receiver);
    }

    public ASDPlaydateUser getReceiver()
    {
        return (ASDPlaydateUser) getParseUser(ATTR_RECEIVER);
    }

    public Status getStatus()
    {
        String status = getString(ATTR_STATUS);
        return Status.valueOf(status);
    }

    public void setStatus(Status status)
    {
        put(ATTR_STATUS, status.name());
    }

    public void setExpireDate(DateTime expireDate)
    {
        put(ATTR_EXPIRE_DATE, DateHelpers.UTCDate(expireDate));
    }

    public DateTime getExpireDate()
    {
        return DateHelpers.LocalDateTime(getDate(ATTR_EXPIRE_DATE));
    }

    public enum Status
    {
        PENDING, ACCEPTED, DENIED;
    }
}
