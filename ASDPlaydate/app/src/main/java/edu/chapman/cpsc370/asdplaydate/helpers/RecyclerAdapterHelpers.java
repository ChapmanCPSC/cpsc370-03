package edu.chapman.cpsc370.asdplaydate.helpers;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.Toast;

import com.google.android.gms.maps.model.Marker;
import com.parse.ParseException;

import org.joda.time.DateTime;

import edu.chapman.cpsc370.asdplaydate.managers.SessionManager;
import edu.chapman.cpsc370.asdplaydate.models.ASDPlaydateUser;
import edu.chapman.cpsc370.asdplaydate.models.Conversation;

/**
 * Created by Xavi on 23/11/15.
 */
public class RecyclerAdapterHelpers
{

    public static void sendChatRequest(SessionManager sm, ASDPlaydateUser receiver, Context ctx, Marker marker)
    {
        try
        {
            ASDPlaydateUser initiator = (ASDPlaydateUser) ASDPlaydateUser.become(sm.getSessionToken());     //TODO: get initiator's broadcast expiredate here
            Conversation convo = new Conversation(initiator, receiver, Conversation.Status.PENDING, DateTime.now().plusMinutes(60));
            convo.save();
            Toast.makeText(ctx, "Sent chat request to " + receiver.getFirstName() + " " + receiver.getLastName(), Toast.LENGTH_SHORT).show();
            //TODO: update the send chat request UI here
            //remove the marker
            marker.remove();
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
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
}
