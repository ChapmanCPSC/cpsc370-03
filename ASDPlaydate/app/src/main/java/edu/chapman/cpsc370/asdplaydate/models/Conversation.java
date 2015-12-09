package edu.chapman.cpsc370.asdplaydate.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.List;

import edu.chapman.cpsc370.asdplaydate.helpers.DateHelpers;

@ParseClassName("Conversations")
public class Conversation extends ParseObject
{
    public static final String ATTR_ID = "objectId";
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

    public static Conversation getConversation(String objectId) throws Exception
    {
        ParseQuery<Conversation> q = new ParseQuery<Conversation>(Conversation.class);
        q.whereEqualTo(Conversation.ATTR_ID, objectId);

        List<Conversation> convos = q.find();

        return (Conversation) convos.get(0);
    }

    public static String printDate(DateTime date)
    {
        DateTimeFormatter dateFormatted = DateTimeFormat.forPattern("MM/dd");
        DateTimeFormatter timeFormatted = DateTimeFormat.forPattern("hh:mma");
        return " " + dateFormatted.print(date) + " at " + timeFormatted.print(date);
    }
}
