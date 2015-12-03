package edu.chapman.cpsc370.asdplaydate.models;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

/**
 * Created by Xavi on 19/11/15.
 */
public class MarkerLabelInfo
{
    private ASDPlaydateUser parent;
    private Child child;
    private Marker marker;
    private LatLng latLng;
    private int index;
    private boolean hasConversation = false;

    public MarkerLabelInfo(ASDPlaydateUser parent, Child child)
    {
        this.parent = parent;
        this.child = child;
    }

    public MarkerLabelInfo(ASDPlaydateUser parent, Child child, LatLng latLng)
    {
        this.parent = parent;
        this.child = child;
        this.latLng = latLng;
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

    public Marker getMarker()
    {
        return marker;
    }

    public void setMarker(Marker marker)
    {
        this.marker = marker;
    }

    public LatLng getLatLng()
    {
        return latLng;
    }

    public void setLatLng(LatLng latLng)
    {
        this.latLng = latLng;
    }

    public int getIndex()
    {
        return index;
    }

    public void setIndex(int index)
    {
        this.index = index;
    }

    public boolean hasConversation()
    {
        return hasConversation;
    }

    public void setHasConversation(boolean hasConversation)
    {
        this.hasConversation = hasConversation;
    }
}
