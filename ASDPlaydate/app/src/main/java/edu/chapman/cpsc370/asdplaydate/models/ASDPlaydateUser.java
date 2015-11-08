package edu.chapman.cpsc370.asdplaydate.models;

import com.parse.ParseClassName;
import com.parse.ParseUser;

@ParseClassName("ASDPlaydateUsers")
public class ASDPlaydateUser extends ParseUser
{
    private final String ATTR_FIRST_NAME = "first_name";
    private final String ATTR_LAST_NAME = "last_name";
    private final String ATTR_CITY_NAME = "city";

    public ASDPlaydateUser(){}

    public ASDPlaydateUser(String email, String password, String firstName, String lastName, String city)
    {
        setUsername(email);
        setEmail(email);
        setPassword(password);
        setFirstName(firstName);
        setLastName(lastName);
        setCityName(city);
    }

    public String getFirstName()
    {
        return getString(ATTR_FIRST_NAME);
    }

    public void setFirstName(String firstName)
    {
        put(ATTR_FIRST_NAME, firstName);
    }

    public String getLastName()
    {
        return getString(ATTR_LAST_NAME);
    }

    public void setLastName(String lastName)
    {
        put(ATTR_LAST_NAME, lastName);
    }

    public String getCityName()
    {
        return getString(ATTR_CITY_NAME);
    }

    public void setCityName(String city)
    {
        put(ATTR_CITY_NAME, city);
    }
}
