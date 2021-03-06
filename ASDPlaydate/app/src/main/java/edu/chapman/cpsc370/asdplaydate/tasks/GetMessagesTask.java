package edu.chapman.cpsc370.asdplaydate.tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.parse.ParseQuery;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.util.ArrayList;
import java.util.List;

import edu.chapman.cpsc370.asdplaydate.managers.SessionManager;
import edu.chapman.cpsc370.asdplaydate.models.ASDPlaydateUser;
import edu.chapman.cpsc370.asdplaydate.models.ChatRequestListRecyclerItem;
import edu.chapman.cpsc370.asdplaydate.models.Conversation;
import edu.chapman.cpsc370.asdplaydate.models.Message;

public class GetMessagesTask extends AsyncTask<Void, Void, Void>
{
    Context ctx;
    public Runnable onFinish;
    public List<ChatRequestListRecyclerItem> mItems = new ArrayList<>();

    public GetMessagesTask(Context ctx)
    {
        this.ctx = ctx;
    }

    @Override
    protected Void doInBackground(Void... params)
    {
        try
        {
            //receiver from testSendChatInvitation()
            SessionManager sessionManager = new SessionManager(ctx);
            ASDPlaydateUser me = (ASDPlaydateUser) ASDPlaydateUser.become(sessionManager.getSessionToken());

            //show conversations the current user received from other users
            //that the current user did not deny
            ParseQuery<Conversation> meReceive = new ParseQuery<>(Conversation.class);
            meReceive.whereEqualTo(Conversation.ATTR_RECEIVER, me);
            meReceive.whereNotEqualTo(Conversation.ATTR_STATUS, Conversation.Status.DENIED.name());

            //show only the conversations the current user sent and other users accepted
            ParseQuery<Conversation> meSend = new ParseQuery<>(Conversation.class);
            meSend.whereEqualTo(Conversation.ATTR_INITIATOR, me);
            meSend.whereEqualTo(Conversation.ATTR_STATUS, Conversation.Status.ACCEPTED.name());

            List<ParseQuery<Conversation>> both = new ArrayList<>();
            both.add(meReceive);
            both.add(meSend);

            ParseQuery<Conversation> meSendOrMeReceive = ParseQuery.or(both);
            meSendOrMeReceive.whereGreaterThan(Conversation.ATTR_EXPIRE_DATE, DateTime.now(DateTimeZone.UTC).toDate());

            List<Conversation> displayConvos = meSendOrMeReceive.find();

            mItems.clear();
            for (Conversation c : displayConvos)
            {
                boolean accepted = false;
                if (c.getStatus().toString().equals(Conversation.Status.ACCEPTED.name()))
                    accepted = true;

                ASDPlaydateUser initiator = c.getInitiator();
                ASDPlaydateUser receiver = c.getReceiver();

                if (initiator.equals(me))
                {
                    String firstName = "";
                    String lastName = "";
                    Message lastMsg = null;
                    try
                    {
                        firstName = receiver.fetchIfNeeded().getString("first_name");
                        lastName = receiver.fetchIfNeeded().getString("last_name");
                        ParseQuery<Message> q = new ParseQuery<>(Message.class);
                        q.whereEqualTo(Message.ATTR_CONVERSATION, c);
                        q.orderByDescending(Message.ATTR_TIMESTAMP);
                        q.setLimit(1);
                        lastMsg = q.getFirst();
                        lastMsg.fetchIfNeeded();
                    } catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                    ChatRequestListRecyclerItem item = new ChatRequestListRecyclerItem(c.getObjectId(), receiver.getObjectId(), firstName + " " + lastName, null, accepted);
                    if (lastMsg != null)
                    {
                        item.setRead(lastMsg.isRead());
                        item.setLastMsg(lastMsg.getText());
                    }
                    mItems.add(item);
                } else
                {
                    String firstName = "";
                    String lastName = "";
                    Message lastMsg = null;
                    try
                    {
                        firstName = initiator.fetchIfNeeded().getString("first_name");
                        lastName = initiator.fetchIfNeeded().getString("last_name");
                        ParseQuery<Message> q = new ParseQuery<>(Message.class);
                        q.whereEqualTo(Message.ATTR_CONVERSATION, c);
                        q.orderByDescending(Message.ATTR_TIMESTAMP);
                        q.setLimit(1);
                        lastMsg = q.getFirst();
                        lastMsg.fetchIfNeeded();
                    } catch (Exception e)
                    {
                        e.printStackTrace();
                    }

                    ChatRequestListRecyclerItem item = new ChatRequestListRecyclerItem(c.getObjectId(), initiator.getObjectId(), firstName + " " + lastName, null, accepted);
                    if (lastMsg != null)
                    {
                        item.setRead(lastMsg.isRead());
                        item.setLastMsg(lastMsg.getText());
                    }
                    mItems.add(item);
                }
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void params)
    {
        super.onPostExecute(params);

        if (onFinish != null)
        {
            onFinish.run();
        }
    }
}
