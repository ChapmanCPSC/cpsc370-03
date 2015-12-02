package edu.chapman.cpsc370.asdplaydate.fragments;

import android.app.Fragment;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseQuery;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.chapman.cpsc370.asdplaydate.R;
import edu.chapman.cpsc370.asdplaydate.adapters.MarkerLabelAdapter;
import edu.chapman.cpsc370.asdplaydate.adapters.ResultListRecyclerAdapter;
import edu.chapman.cpsc370.asdplaydate.helpers.DateHelpers;
import edu.chapman.cpsc370.asdplaydate.helpers.LocationHelpers;
import edu.chapman.cpsc370.asdplaydate.managers.SessionManager;
import edu.chapman.cpsc370.asdplaydate.models.ASDPlaydateUser;
import edu.chapman.cpsc370.asdplaydate.models.Broadcast;
import edu.chapman.cpsc370.asdplaydate.models.Child;
import edu.chapman.cpsc370.asdplaydate.models.Conversation;
import edu.chapman.cpsc370.asdplaydate.models.MarkerLabelInfo;


public class FindFragmentContainer extends Fragment
{

    public ArrayList<MarkerLabelInfo> broadcasts;
    GoogleMap googleMap = null;
    public Location myLocation;
    private boolean showingResultList = false;
    public ResultListRecyclerAdapter listAdapter;
    public MarkerLabelAdapter labelAdapter;
    public SessionManager sessionManager;

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

            sessionManager = new SessionManager(getActivity());
        }

    }

    @Override
    public void onResume()
    {
        super.onResume();
        try
        {
            updateUI(sessionManager);
        }
        catch (Exception e)
        {
            e.printStackTrace();
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
        try
        {
            updateUI(sessionManager);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        getChildFragmentManager()
                .beginTransaction()
                .setCustomAnimations(
                        R.animator.flip_right_in, R.animator.flip_right_out,
                        R.animator.flip_left_in, R.animator.flip_left_out)
                .replace(R.id.find_container, new ResultListFragment())
                .addToBackStack(null)
                .commit();
        if (listAdapter != null)
        {
            listAdapter.notifyDataSetChanged();
        }
    }

    public void updateUI(SessionManager sm) throws Exception
    {
        final ASDPlaydateUser me = (ASDPlaydateUser) ASDPlaydateUser.getCurrentUser();

        ParseQuery<Conversation> queryI = new ParseQuery<>(Conversation.class);
        queryI.whereEqualTo(Conversation.ATTR_INITIATOR, me);
        ParseQuery<Conversation> queryR = new ParseQuery<>(Conversation.class);
        queryR.whereEqualTo(Conversation.ATTR_RECEIVER, me);

        //where I'm the initiator or the receiver
        List<ParseQuery<Conversation>> queries = new ArrayList<>();
        queries.add(queryI);
        queries.add(queryR);

        //and accepted state
        ParseQuery<Conversation> mainQuery = ParseQuery.or(queries);
        mainQuery.whereMatches(Conversation.ATTR_STATUS, Conversation.Status.ACCEPTED.name());

        List<Conversation> acceptedConvos = mainQuery.find();
        Set<ASDPlaydateUser> acceptedUsers = new HashSet<ASDPlaydateUser>();
        for (Conversation conversation : acceptedConvos)
        {
            ASDPlaydateUser user = (ASDPlaydateUser) conversation.get(Conversation.ATTR_INITIATOR);
            user.fetchIfNeeded();
            if (!user.getObjectId().equals(me.getObjectId()))
            {
                acceptedUsers.add(user);
            }
            else
            {
                acceptedUsers.add((ASDPlaydateUser) conversation.get(Conversation.ATTR_RECEIVER));
            }
        }

        List<ParseQuery<Broadcast>> queries1 = new ArrayList<>();
        ParseQuery<Broadcast> notMeQ = new ParseQuery<>(Broadcast.class);
        notMeQ.whereNotEqualTo(Broadcast.ATTR_BROADCASTER, me);
        queries1.add(notMeQ);
        for (ASDPlaydateUser user : acceptedUsers)
        {
            ParseQuery<Broadcast> userQ = new ParseQuery<>(Broadcast.class);
            userQ.whereNotEqualTo(Broadcast.ATTR_BROADCASTER, user);
            queries1.add(userQ);
        }

        ParseQuery<Broadcast> awesomeQuery = ParseQuery.or(queries1);
        awesomeQuery.whereGreaterThan(Broadcast.ATTR_EXPIRE_DATE, DateHelpers.UTCDate(DateTime.now()))
                .whereWithinMiles(Broadcast.ATTR_LOCATION,
                        new ParseGeoPoint(myLocation.getLatitude(), myLocation.getLongitude()), sm.getSearchRadius());

        awesomeQuery.findInBackground(new FindCallback<Broadcast>()
        {
            @Override
            public void done(List<Broadcast> list, ParseException e)
            {
                ArrayList<MarkerLabelInfo> info = new ArrayList<>();
                int index = 0;

                if(list.isEmpty() && !showingResultList)
                {
                    Toast.makeText(getActivity(), getString(R.string.no_results_found), Toast.LENGTH_SHORT).show();
                }
                else if(list.isEmpty() && showingResultList)
                {
                    if(ResultListFragment.noResults != null)
                    {
                        ResultListFragment.noResults.setText(getString(R.string.no_results_found));
                        ResultListFragment.noResults.setVisibility(View.VISIBLE);
                    }
                }
                else
                {
                    
                    for (Broadcast broadcast : list)
                    {
                        try
                        {
                            // .fetchIfNeeded() gets parent info, not just the parent objectId
                            ASDPlaydateUser bcaster = (ASDPlaydateUser) broadcast.getBroadcaster().fetchIfNeeded();
                            // need to fix the query, remove if statement later
                            if (bcaster.equals(me))
                            {
                                break;
                            }
                            Child child = getChildWithParent(bcaster);
                            LatLng latLng = LocationHelpers.toLatLng(broadcast.getLocation());
                            MarkerLabelInfo markerLabelInfo = new MarkerLabelInfo(bcaster, child, latLng);
                            markerLabelInfo.setIndex(index);
                            info.add(markerLabelInfo);
                        }
                        catch (Exception e1)
                        {
                            e1.printStackTrace();
                        }
                    }

                }
                broadcasts = info;
                onFinish();

            }
        });
    }

    private void onFinish()
    {
        placeMarkers(broadcasts);
        labelAdapter = new MarkerLabelAdapter(FindFragmentContainer.this, getActivity());
        googleMap.setInfoWindowAdapter(labelAdapter);
        googleMap.setOnInfoWindowClickListener(labelAdapter);
    }

    private void placeMarkers(ArrayList<MarkerLabelInfo> data)
    {
        googleMap.clear();
        for (int i = 0; i < data.size(); i++)
        {
            MarkerLabelInfo info = data.get(i);
            Marker marker = googleMap.addMarker(new MarkerOptions().position(info.getLatLng()));
            info.setMarker(marker);
            data.set(i, info);
        }
    }

    private Child getChildWithParent(ASDPlaydateUser parent) throws Exception
    {
        ParseQuery<Child> q = new ParseQuery<>(Child.class);
        q.whereEqualTo(Child.ATTR_PARENT, parent);
        return q.find().get(0);
    }
}
