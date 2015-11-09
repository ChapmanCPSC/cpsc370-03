package edu.chapman.cpsc370.asdplaydate.parse;

import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import junit.framework.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import edu.chapman.cpsc370.asdplaydate.models.ASDPlaydateUser;
import edu.chapman.cpsc370.asdplaydate.models.Child;

/**
 * Created by ryanburns on 11/7/15.
 */
public class ParseUserTest extends ParseTest
{
    @Test
    public void testSignUp() throws Exception
    {
        //get list of children
        List<Child> beforeChildren = new ArrayList<Child>();
        try
        {
            beforeChildren = getChildren();
        }
        catch (ParseException pe)
        {
            Assert.fail(pe.getMessage());
        }

        //get list of users
        List<ParseUser> beforeUsers = new ArrayList<ParseUser>();
        try
        {
            beforeUsers = getUsers();
        }
        catch (ParseException pe)
        {
            Assert.fail(pe.getMessage());
        }

        ASDPlaydateUser user = new ASDPlaydateUser("rburns7@chapman.edu", "test", "Ryan", "Burns", "Santa Ana");
        Child child = new Child(user, "Lilly3",10, Child.Gender.FEMALE, "High Functioning");
        user.signUp();
        child.save();

        //get list of users
        List<ParseUser> afterUsers = new ArrayList<ParseUser>();
        try
        {
            afterUsers = getUsers();
        }
        catch (ParseException pe)
        {
            Assert.fail(pe.getMessage());
        }

        assertTrue("No user was created", afterUsers.size() > beforeUsers.size());

        //get new list of children. make sure one more
        List<Child> afterChildren = new ArrayList<Child>();

        try
        {
            afterChildren = getChildren();
        }
        catch (ParseException pe)
        {
            Assert.fail(pe.getMessage());
        }

        assertTrue("No child was created", afterChildren.size() > beforeChildren.size());
    }

    @Test
    public void testUpdateProfile() throws Exception
    {
        ASDPlaydateUser user = (ASDPlaydateUser) ASDPlaydateUser.become(TEST_SESSION);
        String name = user.getFirstName();
        assertNotNull(name);
        assertNotSame(name, "");

        String newName = name.charAt(0) == 'R' ? "ryan" : "Ryan";

        user.setFirstName(newName);
        user.save();

        ASDPlaydateUser gotUser = getUser(user.getObjectId());
        assertEquals(newName, gotUser.getFirstName());
    }

    private ASDPlaydateUser getUser(String objectId) throws Exception
    {
        ParseQuery<ParseUser> q = ASDPlaydateUser.getQuery();
        q.whereEqualTo(ASDPlaydateUser.ATTR_ID, objectId);

        List<ParseUser> users = q.find();

        return (ASDPlaydateUser) users.get(0);
    }

    private List<ParseUser> getUsers() throws ParseException
    {
        ParseQuery<ParseUser> q = ParseUser.getQuery();
        return q.find();
    }

    private List<Child> getChildren() throws ParseException
    {
        ParseQuery<Child> q = new ParseQuery<Child>(Child.class);
        return q.find();
    }

}
