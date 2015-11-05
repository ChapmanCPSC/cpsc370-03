package edu.chapman.cpsc370.asdplaydate;

/**
 * Created by Xavi on 3/11/15.
 */
public class ChatRequestListRecyclerItem
{
    private String parentName;
    private String lastMsg;
    private boolean hasAccepted;

    public ChatRequestListRecyclerItem(String parentName, String lastMsg, boolean hasAccepted) {
        this.parentName = parentName;
        this.lastMsg = lastMsg;
        this.hasAccepted = hasAccepted;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public String getLastMsg() {
        return lastMsg;
    }

    public void setLastMsg(String lastMsg) {
        this.lastMsg = lastMsg;
    }

    public boolean isHasAccepted() {
        return hasAccepted;
    }

    public void setHasAccepted(boolean hasAccepted) {
        this.hasAccepted = hasAccepted;
    }
}
