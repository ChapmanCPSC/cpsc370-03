package edu.chapman.cpsc370.asdplaydate.adapters;

import android.content.Context;
import android.location.Location;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewParent;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseUser;

import org.joda.time.DateTime;

import java.util.HashMap;
import java.util.List;

import edu.chapman.cpsc370.asdplaydate.R;
import edu.chapman.cpsc370.asdplaydate.fragments.FindFragment;
import edu.chapman.cpsc370.asdplaydate.fragments.FindFragmentContainer;
import edu.chapman.cpsc370.asdplaydate.helpers.DateHelpers;
import edu.chapman.cpsc370.asdplaydate.helpers.LocationHelpers;
import edu.chapman.cpsc370.asdplaydate.managers.SessionManager;
import edu.chapman.cpsc370.asdplaydate.models.ASDPlaydateUser;
import edu.chapman.cpsc370.asdplaydate.models.Broadcast;
import edu.chapman.cpsc370.asdplaydate.models.Child;
import edu.chapman.cpsc370.asdplaydate.models.Conversation;
import edu.chapman.cpsc370.asdplaydate.models.MarkerLabelInfo;

/**
 * Created by Kelly on 11/4/15.
 */
public class MarkerLabelAdapter implements GoogleMap.InfoWindowAdapter, GoogleMap.OnInfoWindowClickListener
{

    Context ctx;
    HashMap<LatLng, MarkerLabelInfo> data;
    FindFragment fragment;
    private SessionManager sessionManager;
    private LatLng markerLocation;
    private int tapCount;

    public MarkerLabelAdapter(FindFragment fragment, Context ctx, HashMap<LatLng, MarkerLabelInfo> data)
    {
        this.fragment = fragment;
        this.ctx = ctx;
        this.data = data;
        sessionManager = new SessionManager(ctx);
        markerLocation = null;
        tapCount = 0;
    }

    @Override
    public View getInfoWindow(Marker marker)
    {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker)
    {
        LayoutInflater inflater = LayoutInflater.from(ctx);
        View label = inflater.inflate(R.layout.marker_label, null);
        TextView parentName = (TextView) label.findViewById(R.id.tv_parent_name);
        TextView childAge = (TextView) label.findViewById(R.id.tv_child_age);
        TextView childName = (TextView) label.findViewById(R.id.tv_child_name);
        TextView childGender = (TextView) label.findViewById(R.id.tv_child_gender);
        TextView optionalMsg = (TextView) label.findViewById(R.id.tv_optional_message);
        TextView profileDistance = (TextView) label.findViewById(R.id.tv_profile_distance);
        TextView tapMessage = (TextView) label.findViewById(R.id.tv_tap_message);

        LatLng markerPos = marker.getPosition();
        if (data.containsKey(markerPos))
        {
            MarkerLabelInfo info = data.get(markerPos);

            // Set info here
            ASDPlaydateUser bcaster = info.getParent();
            Child child = info.getChild();
            parentName.setText(bcaster.getFirstName() + " " + bcaster.getLastName());
            childAge.setText(child.getAge() + " yr old");
            childName.setText(child.getFirstName());
            childGender.setText("(" + child.getGender().name().substring(0,1) + ")");
            optionalMsg.setText(child.getDescription());

            FindFragmentContainer container = (FindFragmentContainer) fragment.getParentFragment();
            ParseGeoPoint myPgp = LocationHelpers.toParseGeoPoint(container.myLocation);
            ParseGeoPoint broadcastPgp = LocationHelpers.toParseGeoPoint(markerPos);
            //TODO: Check rounding
            profileDistance.setText(Math.round(myPgp.distanceInMilesTo(broadcastPgp)) + " miles from you");
            tapMessage.setText(R.string.tap_twice_send_chat);
        }

        return label;
    }

    @Override
    public void onInfoWindowClick(Marker marker)
    {
        if (markerLocation == null)
        {
            markerLocation = marker.getPosition();
        }
        if (markerLocation.equals(marker.getPosition()))
        {
            // record taps
            if (tapCount == 0)
            {
                tapCount+=1;
            }
            else if (tapCount == 1)
            {
                // send chat request
                ASDPlaydateUser initiator = null;
                try
                {
                    initiator = (ASDPlaydateUser) ASDPlaydateUser.become(sessionManager.getSessionToken());
                    ASDPlaydateUser receiver = data.get(markerLocation).getParent();                      //TODO: get initiator's broadcast expiredate here
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
        }
        else
        {
            markerLocation = marker.getPosition();
            tapCount = 1;
        }
    }
}
