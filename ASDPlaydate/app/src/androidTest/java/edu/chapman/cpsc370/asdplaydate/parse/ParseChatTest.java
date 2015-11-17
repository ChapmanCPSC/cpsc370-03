package edu.chapman.cpsc370.asdplaydate.parse;

import android.util.Log;

import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.joda.time.DateTime;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import edu.chapman.cpsc370.asdplaydate.models.ASDPlaydateUser;
import edu.chapman.cpsc370.asdplaydate.models.Broadcast;
import edu.chapman.cpsc370.asdplaydate.models.ChatMessage;
import edu.chapman.cpsc370.asdplaydate.models.Child;
import edu.chapman.cpsc370.asdplaydate.models.Conversation;
import edu.chapman.cpsc370.asdplaydate.models.Message;

public class ParseChatTest extends ParseTest
{
    private final String INIT_USERNAME = "rburns7@chapman.edu";
    private final String REC_USERNAME = "caest100@mail.chapman.edu";

    //both of the above users have the same password defined below
    private final String TEST_PASSWORD = "test";

    //test coordinates
    private final Double TEST_LAT = 33.797685;
    private final Double TEST_LON = -117.849597;

    //Known accepted objectID
    private final String TEST_KEY = "SLEfmNyxmd";

    @Test
    public Conversation testSendChatInvitation() throws Exception
    {
        //query all the users
        ParseQuery<ParseUser> q = ASDPlaydateUser.getQuery();

        //get two results to use for test
        ASDPlaydateUser initiator = (ASDPlaydateUser) q.find().get(0);
        assertNotNull(initiator);

        ASDPlaydateUser receiver = (ASDPlaydateUser) q.find().get(1);
        assertNotNull(receiver);

        //start a test broadcast
        DateTime expireDate = DateTime.now().plusMinutes(60);
        ParseGeoPoint location = new ParseGeoPoint(TEST_LAT, TEST_LON);

        Broadcast broadcast = new Broadcast(initiator, location, expireDate);
        broadcast.save();

        Conversation convo = new Conversation(initiator, receiver, Conversation.Status.PENDING, broadcast.getExpireDate());
        convo.save();

        String lastID = convo.getObjectId();

        Conversation convoFromDb = getConversation(lastID);

        assertTrue(convoFromDb.getExpireDate().isAfterNow());
        assertTrue(convo.getExpireDate().equals(convoFromDb.getExpireDate()));
        assertTrue(convoFromDb.getInitiator().equals(convo.getInitiator()));
        assertTrue(convoFromDb.getReceiver().equals(convo.getReceiver()));
        return convo;
    }

    @Test
    public Conversation testAcceptChatInvitation() throws Exception
    {
        Conversation convo = testSendChatInvitation();

        DateTime oldExpireDate = convo.getExpireDate();

        convo.setStatus(Conversation.Status.ACCEPTED);
        convo.setExpireDate(DateTime.now().plusHours(24));
        convo.save();

        String lastID = convo.getObjectId();
        Conversation convoFromDb = getConversation(lastID);

        assertFalse(convoFromDb.getExpireDate().equals(oldExpireDate));
        assertTrue(convoFromDb.getExpireDate().isAfterNow());
        assertTrue(convoFromDb.getExpireDate().minusHours(24).isBefore(DateTime.now().toInstant()));
        return convoFromDb;
    }

    @Test
    public void testRejectChatInvitation() throws Exception
    {
        Conversation convo = testSendChatInvitation();

        DateTime oldExpireDate = convo.getExpireDate();

        convo.setStatus(Conversation.Status.DENIED);
        convo.setExpireDate(DateTime.now());
        convo.save();

        assertNotSame(convo.getExpireDate(), oldExpireDate);
        assertTrue(convo.getExpireDate().isBeforeNow());
    }

    @Test
    public void testGetChat() throws Exception
    {
        ParseQuery<Conversation> query = new ParseQuery<Conversation>(Conversation.class);
        Conversation convo = query.get(TEST_KEY);

        assertNotNull(convo.getInitiator());
        assertNotNull(convo.getReceiver());
    }


    @Test
    public void testSendAndGetMessages() throws Exception //lien103
    {
        String text = "test_sent_message_text";
        Conversation convo = testSendChatInvitation();
        Message message = new Message(convo, (ASDPlaydateUser) ASDPlaydateUser.logIn(INIT_USERNAME, TEST_PASSWORD), text, true, convo.getExpireDate());
        message.save();

        ParseQuery<Conversation> query = new ParseQuery<Conversation>(Conversation.class);
        assertNotNull(query.whereEqualTo(convo.getObjectId(), message.getObjectId()));

    }

    public Conversation getConversation(String objectId) throws Exception
    {
        ParseQuery<Conversation> q = new ParseQuery<Conversation>(Conversation.class);
        Conversation convo = q.get(objectId);

        return convo;
    }
}
