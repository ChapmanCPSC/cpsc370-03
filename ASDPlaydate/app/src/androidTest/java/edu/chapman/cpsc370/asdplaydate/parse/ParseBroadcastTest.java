package edu.chapman.cpsc370.asdplaydate.parse;

import com.parse.ParseGeoPoint;
import com.parse.ParseQuery;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Test;

import java.util.List;
import java.util.UUID;

import edu.chapman.cpsc370.asdplaydate.models.ASDPlaydateUser;
import edu.chapman.cpsc370.asdplaydate.models.Broadcast;

public class ParseBroadcastTest extends ParseTest
{

    @Test
    public void testBroadcast() throws Exception
    {
        // Log in with existing user account
        ASDPlaydateUser broadcaster = (ASDPlaydateUser) ASDPlaydateUser.become(TEST_SESSION);

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
        ASDPlaydateUser me = (ASDPlaydateUser) ASDPlaydateUser.become(TEST_SESSION);

        //get a random user (eg first user ever)
        ASDPlaydateUser broadcaster = (ASDPlaydateUser) ASDPlaydateUser.getQuery().find().get(0);
        ParseGeoPoint broadcastLocation = new ParseGeoPoint(33.7928, -117.8514);
        DateTime broadcastExpireDate = DateTime.now().plusMinutes(1); //broadcast for 1 minute

        String uniqueMessage = UUID.randomUUID().toString();

        Broadcast myBroadcast = new Broadcast(broadcaster, broadcastLocation, uniqueMessage, broadcastExpireDate);
        myBroadcast.save();

        // Set locations to be within 1 mile
        ParseGeoPoint myLocation = new ParseGeoPoint(33.80727301695, -117.8514);

        //make sure this location is within a mile of the testing broadcaster
        assertTrue(myLocation.distanceInMilesTo(broadcastLocation) <= 1);

        ParseQuery<Broadcast> q = new ParseQuery<>(Broadcast.class);
        q.whereGreaterThan(Broadcast.ATTR_EXPIRE_DATE, DateTime.now(DateTimeZone.UTC).toDate())
                .whereWithinMiles(Broadcast.ATTR_LOCATION, myLocation, 1.0)
                .whereNotEqualTo(Broadcast.ATTR_BROADCASTER, me);

        List<Broadcast> results = q.find();

        //make sure at least 1 result
        assertTrue(results.size() > 0);

        //make sure there is a result with the unique message
        boolean foundBroadcasted = false;
        for (Broadcast b:results)
        {
            if (b.getMessage() != null && b.getMessage().equals(uniqueMessage))
            {
                foundBroadcasted = true;
            }
        }
        assertTrue(foundBroadcasted);
    }
}
