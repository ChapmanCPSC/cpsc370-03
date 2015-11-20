package edu.chapman.cpsc370.asdplaydate.adapters;

import android.content.Context;
import android.location.Location;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.parse.ParseGeoPoint;

import java.util.HashMap;
import java.util.List;

import edu.chapman.cpsc370.asdplaydate.R;
import edu.chapman.cpsc370.asdplaydate.fragments.FindFragment;
import edu.chapman.cpsc370.asdplaydate.models.ASDPlaydateUser;
import edu.chapman.cpsc370.asdplaydate.models.Child;
import edu.chapman.cpsc370.asdplaydate.models.MarkerLabelInfo;

/**
 * Created by Kelly on 11/4/15.
 */
public class MarkerLabelAdapter implements GoogleMap.InfoWindowAdapter
{

    Context ctx;
    HashMap<LatLng, MarkerLabelInfo> data;
    FindFragment fragment;

    public MarkerLabelAdapter(FindFragment fragment, Context ctx, HashMap<LatLng, MarkerLabelInfo> data)
    {
        this.fragment = fragment;
        this.ctx = ctx;
        this.data = data;
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
        TextView chatRequest = (Button) label.findViewById(R.id.btn_chat_request);

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

            ParseGeoPoint myPgp = toParseGeoPoint(fragment.myLocation);
            ParseGeoPoint broadcastPgp = toParseGeoPoint(markerPos);
            //TODO: Check rounding
            profileDistance.setText(Math.round(myPgp.distanceInMilesTo(broadcastPgp)) + " miles from you");
        }

        chatRequest.setOnClickListener(onClickListener);

        return label;
    }

    public ParseGeoPoint toParseGeoPoint(Location location)
    {
        return new ParseGeoPoint(location.getLatitude(), location.getLongitude());
    }

    public ParseGeoPoint toParseGeoPoint(LatLng latLng)
    {
        return new ParseGeoPoint(latLng.latitude, latLng.longitude);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            switch(v.getId())
            {
                case R.id.btn_chat_request:
                    // TODO: Open chat
                    break;
                default:
                    break;
            }
        }
    };
}
