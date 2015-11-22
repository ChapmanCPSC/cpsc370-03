package edu.chapman.cpsc370.asdplaydate.models;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Xavi on 19/11/15.
 */
public class MarkerLabelInfo
{
    private ASDPlaydateUser parent;
    private Child child;
    private LatLng location;

    public MarkerLabelInfo(ASDPlaydateUser parent, Child child)
    {
        this.parent = parent;
        this.child = child;
    }

    public MarkerLabelInfo(ASDPlaydateUser parent, Child child, LatLng location)
    {
        this.parent = parent;
        this.child = child;
        this.location = location;
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

    public LatLng getLocation()
    {
        return location;
    }

    public void setLocation(LatLng location)
    {
        this.location = location;
    }

}
