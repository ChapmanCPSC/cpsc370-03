package edu.chapman.cpsc370.asdplaydate.helpers;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.Toast;

import com.google.android.gms.maps.model.Marker;
import com.parse.ParsePush;
import com.parse.ParseQuery;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.util.ArrayList;
import java.util.List;

import edu.chapman.cpsc370.asdplaydate.R;
import edu.chapman.cpsc370.asdplaydate.activities.ChatActivity;
import edu.chapman.cpsc370.asdplaydate.models.ASDPlaydateUser;
import edu.chapman.cpsc370.asdplaydate.models.ChatRequestListRecyclerItem;
import edu.chapman.cpsc370.asdplaydate.models.Conversation;

/**
 * Created by Xavi on 23/11/15.
 */
public class RecyclerAdapterHelpers
{

    public static void sendChatRequest(ASDPlaydateUser receiver, Context ctx, Marker marker)
    {
        ASDPlaydateUser initiator = (ASDPlaydateUser) ASDPlaydateUser.getCurrentUser();
        Conversation result = usersHaveConversation(initiator, receiver);
        if (result != null)
        {
            Intent i = new Intent(ctx, ChatActivity.class);
            i.putExtra("conversationID", result.getObjectId());
            if (result.getInitiator().equals(initiator))
            {
                i.putExtra("parentName", result.getReceiver().getFirstName() + " " + result.getReceiver().getLastName());
            } else
            {
                i.putExtra("parentName", result.getInitiator().getFirstName() + " " + result.getInitiator().getLastName());
            }
            ctx.startActivity(i);
            Toast.makeText(ctx, ctx.getText(R.string.users_have_conversation), Toast.LENGTH_SHORT).show();
        } else
        {
            DateTime convoExpire = initiator.getLastBroadcast().getExpireDate();
            if (convoExpire == null)
            {
                convoExpire = DateTime.now().plusHours(24);
            }

            Conversation convo = new Conversation(initiator, receiver, Conversation.Status.PENDING, convoExpire);
            convo.saveInBackground();
            Toast.makeText(ctx, "Sent chat request to " + receiver.getFirstName() + " " + receiver.getLastName(), Toast.LENGTH_SHORT).show();

            // Send push to receiver
            ParsePush push = new ParsePush();
            push.setChannel("c_" + receiver.getObjectId());
            push.setMessage("New chat request from " + initiator.getFirstName() + " "
                    + initiator.getLastName());
            push.sendInBackground();
        }
        //remove the marker
        marker.remove();
    }

    public static Conversation usersHaveConversation(ASDPlaydateUser user1, ASDPlaydateUser user2)
    {
        ParseQuery<Conversation> user1Initiate = new ParseQuery<>(Conversation.class);
        user1Initiate.whereEqualTo(Conversation.ATTR_INITIATOR, user1);
        user1Initiate.whereEqualTo(Conversation.ATTR_RECEIVER, user2);

        ParseQuery<Conversation> user2Initiate = new ParseQuery<>(Conversation.class);
        user2Initiate.whereEqualTo(Conversation.ATTR_INITIATOR, user2);
        user2Initiate.whereEqualTo(Conversation.ATTR_RECEIVER, user1);

        List<ParseQuery<Conversation>> both = new ArrayList<>();
        both.add(user1Initiate);
        both.add(user2Initiate);

        ParseQuery<Conversation> conversationsBetweenUsers = ParseQuery.or(both);
        conversationsBetweenUsers.whereNotEqualTo(Conversation.ATTR_STATUS, Conversation.Status.DENIED.name());
        conversationsBetweenUsers.whereGreaterThan(Conversation.ATTR_EXPIRE_DATE, DateTime.now().toDate());
        try
        {
            if (conversationsBetweenUsers.count() > 0)
            {
                return conversationsBetweenUsers.find().get(0);
            }

        } catch (Exception e)
        {
            e.printStackTrace();
        }


        return null;
    }

    public static void doSlideOutAnim(View vi)
    {
        ObjectAnimator slideOutRight = ObjectAnimator.ofFloat(vi, "translationX", 1000f);
        ObjectAnimator fadeOut = ObjectAnimator.ofFloat(vi, "alpha", 0);
        slideOutRight.setDuration(250);
        fadeOut.setDuration(250);
        AnimatorSet set = new AnimatorSet();
        set.playTogether(slideOutRight, fadeOut);
        set.setInterpolator(new AccelerateInterpolator());
        set.start();
    }

    public static void denyRequest(ChatRequestListRecyclerItem item)
    {
        Conversation convo = new Conversation();
        String conversationID = item.getConversationID();
        try
        {
            convo = Conversation.getConversation(conversationID);
        } catch (Exception e)
        {
            e.printStackTrace();
        }

        convo.setStatus(Conversation.Status.DENIED);
        convo.setExpireDate(DateTime.now());
        convo.saveInBackground();
    }
}
