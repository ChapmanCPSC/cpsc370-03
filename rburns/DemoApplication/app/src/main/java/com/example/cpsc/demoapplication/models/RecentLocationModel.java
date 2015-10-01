package com.example.cpsc.demoapplication.models;

/**
 * Created by ryanb on 9/30/2015.
 */
public class RecentLocationModel
{
    private String zip;
    private int count;

    public RecentLocationModel(String zip, int count)
    {
        this.zip = zip;
        this.count = count;
    }

    public RecentLocationModel(String zip)
    {
        this.zip = zip;
        this.count = 1;
    }

    public String getZip()
    {
        return zip;
    }

    public int getCount()
    {
        return count;
    }

    public void setCount(int count)
    {
        this.count = count;
    }

    public void setZip(String zip)
    {
        this.zip = zip;
    }
}
