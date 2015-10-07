package com.example.cpsc.lienapp.db;

/**
 * Created by IsaacLien on 10/6/15.
 */
public class RecentEmailModel
{
    private String emailAddress;
    private int count;

    public RecentEmailModel(String email, int count)
    {
        this.emailAddress = email;
        this.count = count;
    }

    public RecentEmailModel(String email)
    {
        this.emailAddress = email;
        this.count = 1;
    }

    public String getEmailAddress()
    {
        return emailAddress;
    }

    public int getCount()
    {
        return count;
    }

    public void setCount(int count)
    {
        this.count = count;
    }

    public void setEmailAddress(String email)
    {
        this.emailAddress = email;
    }
}
