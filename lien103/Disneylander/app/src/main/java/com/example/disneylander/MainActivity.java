package com.example.disneylander;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.json.JSONObject;

import com.example.disneylander.db.DbHelpers;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.support.v4.widget.DrawerLayout;

public class MainActivity extends ActionBarActivity implements
		NavigationDrawerFragment.NavigationDrawerCallbacks {

	//Isaac Lien Project Notes
	//Everything in this project should work without breaking (to my knowledge.
	//Improvements I would make would be better performace using the database, as sometimes it seems a little slow when I am loading a fragment that makes a database lookup, particularly on that "Stats" fragment
	
	/**
	 * Fragment managing the behaviors, interactions and presentation of the
	 * navigation drawer.
	 */
	private NavigationDrawerFragment mNavigationDrawerFragment;

	/**
	 * Used to store the last screen title. For use in
	 * {@link #restoreActionBar()}.
	 */
	private CharSequence mTitle;
	final int result_finished = 2000;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//Read the Rides List into the Database from a text file
		 final ArrayList<String> ridesList = new ArrayList<String>(); //creates an array to populate the list

			try {
				BufferedReader reader = new BufferedReader(
				        new InputStreamReader(this.getAssets().open("rides.txt")));
				String mLine = reader.readLine();
			    while (mLine != null) {
			    	ridesList.add(mLine);//Add valid ride to the list	    	
			        mLine = reader.readLine();//go to the next line
			    }
				
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		
		for (int i = 0; i < ridesList.size(); i++)//Insert the rides into the database
		{
			RideModel tempRideModel = new RideModel(false, i, ridesList.get(i));
			DbHelpers.InsertRide(tempRideModel, this);
		}
		
			//End Test Read in rides from text file
		
		
		mNavigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager()
				.findFragmentById(R.id.navigation_drawer);
		mTitle = getTitle();

		// Set up the drawer.
		mNavigationDrawerFragment.setUp(R.id.navigation_drawer,
				(DrawerLayout) findViewById(R.id.drawer_layout));
	}

	@Override
	public void onNavigationDrawerItemSelected(int position) {
		// update the main content by replacing fragments
		FragmentManager fragmentManager = getSupportFragmentManager();
		fragmentManager
				.beginTransaction()
				.replace(R.id.container,
						PlaceholderFragment.newInstance(position + 1)).commit();
	}

	public void onSectionAttached(int number) {

		// ToDoFragment newFragment; OLD CODE 1
		Fragment fragment;
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction transaction = fm.beginTransaction();

		switch (number) {
		case 1:// Main Menu Tab
			mTitle = getString(R.string.title_section1);
			break;
		case 2:// To-Do List Tab
			mTitle = getString(R.string.title_section2);
			fragment = new ToDoFragment();
			transaction.replace(R.id.container, fragment);
			transaction.addToBackStack(null);
			transaction.commit();
			break;
		case 3:// My Stats Tab
			mTitle = getString(R.string.title_section3);
			fragment = new StatsFragment();
			transaction.replace(R.id.container, fragment);
			transaction.addToBackStack(null);
			transaction.commit();
			break;
		case 4:// Map Tab
			mTitle = getString(R.string.title_section4);
			fragment = new MapFragment();
			transaction.replace(R.id.container, fragment);
			transaction.addToBackStack(null);
			transaction.commit();
			break;
		case 5:// Rides List Tab
			mTitle = getString(R.string.title_section5);
			fragment = new RidesListFragment();
			transaction.replace(R.id.container, fragment);
			transaction.addToBackStack(null);
			transaction.commit();
			break;
		default:
			break;
		}
	}

	public void restoreActionBar() {
		ActionBar actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setTitle(mTitle);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (!mNavigationDrawerFragment.isDrawerOpen()) {
			// Only show items in the action bar relevant to this screen
			// if the drawer is not showing. Otherwise, let the drawer
			// decide what to show in the action bar.
			getMenuInflater().inflate(R.menu.main, menu);
			restoreActionBar();
			return true;
		}
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) 
	{
		if (requestCode == result_finished)
		{
			onNavigationDrawerItemSelected(4);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		private static final String ARG_SECTION_NUMBER = "section_number";

		/**
		 * Returns a new instance of this fragment for the given section number.
		 */
		
		
		public static PlaceholderFragment newInstance(int sectionNumber) {
			PlaceholderFragment fragment = new PlaceholderFragment();
			Bundle args = new Bundle();
			args.putInt(ARG_SECTION_NUMBER, sectionNumber);
			fragment.setArguments(args);
			
			
			return fragment;
		}

		public PlaceholderFragment() {
			
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,//Edit the main fragment here
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			
			//reference views by ID
			TextView currentTemp = (TextView) rootView.findViewById(R.id.CurrentTempuratureTextView);
			//currentTemp.setText("71");

			FindWeather fw = new FindWeather(getActivity(), currentTemp);
			try {
				fw.execute();
			} catch (Exception e) {
				
			}
			
			return rootView;
		}

		
		@Override
		public void onAttach(Activity activity) {
			super.onAttach(activity);
			((MainActivity) activity).onSectionAttached(getArguments().getInt(
					ARG_SECTION_NUMBER));
		}
	}

}
