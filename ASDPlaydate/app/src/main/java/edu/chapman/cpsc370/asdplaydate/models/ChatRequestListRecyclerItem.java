package edu.chapman.cpsc370.asdplaydate.models;

/**
 * Created by Xavi on 3/11/15.
 */
public class ChatRequestListRecyclerItem
{
    private String parentName;
    private String lastMsg;
    private boolean accepted;
    private String conversationID;

    public ChatRequestListRecyclerItem(String conversationID, String parentName, String lastMsg, boolean accepted)
    {
        this.conversationID = conversationID;
        this.parentName = parentName;
        this.lastMsg = lastMsg;
        this.accepted = accepted;
    }

    public String getConversationID()
    {
        return conversationID;
    }

    public void setConversationID(String conversationID)
    {
        this.conversationID = conversationID;
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
