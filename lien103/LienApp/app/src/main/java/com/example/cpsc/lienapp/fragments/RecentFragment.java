package com.example.cpsc.lienapp.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.example.cpsc.lienapp.R;
import com.example.cpsc.lienapp.MainActivity;
import com.example.cpsc.lienapp.db.EmailDataProvider;
import com.example.cpsc.lienapp.db.RecentEmailAdapter;
import com.example.cpsc.lienapp.db.RecentEmailModel;

import java.util.List;

/**
 * Created by IsaacLien on 10/6/15.
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

//Test Code Starts HERE
        //get locations from db
        List<RecentEmailModel> locations = EmailDataProvider.GetRecentEmails(getActivity());

        RecentEmailAdapter adapter = new RecentEmailAdapter(getActivity(), locations);

        recentList.setAdapter(adapter);
//Test Code Ends HERE

        return rootView;
    }

    @Override
    public void onAttach(Activity act)
    {
        super.onAttach(act);
        ((MainActivity) act).onSectionAttached(2);
    }
}

