package edu.chapman.cpsc370.asdplaydate.fragments;

import android.app.Fragment;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.model.LatLng;
import com.parse.ParseGeoPoint;
import com.parse.ParseQuery;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import edu.chapman.cpsc370.asdplaydate.R;
import edu.chapman.cpsc370.asdplaydate.adapters.MarkerLabelAdapter;
import edu.chapman.cpsc370.asdplaydate.adapters.ResultListRecyclerAdapter;
import edu.chapman.cpsc370.asdplaydate.helpers.DateHelpers;
import edu.chapman.cpsc370.asdplaydate.helpers.LocationHelpers;
import edu.chapman.cpsc370.asdplaydate.managers.SessionManager;
import edu.chapman.cpsc370.asdplaydate.models.ASDPlaydateUser;
import edu.chapman.cpsc370.asdplaydate.models.Broadcast;
import edu.chapman.cpsc370.asdplaydate.models.Child;
import edu.chapman.cpsc370.asdplaydate.models.MarkerLabelInfo;


public class FindFragmentContainer extends Fragment
{

    public ArrayList<MarkerLabelInfo> broadcasts;
    public Location myLocation;
    private boolean showingResultList = false;
    public ResultListRecyclerAdapter adapter;
    public MarkerLabelAdapter mla;
    public boolean needsNotify;

    public FindFragmentContainer()
    {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_find_container, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        // Load the map fragment into the container initially
        if (savedInstanceState == null)
        {
            getChildFragmentManager()
                    .beginTransaction()
                    .add(R.id.find_container, new FindFragment())
                    .commit();
        }

    }

    public void flipFragment()
    {
        // Flip back to the map
        if (showingResultList)
        {
            showingResultList = false;
            getChildFragmentManager().popBackStack();
            return;
        }

        // Flip to result list
        showingResultList = true;
        getChildFragmentManager()
                .beginTransaction()
                .setCustomAnimations(
                        R.animator.flip_right_in, R.animator.flip_right_out,
                        R.animator.flip_left_in, R.animator.flip_left_out)
                .replace(R.id.find_container, new ResultListFragment())
                .addToBackStack(null)
                .commit();
        if (adapter != null)
        {
            adapter.notifyDataSetChanged();
        }
    }

    ArrayList<MarkerLabelInfo> getBroadcasts(SessionManager sm) throws Exception
    {
        ASDPlaydateUser user = (ASDPlaydateUser) ASDPlaydateUser.getCurrentUser();

        ParseQuery<Broadcast> q = new ParseQuery<>(Broadcast.class);
        q.whereGreaterThan(Broadcast.ATTR_EXPIRE_DATE, DateHelpers.UTCDate(DateTime.now()))
                .whereWithinMiles(Broadcast.ATTR_LOCATION,
                        new ParseGeoPoint(myLocation.getLatitude(), myLocation.getLongitude()), sm.getSearchRadius())
                .whereNotEqualTo(Broadcast.ATTR_BROADCASTER, user);

        List<Broadcast> list = q.find();
        ArrayList<MarkerLabelInfo> data = new ArrayList<>();
        int index = 0;
        for (Broadcast broadcast : list)
        {
            // .fetchIfNeeded() gets parent info, not just the parent objectId
            ASDPlaydateUser bcaster = (ASDPlaydateUser) broadcast.getBroadcaster().fetchIfNeeded();
            Child child = getChildWithParent(bcaster);
            LatLng latLng = LocationHelpers.toLatLng(broadcast.getLocation());
            MarkerLabelInfo markerLabelInfo = new MarkerLabelInfo(bcaster, child, latLng);
            markerLabelInfo.setIndex(index);
            data.add(markerLabelInfo);
        }

        return data;
    }

    private Child getChildWithParent(ASDPlaydateUser parent) throws Exception
    {
        ParseQuery<Child> q = new ParseQuery<>(Child.class);
        q.whereEqualTo(Child.ATTR_PARENT, parent);
        return q.find().get(0);
    }
}
