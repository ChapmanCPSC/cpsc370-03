package com.example.cpsc.demoapplication.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.cpsc.demoapplication.R;
import com.example.cpsc.demoapplication.activities.MainActivity;
import com.example.cpsc.demoapplication.adapters.RecentLocationAdapter;
import com.example.cpsc.demoapplication.db.WeatherDataProvider;
import com.example.cpsc.demoapplication.models.RecentLocationModel;

import java.util.List;

/**
 * Created by cpsc on 9/16/15.
 */
public class RecentFragment extends Fragment
{
    public RecentFragment()
    {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_recent, container, false);

        ListView recentList = (ListView) rootView.findViewById(R.id.lv_locations);

        //get locations from db
        List<RecentLocationModel> locations = WeatherDataProvider.GetRecentLocations(getActivity());

        RecentLocationAdapter adapter = new RecentLocationAdapter(getActivity(), locations);

        recentList.setAdapter(adapter);

        return rootView;
    }

    @Override
    public void onAttach(Activity act)
    {
        super.onAttach(act);
        ((MainActivity) act).onSectionAttached(2);
    }
}
