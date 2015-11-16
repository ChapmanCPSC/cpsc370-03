package edu.chapman.cpsc370.asdplaydate.parse;

import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.UUID;

import edu.chapman.cpsc370.asdplaydate.models.ASDPlaydateUser;
import edu.chapman.cpsc370.asdplaydate.models.Child;

/**
 * Created by ryanburns on 11/7/15.
 */
public class ParseUserTest extends ParseTest
{
    private final String TEST_USERNAME = "rburns@chapman.edu";
    private final String TEST_PASSWORD = "test";

    @Test
    public void testSignUp() throws Exception
    {
        String username = UUID.randomUUID().toString() + "@chapman.edu";
        String password = UUID.randomUUID().toString();

        //create user and child
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

        user.logOut();
    }

    @Test
    public void testLoginLogout() throws Exception
    {
        ASDPlaydateUser user = (ASDPlaydateUser) ASDPlaydateUser.logIn(TEST_USERNAME, TEST_PASSWORD);
        assertNotNull(user);
        Child child = getChildWithParent(user);
        assertNotNull(child);

        // When logged in, calling getCurrentUser() should not return null
        ASDPlaydateUser currentUser = (ASDPlaydateUser) ASDPlaydateUser.getCurrentUser();
        assertNotNull(currentUser);

        // If logged out properly, calling getCurrentUser() should return null
        ASDPlaydateUser.logOut();
        currentUser = (ASDPlaydateUser) ASDPlaydateUser.getCurrentUser();
        assertNull(currentUser);
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
    public void testEditChild() throws Exception
    {
        ASDPlaydateUser user = (ASDPlaydateUser) ASDPlaydateUser.become(TEST_SESSION);
        assertNotNull(user);

        Child testChild = getChildWithParent(user);
        assertNotNull(testChild);

        String childName = testChild.getFirstName();
        assertNotNull(childName);
        assertNotSame(childName, "");

        String newName = childName.charAt(0) == 'L' ? "lily" : "Lily";
        testChild.setFirstName(newName);
        testChild.save();

        int newAge = testChild.getAge()+1;
        testChild.setAge(newAge);
        testChild.save();

        ASDPlaydateUser compareUser = getUser(user.getObjectId());
        Child compareChild = getChildWithParent(compareUser);
        assertEquals(newName, compareChild.getFirstName());
        assertEquals(newAge, compareChild.getAge());

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

}
