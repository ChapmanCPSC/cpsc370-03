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
    public GoogleMap googleMap = null;
    public Location myLocation;
    private boolean showingResultList = false;
    public ResultListRecyclerAdapter listAdapter;
    public MarkerLabelAdapter labelAdapter;
    public SessionManager sessionManager;
    public boolean broadcasted = false;

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
            if(showingResultList){
                updateUI();
            }

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

    public void updateUI() throws Exception
    {
        if (myLocation == null)
        {
            return;
        }

        // Get the current user
        final ASDPlaydateUser me = (ASDPlaydateUser) ASDPlaydateUser.getCurrentUser();

        // Query to get conversations where
        ParseQuery<Conversation> queryI = new ParseQuery<>(Conversation.class);
        queryI.whereEqualTo(Conversation.ATTR_INITIATOR, me); // I am the initiator
        ParseQuery<Conversation> queryR = new ParseQuery<>(Conversation.class);
        queryR.whereEqualTo(Conversation.ATTR_RECEIVER, me); // I am the receiver

        List<ParseQuery<Conversation>> queries = new ArrayList<>();
        queries.add(queryI);
        queries.add(queryR);

        ParseQuery<Conversation> initOrRec = ParseQuery.or(queries); // I am the initiator or the receiver
        initOrRec.whereMatches(Conversation.ATTR_STATUS, Conversation.Status.ACCEPTED.name()); // and conversation Status is ACCEPTED

        ParseQuery<Conversation> pend = initOrRec; // I am the initiator or the receiver
        pend.whereMatches(Conversation.ATTR_STATUS, Conversation.Status.PENDING.name()); // and conversation status is pending

        queries.clear();
        queries.add(initOrRec);
        queries.add(pend);

        ParseQuery<Conversation> convoQuery = ParseQuery.or(queries);
        convoQuery.whereGreaterThan(Conversation.ATTR_EXPIRE_DATE, DateHelpers.UTCDate(DateTime.now()));
        final Set<String> acceptedUserIds = new HashSet<>(); // Store ids of users who have valid conversation with the current user
        convoQuery.findInBackground(new FindCallback<Conversation>()
        {
            @Override
            public void done(List<Conversation> objects, ParseException e)
            {
                for (Conversation convo : objects)
                {
                    String initiatorId = convo.getInitiator().getObjectId();
                    if (me.getObjectId().equals(initiatorId))
                    {
                        acceptedUserIds.add(convo.getReceiver().getObjectId());
                    }
                    else
                    {
                        acceptedUserIds.add(initiatorId);
                    }
                }
                onFindAcceptedUsersFinish(me, acceptedUserIds);
            }
        });
    }

    private void onFindAcceptedUsersFinish(ASDPlaydateUser me, final Set<String> acceptedUserIds)
    {
        // Query to get broadcasts where
        ParseQuery<Broadcast> mainQuery = new ParseQuery<>(Broadcast.class);
        mainQuery.whereGreaterThan(Broadcast.ATTR_EXPIRE_DATE, DateHelpers.UTCDate(DateTime.now())) // expire date is beyond now
                .orderByDescending(Broadcast.ATTR_EXPIRE_DATE) // latest broadcasts first
                .whereWithinMiles(Broadcast.ATTR_LOCATION,
                        new ParseGeoPoint(myLocation.getLatitude(), myLocation.getLongitude()), sessionManager.getSearchRadius()) // within the broadcast radius
                .whereNotEqualTo(Broadcast.ATTR_BROADCASTER, me); // not my own broadcasts

        mainQuery.findInBackground(new FindCallback<Broadcast>()
        {
            @Override
            public void done(List<Broadcast> list, ParseException e)
            {
                ArrayList<MarkerLabelInfo> info = new ArrayList<>();
                int index = 0;

                if (list.isEmpty() && !showingResultList)
                {
                    Toast.makeText(getActivity(), getString(R.string.no_results_found), Toast.LENGTH_SHORT).show();
                }
                else if (list.isEmpty() && showingResultList)
                {
                    if (ResultListFragment.noResults != null)
                    {
                        ResultListFragment.noResults.setText(getString(R.string.no_results_found));
                        ResultListFragment.noResults.setVisibility(View.VISIBLE);
                    }
                }
                else
                {
                    Set<ASDPlaydateUser> users = new HashSet<>(); // Prevent multiple broadcasts from showing by getting the latest broadcast
                    for (Broadcast broadcast : list)
                    {
                        try
                        {
                            ASDPlaydateUser bcaster = (ASDPlaydateUser) broadcast.getBroadcaster().fetchIfNeeded();
                            if (!users.contains(bcaster))
                            {
                                Child child = getChildWithParent(bcaster);
                                LatLng latLng = LocationHelpers.toLatLng(broadcast.getLocation());
                                MarkerLabelInfo markerLabelInfo = new MarkerLabelInfo(bcaster, child, latLng);
                                markerLabelInfo.setIndex(index);
                                if (acceptedUserIds.contains(broadcast.getBroadcaster().getObjectId()))
                                {
                                    markerLabelInfo.setHasConversation(true);
                                }
                                info.add(markerLabelInfo);
                                users.add(bcaster);
                            }
                        }
                        catch (Exception e1)
                        {
                            e1.printStackTrace();
                        }
                    }

                }
                broadcasts = info;
                onBroadcastQueryFinish();

            }
        });
    }

    private void onBroadcastQueryFinish()
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
