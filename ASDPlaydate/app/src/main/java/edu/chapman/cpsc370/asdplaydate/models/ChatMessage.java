package edu.chapman.cpsc370.asdplaydate.models;

/**
 * Created by Martin on 11/3/2015.
 */
public class ChatMessage
{
    private String messageText;
    private long messageID;
    private long userID;
    private String dateTime;
    private boolean isMe;

    public long getMessageID()
    {
        return messageID;
    }

    public long getUserID()
    {
        return userID;
    }

    public String getDateTime()
    {
        return dateTime;
    }

    public String getMessageText()
    {
        return messageText;
    }

    public boolean isMe()
    {
        return isMe;
    }

    public void setIsMe(boolean isMe)
    {
        this.isMe = isMe;
    }

    public void setDateTime(String dateTime)
    {
        this.dateTime = dateTime;
    }

    public void setMessageText(String messageText)
    {
        this.messageText = messageText;
    }

    public void setMessageID(long messageID)
    {
        this.messageID = messageID;
    }

    public void setUserID(long userID)
    {
        this.userID = userID;
    }
}
