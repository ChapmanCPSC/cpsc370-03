package edu.chapman.cpsc370.asdplaydate.parse;

import com.parse.ParseGeoPoint;
import com.parse.ParseQuery;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Test;

import edu.chapman.cpsc370.asdplaydate.models.ASDPlaydateUser;
import edu.chapman.cpsc370.asdplaydate.models.Broadcast;

public class ParseBroadcastTest extends ParseTest
{

    @Test
    public void testBroadcast() throws Exception
    {
        // Log in with existing user account
        ASDPlaydateUser broadcaster = (ASDPlaydateUser) ASDPlaydateUser.logIn("rburns11@chapman.edu", "test");

        // Location where the user is at
        ParseGeoPoint location = new ParseGeoPoint(33.7928, -117.8514);

        // The message the user broadcasted
        String message = "I'm at Starbucks";

        // The time that the broadcast will expire
        int duration = 60;
        DateTime expireDate = DateTime.now().plusMinutes(duration);

        // Try adding to Parse with a message
        Broadcast b1 = new Broadcast(broadcaster, location, message, expireDate);
        b1.save();

        // Try adding to parse without a message
        Broadcast b2 = new Broadcast(broadcaster, location, expireDate);
        b2.save();
    }

    @Test
    public void testBroadcastResults() throws Exception
    {
        // 1 mile = approx 0.01447301695 in latitude/longtitude

        // Login with existing users
        ASDPlaydateUser bcaster1 = (ASDPlaydateUser) ASDPlaydateUser.logIn("rburns4@chapman.edu", "test");
        assertNotNull(bcaster1);
        ASDPlaydateUser bcaster2 = (ASDPlaydateUser) ASDPlaydateUser.logIn("rburns5@chapman.edu", "test");
        assertNotNull(bcaster2);

        // Set locations to be within 1 mile
        ParseGeoPoint l1 = new ParseGeoPoint(33.7928, -117.8514);
        ParseGeoPoint l2 = new ParseGeoPoint(33.80727301695, -117.8514);

        // Confirm locations are within 1 mile
        System.out.println(l1.distanceInMilesTo(l2)); // 0.9999999998005207 mi

        // Both users broadcasted at the same time with different broadcast times
        DateTime myExpireDate = DateTime.now().plusMinutes(60); // Broadcast ends in 60 mins
        DateTime broadcastToGetExpireDate = DateTime.now().plusMinutes(30); // Broadcast ends in 30 mins

        // Save in Parse
        Broadcast myBroadcast = new Broadcast(bcaster1, l1, "bcaster1", myExpireDate);
        myBroadcast.save();
        Broadcast broadcastToGet = new Broadcast(bcaster1, l2, "bcaster2", broadcastToGetExpireDate);
        broadcastToGet.save();

        ParseQuery<Broadcast> q = new ParseQuery<Broadcast>(Broadcast.class);
        q.whereGreaterThan(Broadcast.ATTR_EXPIRE_DATE, DateTime.now(DateTimeZone.UTC).toDate())
                .whereWithinMiles(Broadcast.ATTR_LOCATION, l1, 1.0)
                .whereNotEqualTo(Broadcast.ATTR_BROADCASTER, bcaster1);

        // Confirm how many results returned
        assertTrue(q.count()>0);
    }
}
