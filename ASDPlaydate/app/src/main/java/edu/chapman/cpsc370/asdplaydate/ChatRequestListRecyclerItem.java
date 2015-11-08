package edu.chapman.cpsc370.asdplaydate;

/**
 * Created by Xavi on 3/11/15.
 */
public class ChatRequestListRecyclerItem
{
    private String parentName;
    private String lastMsg;
    private boolean accepted;

    public ChatRequestListRecyclerItem(String parentName, String lastMsg, boolean accepted)
    {
        this.parentName = parentName;
        this.lastMsg = lastMsg;
        this.accepted = accepted;
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

    public void setAccepted(boolean hasAccepted)
    {
        this.accepted = accepted;
    }
}
