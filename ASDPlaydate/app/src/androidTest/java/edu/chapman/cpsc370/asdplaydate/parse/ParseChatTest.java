package edu.chapman.cpsc370.asdplaydate.parse;

import android.util.Log;

import com.parse.ParseGeoPoint;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import edu.chapman.cpsc370.asdplaydate.models.ASDPlaydateUser;
import edu.chapman.cpsc370.asdplaydate.models.Broadcast;
import edu.chapman.cpsc370.asdplaydate.models.Conversation;
import edu.chapman.cpsc370.asdplaydate.models.Message;

public class ParseChatTest extends ParseTest
{
    private final Double TEST_LAT = 33.797685;
    private final Double TEST_LON = -117.849597;

    @Test
    public Conversation testSendChatInvitation() throws Exception
    {
        //query all the users
        List<ParseUser> users = ASDPlaydateUser.getQuery().find();

        //get two results to use for test
        ASDPlaydateUser initiator = (ASDPlaydateUser) users.get(0);
        assertNotNull(initiator);

        ASDPlaydateUser receiver = (ASDPlaydateUser) users.get(1);
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
        assertTrue(convoFromDb.getStatus() == Conversation.Status.ACCEPTED);

        return convoFromDb;
    }

    @Test
    public Conversation testRejectChatInvitation() throws Exception
    {
        Conversation convo = testSendChatInvitation();

        DateTime oldExpireDate = convo.getExpireDate();

        convo.setStatus(Conversation.Status.DENIED);
        convo.setExpireDate(DateTime.now());
        convo.save();

        String lastID = convo.getObjectId();
        Conversation convoFromDb = getConversation(lastID);

        assertNotSame(convoFromDb.getExpireDate(), oldExpireDate);
        assertTrue(convoFromDb.getExpireDate().isBeforeNow());
        assertTrue(convoFromDb.getStatus() == Conversation.Status.DENIED);

        return convoFromDb;
    }

    @Test
    public void testGetConversations() throws Exception
    {
        List<ParseUser> users = ASDPlaydateUser.getQuery().find();

        //receiver from testSendChatInvitation()
        ASDPlaydateUser meReceiver = (ASDPlaydateUser) users.get(1);

        Conversation pendingChat = testSendChatInvitation();
        Conversation acceptedChat = testAcceptChatInvitation();
        testRejectChatInvitation();

        ParseQuery<Conversation> queryI = new ParseQuery<>(Conversation.class);
        queryI.whereEqualTo(Conversation.ATTR_INITIATOR, meReceiver);
        ParseQuery<Conversation> queryR = new ParseQuery<>(Conversation.class);
        queryR.whereEqualTo(Conversation.ATTR_RECEIVER, meReceiver);

        //where I'm the initiator or the receiver
        List<ParseQuery<Conversation>> queries = new ArrayList<>();
        queries.add(queryI);
        queries.add(queryR);

        //and accepted state
        ParseQuery<Conversation> initOrRec = ParseQuery.or(queries);
        initOrRec.whereMatches(Conversation.ATTR_STATUS, Conversation.Status.ACCEPTED.name());

        ParseQuery<Conversation> pend = new ParseQuery<>(Conversation.class);
        pend.whereMatches(Conversation.ATTR_STATUS, Conversation.Status.PENDING.name());
        pend.whereEqualTo(Conversation.ATTR_RECEIVER, meReceiver);

        queries.clear();
        queries.add(initOrRec);
        queries.add(pend);

        //or status is pending and i'm the receiver
        ParseQuery<Conversation> mainQuery = ParseQuery.or(queries);
        mainQuery.whereGreaterThan(Conversation.ATTR_EXPIRE_DATE, DateTime.now(DateTimeZone.UTC).toDate());

        List<Conversation> displayConvos = mainQuery.find();
        assertNotNull(displayConvos);

        List<Conversation> pendingConvos = new ArrayList<>();
        List<Conversation> acceptedConvos = new ArrayList<>();
        for (Conversation conversation:displayConvos)
        {
            switch (conversation.getStatus())
            {
                case DENIED:
                    fail("Query should not have gotten DENIED conversations.");
                case PENDING:
                    pendingConvos.add(conversation);
                    break;
                case ACCEPTED:
                    acceptedConvos.add(conversation);
                    break;
            }
        }

        //make sure pending convo is there
        assertTrue(pendingConvos.size()>0);
        assertTrue(pendingConvos.contains(pendingChat));

        //make sure accepted convo is there
        assertTrue(acceptedConvos.size()>0);
        assertTrue(acceptedConvos.contains(acceptedChat));
    }


    @Test
    public void testSendAndGetMessages() throws Exception //lien103
    {
        List<ParseUser> users = ASDPlaydateUser.getQuery().find();
        ASDPlaydateUser userInConvo = (ASDPlaydateUser) users.get(0);

        String text = "test_sent_message_text";
        Conversation convo = testSendChatInvitation();
        Message message = new Message(convo, userInConvo, text, false, DateTime.now());
        message.save();

        //query messages for that convo
        ParseQuery<Message> convoMessageQuery = new ParseQuery<>(Message.class)
                .whereEqualTo(Message.ATTR_CONVERSATION, convo);

        List<Message> convoMessages = convoMessageQuery.find();

        assertNotNull(convoMessages);
        assertTrue(convoMessages.contains(message));
    }

    public Conversation getConversation(String objectId) throws Exception
    {
        ParseQuery<Conversation> q = new ParseQuery<>(Conversation.class);
        Conversation convo = q.get(objectId);

        return convo;
    }

    @Test
    public void getLastMessage() throws Exception
    {
        Conversation convo = getConversation("CLaQ4hSZsa");
        ParseQuery<Message> q = new ParseQuery<Message>(Message.class);
        q.whereEqualTo(Message.ATTR_CONVERSATION, convo);
        q.orderByDescending(Message.ATTR_TIMESTAMP);

        Message lastMsg = q.getFirst();
        assertNotNull(lastMsg);
    }
}
