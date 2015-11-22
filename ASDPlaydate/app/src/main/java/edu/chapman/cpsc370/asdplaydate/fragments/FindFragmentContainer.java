package edu.chapman.cpsc370.asdplaydate.fragments;

import android.app.Fragment;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.model.LatLng;

import java.util.HashMap;

import edu.chapman.cpsc370.asdplaydate.R;
import edu.chapman.cpsc370.asdplaydate.models.MarkerLabelInfo;


public class FindFragmentContainer extends Fragment
{

    HashMap<LatLng, MarkerLabelInfo> broadcasts;
    public Location myLocation;
    private boolean showingResultList = false;

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
    }
}
