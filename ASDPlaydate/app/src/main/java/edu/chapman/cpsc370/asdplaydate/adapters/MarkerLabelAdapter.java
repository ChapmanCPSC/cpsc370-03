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
import edu.chapman.cpsc370.asdplaydate.fragments.FindFragment;
import edu.chapman.cpsc370.asdplaydate.fragments.FindFragmentContainer;
import edu.chapman.cpsc370.asdplaydate.helpers.LocationHelpers;
import edu.chapman.cpsc370.asdplaydate.helpers.RecyclerAdapterHelpers;
import edu.chapman.cpsc370.asdplaydate.managers.SessionManager;
import edu.chapman.cpsc370.asdplaydate.models.ASDPlaydateUser;
import edu.chapman.cpsc370.asdplaydate.models.Child;
import edu.chapman.cpsc370.asdplaydate.models.MarkerLabelInfo;

/**
 * Created by Kelly on 11/4/15.
 */
public class MarkerLabelAdapter implements GoogleMap.InfoWindowAdapter, GoogleMap.OnInfoWindowClickListener
{

    Context ctx;
    FindFragment fragment;
    private SessionManager sessionManager;

    public MarkerLabelAdapter(FindFragment fragment, Context ctx)
    {
        this.fragment = fragment;
        this.ctx = ctx;
        sessionManager = new SessionManager(ctx);
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

        FindFragmentContainer container = (FindFragmentContainer) fragment.getParentFragment();
        for (MarkerLabelInfo info : container.broadcasts)
        {
            if (info.getLatLng().equals(markerPos))
            {
                // Set info here
                ASDPlaydateUser bcaster = info.getParent();
                Child child = info.getChild();
                parentName.setText(bcaster.getFirstName() + " " + bcaster.getLastName());
                childAge.setText("Child: " + child.getAge() + " yr old");
                childName.setText(child.getFirstName());
                childGender.setText("(" + child.getGender().name().substring(0,1) + ")");
                optionalMsg.setText(child.getDescription());

                ParseGeoPoint myPgp = LocationHelpers.toParseGeoPoint(container.myLocation);
                ParseGeoPoint broadcastPgp = LocationHelpers.toParseGeoPoint(markerPos);
                double dist = myPgp.distanceInMilesTo(broadcastPgp);
                String roundedDist = new BigDecimal(String.valueOf(dist)).setScale(1, RoundingMode.HALF_UP).toPlainString();
                profileDistance.setText(roundedDist + " miles from you");
                tapMessage.setText(R.string.tap_send_chat);
                break;
            }
        }

        return label;
    }

    @Override
    public void onInfoWindowClick(Marker marker)
    {
        LatLng markerLocation = marker.getPosition();
        FindFragmentContainer container = (FindFragmentContainer) fragment.getParentFragment();
        for (MarkerLabelInfo info : container.broadcasts)
        {
            if (info.getLatLng().equals(markerLocation))
            {
                ASDPlaydateUser receiver = info.getParent();
                RecyclerAdapterHelpers.sendChatRequest(sessionManager, receiver, ctx, marker);
                container.broadcasts.remove(info.getIndex());
                if (container.adapter != null)
                {
                    container.adapter.notifyItemRemoved(info.getIndex());
                    container.adapter.notifyItemRangeChanged(info.getIndex(), container.broadcasts.size());
                }
                else
                {
                    container.needsNotify = true;
                }
                break;
            }
        }
    }
}
