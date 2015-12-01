package edu.chapman.cpsc370.asdplaydate.models;

/**
 * Created by Xavi on 3/11/15.
 */
public class ChatRequestListRecyclerItem
{
    private String parentName;
    private String lastMsg;
    private boolean accepted;
    private String userID;

    public ChatRequestListRecyclerItem(String userID, String parentName, String lastMsg, boolean accepted)
    {
        this.userID = userID;
        this.parentName = parentName;
        this.lastMsg = lastMsg;
        this.accepted = accepted;
    }

    public String getUserID()
    {
        return userID;
    }

    public void setUserID(String userID)
    {
        this.userID = userID;
    }

    public String getParentName()
    {
        return parentName;
    }

    public void setParentName(String parentName)
    {
        this.parentName = parentName;
    }

    public String getLastMsg()
    {
        return lastMsg;
    }

    public void setLastMsg(String lastMsg)
    {
        this.lastMsg = lastMsg;
    }

    public boolean isAccepted()
    {
        return accepted;
    }

    public void setAccepted(boolean accepted)
    {
        this.accepted = accepted;
    }
}
