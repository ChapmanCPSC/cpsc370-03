package edu.chapman.cpsc370.asdplaydate.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.joda.time.DateTime;

import java.util.List;

import edu.chapman.cpsc370.asdplaydate.helpers.DateHelpers;

@ParseClassName("Children")
public class Child extends ParseObject
{
    public static final String ATTR_PARENT = "parent";
    public static final String ATTR_FIRST_NAME = "first_name";
    public static final String ATTR_AGE = "age";
    public static final String ATTR_GENDER = "gender";
    public static final String ATTR_DESCRIPTION = "description";

    public Child(){}

    public Child(ASDPlaydateUser parent, String name, int age, Gender gender)
    {
        setParent(parent);
        setFirstName(name);
        setAge(age);
        setGender(gender);
    }

    public Child(ASDPlaydateUser parent, String name, int age, Gender gender, String description)
    {
        this(parent, name, age, gender);
        setDescription(description);
    }

    public ASDPlaydateUser getParent()
    {
        return (ASDPlaydateUser) getParseUser(ATTR_PARENT);
    }

    public void setParent(ASDPlaydateUser parent)
    {
        put(ATTR_PARENT, parent);
    }

    public String getFirstName()
    {
        return getString(ATTR_FIRST_NAME);
    }

    public void setFirstName(String name)
    {
        put(ATTR_FIRST_NAME, name);
    }

    public int getAge()
    {
        return getInt(ATTR_AGE);
    }

    public void setAge(int age)
    {
        put(ATTR_AGE, age);
    }

    public Gender getGender()
    {
        String gender = getString(ATTR_GENDER);
        return Gender.valueOf(gender);
    }

    public void setGender(Gender gender)
    {
        put(ATTR_GENDER, gender.name());
    }

    public String getDescription()
    {
        return getString(ATTR_DESCRIPTION);
    }

    public void setDescription(String desc)
    {
        put(ATTR_DESCRIPTION, desc);
    }

    public enum Gender
    {
        MALE,FEMALE,NONE;
    }

    public static Child getChildFromParent(ASDPlaydateUser user) throws Exception
    {
        ParseQuery<Child> q = new ParseQuery<>(Child.class);
        q.whereEqualTo(Child.ATTR_PARENT, user);

        List<Child> children = q.find();

        return (Child) children.get(0);
    }
}
