package com.example.disneylander;

import com.example.disneylander.db.DbHelpers;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
 
 public class StatsFragment extends Fragment {
     
    public StatsFragment(){}
     
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
  
        View rootView = inflater.inflate(R.layout.fragment_stats, container, false);
         
        //reference views by id
        TextView mostRiddenRideView = (TextView) rootView.findViewById(R.id.MostRiddenRideTextView);
        TextView totalRideCountView = (TextView) rootView.findViewById(R.id.totalRideCountView);
        
        mostRiddenRideView.setText(DbHelpers.GetMostRiddenRide(getActivity()));//Set the most ridden ride
        totalRideCountView.setText(DbHelpers.GetTotalRiddenCount(getActivity()));//Set the ridden count
        
        return rootView;
    }
}