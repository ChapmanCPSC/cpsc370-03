package edu.chapman.cpsc370.asdplaydate.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import org.joda.time.DateTime;

import edu.chapman.cpsc370.asdplaydate.helpers.DateHelpers;

@ParseClassName("Messages")
public class Message extends ParseObject
{
    public static String ATTR_CONVERSATION = "conversation";
    public static String ATTR_AUTHOR = "author";
    public static String ATTR_TEXT = "text";
    public static String ATTR_IS_READ = "is_read";
    public static String ATTR_TIMESTAMP = "timestamp";

    public Message(){}

    public Message(Conversation conversation, ASDPlaydateUser author, String text, boolean isRead, DateTime timestamp)
    {
        setConversation(conversation);
        setAuthor(author);
        setText(text);
        setIsRead(isRead);
        setTimestamp(timestamp);
    }

    public void setConversation(Conversation conversation)
    {
        put(ATTR_CONVERSATION, conversation);
    }

    public Conversation getConversation()
    {
        return (Conversation) getParseObject(ATTR_CONVERSATION);
    }

    public void setAuthor(ASDPlaydateUser author)
    {
        put(ATTR_AUTHOR, author);
    }

    public ASDPlaydateUser getAuthor()
    {
        return (ASDPlaydateUser) getParseUser(ATTR_AUTHOR);
    }

    public void setText(String text)
    {
        put(ATTR_TEXT, text);
    }

    public String getText()
    {
        return getString(ATTR_TEXT);
    }

    public void setIsRead(boolean isRead)
    {
        put(ATTR_IS_READ, isRead);
    }

    public boolean isRead()
    {
        return getBoolean(ATTR_IS_READ);
    }

    public void setTimestamp(DateTime timestamp)
    {
        put(ATTR_TIMESTAMP, DateHelpers.UTCDate(timestamp));
    }

    public DateTime getTimestamp()
    {
        return DateHelpers.LocalDateTime(getDate(ATTR_TIMESTAMP));
    }
}
