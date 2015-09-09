package com.example.disneylander;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
 
 public class RidesListFragment extends Fragment {
     
    public RidesListFragment(){}
    public final static String EXTRA_MESSAGE = "com.example.ridesapp.MESSAGE";
    
 
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    	
    	//Don't Change this code --->
        final View rootView = inflater.inflate(R.layout.fragment_rideslist, container, false);
        //Don't change this code <---
        
        //Reference Views by ID
        ListView ridesList = (ListView) rootView.findViewById(R.id.ridesListView);
        
        //Begin list filling code ---->
        final ArrayList<String> displayList = new ArrayList<String>(); //creates an array to populate the list

		try {
			BufferedReader reader = new BufferedReader(
			        new InputStreamReader(getActivity().getApplicationContext().getAssets().open("rides.txt")));
			String mLine = reader.readLine();
		    while (mLine != null) {
		    	displayList.add(mLine);//Add valid ride to the list	    	
		        mLine = reader.readLine();//go to the next line
		    }
			
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    
    	ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(getActivity(),R.layout.simplerow, displayList);
    	ridesList.setAdapter( listAdapter );  
        //End List Filling Code <---- 
    	
    	
    	ridesList.setOnItemClickListener(new OnItemClickListener() {

        	//click listener for items in the movie list
            @Override  
            public void onItemClick(AdapterView<?> parent, View view, int position,
                    long id) {

            	startDetailsActivity(rootView,position);
            }
        }); 
        
        return rootView;
    }

    public void startDetailsActivity(View view, int rideId) {
        // Start ride details activity intent
    	Intent intent = new Intent(getActivity().getApplicationContext(), RideDetailsActivity.class);  
    	
    	intent.putExtra(EXTRA_MESSAGE, Integer.toString(rideId));///sends the ride id as a string to the new activity
    	startActivityForResult(intent, 4000);
    }
 }