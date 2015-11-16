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
    private final String TEST_USERNAME = "rburns300@chapman.edu";
    private final String TEST_PASSWORD = "test";

    @Test
    public void testSignUp() throws Exception
    {
        //create user and child
        String username = "rburns312@chapman.edu";
        String password = "test";
        String first = "Ryan";
        String last = "Burns";
        String city = "Santa Ana";

        String childFirst = "Lily";
        int childAge = 12;
        Child.Gender childGender = Child.Gender.FEMALE;
        String childDesc = "Low Functioning";

        ASDPlaydateUser user = new ASDPlaydateUser(username, password, first, last, city);
        Child child = new Child(user, childFirst, childAge, childGender, childDesc);
        user.signUp();
        child.save();

        //logout
        ASDPlaydateUser.logOut();

        //login as same user
        ASDPlaydateUser user2 = (ASDPlaydateUser) ASDPlaydateUser.logIn(username, password);

        //make sure they are the same
        assertEquals(first, user2.getFirstName());

        //get child from that user
        Child child2 = getChildWithParent(user2);

        //make sure child matches
        assertEquals(childFirst, child2.getFirstName());

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

    @Test
    public void testLogin() throws Exception
    {

        ASDPlaydateUser user = (ASDPlaydateUser) ASDPlaydateUser.logIn(TEST_USERNAME, TEST_PASSWORD);
        assertNotNull(user);
        Child child = getChildWithParent(user);
        assertNotNull(child);
    }

    @Test
    public void testLogout() throws Exception
    {
        ASDPlaydateUser user = (ASDPlaydateUser) ASDPlaydateUser.logIn(TEST_USERNAME, TEST_PASSWORD);
        assertNotNull(user);

        // When logged in, calling getCurrentUser() should not return null
        ASDPlaydateUser currentUser = (ASDPlaydateUser) ASDPlaydateUser.getCurrentUser();
        assertNotNull(currentUser);

        // If logged out properly, calling getCurrentUser() should return null
        ASDPlaydateUser.logOut();
        currentUser = (ASDPlaydateUser)ASDPlaydateUser.getCurrentUser();
        assertNull(currentUser);
    }

    @Test
    public void testEditChild() throws Exception
    {
        ASDPlaydateUser user = (ASDPlaydateUser) ASDPlaydateUser.logIn(TEST_USERNAME, TEST_PASSWORD);
        assertNotNull(user);

        Child testChild = getChildWithParent(user);
        assertNotNull(testChild);

        String childName = testChild.getFirstName();
        assertNotNull(childName);
        assertNotSame(childName, "");

        String newName = "Jill";
        testChild.setFirstName(newName);
        testChild.save();

        int newAge = 13;
        testChild.setAge(newAge);
        testChild.save();

        ASDPlaydateUser compareUser = getUser(user.getObjectId());
        Child compareChild = getChildWithParent(compareUser);
        assertEquals(newName, compareChild.getFirstName());
        assertEquals(newAge, compareChild.getAge() );

        ASDPlaydateUser.logOut();

    }


    private Child getChildWithParent(ASDPlaydateUser parent) throws Exception
    {
        ParseQuery<Child> q = new ParseQuery<Child>(Child.class);
        q.whereEqualTo(Child.ATTR_PARENT, parent);
        return q.find().get(0);
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
