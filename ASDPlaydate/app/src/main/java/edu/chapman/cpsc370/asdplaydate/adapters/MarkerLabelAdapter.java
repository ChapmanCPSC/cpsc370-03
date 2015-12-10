package edu.chapman.cpsc370.asdplaydate.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.parse.ParseGeoPoint;

import java.math.BigDecimal;
import java.math.RoundingMode;

import edu.chapman.cpsc370.asdplaydate.R;
import edu.chapman.cpsc370.asdplaydate.fragments.FindFragmentContainer;
import edu.chapman.cpsc370.asdplaydate.helpers.LocationHelpers;
import edu.chapman.cpsc370.asdplaydate.helpers.RecyclerAdapterHelpers;
import edu.chapman.cpsc370.asdplaydate.models.ASDPlaydateUser;
import edu.chapman.cpsc370.asdplaydate.models.Child;
import edu.chapman.cpsc370.asdplaydate.models.MarkerLabelInfo;

/**
 * Created by Kelly on 11/4/15.
 */
public class MarkerLabelAdapter implements GoogleMap.InfoWindowAdapter, GoogleMap.OnInfoWindowClickListener
{

    FindFragmentContainer parent;
    Context ctx;

    public MarkerLabelAdapter(FindFragmentContainer parent, Context ctx)
    {
        this.parent = parent;
        this.ctx = ctx;
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
        TextView broadcastMsg = (TextView) label.findViewById(R.id.tv_broadcast_msg);
        TextView tapMessage = (TextView) label.findViewById(R.id.tv_tap_message);

        LatLng markerPos = marker.getPosition();

        for (MarkerLabelInfo info : parent.broadcasts)
        {
            if (info.getLatLng().equals(markerPos))
            {
                // Set info here
                ASDPlaydateUser bcaster = info.getParent();
                Child child = info.getChild();
                parentName.setText(bcaster.getFirstName() + " " + bcaster.getLastName());
                childAge.setText(ctx.getString(R.string.child) + ": " + child.getAge() + " yr old");
                childName.setText(child.getFirstName());
                childGender.setText("(" + child.getGender().name().substring(0,1) + ")");
                optionalMsg.setText(child.getDescription());

                ParseGeoPoint myPgp = LocationHelpers.toParseGeoPoint(parent.myLocation);
                ParseGeoPoint broadcastPgp = LocationHelpers.toParseGeoPoint(markerPos);
                double dist = myPgp.distanceInMilesTo(broadcastPgp);
                String roundedDist = new BigDecimal(String.valueOf(dist)).setScale(1, RoundingMode.HALF_UP).toPlainString();
                profileDistance.setText(roundedDist + " " + ctx.getString(R.string.miles_from_you));

                String message = bcaster.getLastBroadcast().getMessage();
                if (message.length() > 0)
                {
                    broadcastMsg.setText(ctx.getString(R.string.broadcast_msg_label) + "\n" + message);
                } else
                {
                    broadcastMsg.setVisibility(View.GONE);
                }

                if (!info.hasConversation())
                {
                    tapMessage.setText(R.string.tap_send_chat);
                }
                else
                {
                    tapMessage.setVisibility(View.GONE);
                }
                break;
            }
        }

        return label;
    }

    @Override
    public void onInfoWindowClick(Marker marker)
    {
        LatLng markerLocation = marker.getPosition();
        for (MarkerLabelInfo info : parent.broadcasts)
        {
            if (info.getLatLng().equals(markerLocation))
            {
                if (!info.hasConversation())
                {
                    ASDPlaydateUser receiver = info.getParent();
                    RecyclerAdapterHelpers.sendChatRequest(receiver, ctx, marker);
                    parent.broadcasts.remove(info.getIndex());
                    if (parent.listAdapter != null)
                    {
                        parent.listAdapter.notifyItemRemoved(info.getIndex());
                        parent.listAdapter.notifyItemRangeChanged(info.getIndex(), parent.broadcasts.size());
                    }
                }
                break;
            }
        }
    }
}
