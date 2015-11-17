package edu.chapman.cpsc370.asdplaydate.parse;

import com.parse.ParseGeoPoint;
import com.parse.ParseQuery;

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

    @Test
    public Conversation testSendChatInvitation() throws Exception
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
        return convo;
    }

    @Test
    public void testAcceptChatInvitation() throws Exception
    {
        Conversation convo = testSendChatInvitation();

        DateTime oldExpireDate = convo.getExpireDate();

        convo.setStatus(Conversation.Status.ACCEPTED);
        convo.setExpireDate(DateTime.now().plusHours(24));
        convo.save();

        assertNotSame(convo.getExpireDate(), oldExpireDate);
        assertTrue(convo.getExpireDate().isAfterNow());
        assertTrue(convo.getExpireDate().minusHours(24).isBefore(DateTime.now().toInstant()));
    }

    @Test
    public void testGetMessages() throws Exception //lien103
    {
        String text = "test_message_text";
        Conversation convo = testSendChatInvitation();
        Message message = new Message(convo, (ASDPlaydateUser) ASDPlaydateUser.logIn(INIT_USERNAME, TEST_PASSWORD), text, true, convo.getExpireDate());

        assertNotNull(message.getText());
        assertNotNull(message.getAuthor());
        assertNotNull(message.getConversation());
        assertNotNull(message.getTimestamp());
    }

    @Test
    public void testSendMessage() throws Exception //lien103
    {
        String text = "test_sent_message_text";
        Conversation convo = testSendChatInvitation();
        Message message = new Message(convo, (ASDPlaydateUser) ASDPlaydateUser.logIn(INIT_USERNAME, TEST_PASSWORD), text, true, convo.getExpireDate());
        message.save();

        assertNotNull(message.getText());
        assertNotNull(message.getAuthor());
        assertNotNull(message.getConversation());
        assertNotNull(message.getTimestamp());
    }
}
