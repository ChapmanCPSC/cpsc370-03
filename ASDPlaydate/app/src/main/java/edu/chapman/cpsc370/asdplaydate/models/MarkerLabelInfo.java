package edu.chapman.cpsc370.asdplaydate.models;

/**
 * Created by Xavi on 19/11/15.
 */
public class MarkerLabelInfo
{
    private ASDPlaydateUser parent;
    private Child child;

    public MarkerLabelInfo(ASDPlaydateUser parent, Child child)
    {
        this.parent = parent;
        this.child = child;
    }

    public ASDPlaydateUser getParent()
    {
        return parent;
    }

    public void setParent(ASDPlaydateUser parent)
    {
        this.parent = parent;
    }

    public Child getChild()
    {
        return child;
    }

    public void setChild(Child child)
    {
        this.child = child;
    }

}
