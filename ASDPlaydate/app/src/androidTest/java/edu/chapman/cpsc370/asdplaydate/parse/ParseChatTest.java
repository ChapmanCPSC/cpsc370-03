package edu.chapman.cpsc370.asdplaydate.parse;

import com.parse.ParseGeoPoint;

import org.joda.time.DateTime;

import edu.chapman.cpsc370.asdplaydate.models.ASDPlaydateUser;
import edu.chapman.cpsc370.asdplaydate.models.Broadcast;
import edu.chapman.cpsc370.asdplaydate.models.Conversation;

public class ParseChatTest extends ParseTest
{
    private final String INIT_USERNAME = "rburns7@chapman.edu";
    private final String REC_USERNAME = "caest100@mail.chapman.edu";

    //both of the above users have the same password defined below
    private final String TEST_PASSWORD = "test";

    //test coordinates
    private final Double TEST_LAT = 33.797685;
    private final Double TEST_LON = -117.849597;

    public void sendChatInvitation() throws Exception
    {
        ASDPlaydateUser initiator = (ASDPlaydateUser) ASDPlaydateUser.logIn(INIT_USERNAME, TEST_PASSWORD);
        assertNotNull(initiator);

        ASDPlaydateUser receiver = (ASDPlaydateUser) ASDPlaydateUser.logIn(REC_USERNAME, TEST_PASSWORD);
        assertNotNull(receiver);

        //start a test broadcast (Broadcast Unit Tests were not implemented at this time)
        DateTime expireDate = DateTime.now().plusMinutes(60);
        ParseGeoPoint location = new ParseGeoPoint(TEST_LAT, TEST_LON);

        Broadcast broadcast = new Broadcast(initiator, location, expireDate);
        broadcast.save();

        Conversation convo = new Conversation(initiator, receiver, Conversation.Status.PENDING, broadcast.getExpireDate());
        convo.save();

        assertTrue(convo.getExpireDate().isAfterNow());
    }

    public void acceptChatInvitation() throws Exception
    {

    }

}
