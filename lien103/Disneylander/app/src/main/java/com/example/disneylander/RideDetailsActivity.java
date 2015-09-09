package com.example.disneylander;


import com.example.disneylander.db.DbHelpers;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class RideDetailsActivity extends ActionBarActivity {

	public String toDoTempString = "";
	final static String FILENAME = "to_do_list_file";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ride_details);

		Intent intent = getIntent();
		final String rideIdString = intent
				.getStringExtra(RidesListFragment.EXTRA_MESSAGE);
		final int rideIdInt = Integer.parseInt(rideIdString);
		final RideModel currentRideModel = DbHelpers.GetRide(rideIdInt, this);// creates a model from the given ride ID
																				

		// Reference views by id
		final TextView rideNameTextView = (TextView) findViewById(R.id.NameOfRideTextView);
		final TextView timesRiddenTextView = (TextView) findViewById(R.id.TimesVisitedTextView);
		Button backButton = (Button) findViewById(R.id.backButton);
		Button justRodeButton = (Button) findViewById(R.id.justRodeButton);
		Button addTodoButton = (Button) findViewById(R.id.addTodoButton);

		rideNameTextView.setText(currentRideModel.m_rideName);// Set the ride
																// name in the
																// UI
		timesRiddenTextView.setText(DbHelpers.GetRiddenCount(currentRideModel,
				this) + " times!");// Set the number of times ridden

		backButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();// finishes activity
			}
		});

		justRodeButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				DbHelpers.AddToRideLog(currentRideModel,
						getApplicationContext());// Adds a ride to the ride log

				Toast.makeText(getApplicationContext(),
						"You rode " + currentRideModel.m_rideName + "!",
						Toast.LENGTH_LONG).show(); // show a toast confirming
													// the user just rode a
													// specific ride
				finish();
			}
		});

		// User adds item to their todo list
		addTodoButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				 currentRideModel.m_isToDo = true;//Adds the ride to the todo
				// list
				DbHelpers.UpdateRide(currentRideModel, getApplicationContext());

				Toast.makeText(
						getApplicationContext(),
						"You added " + currentRideModel.m_rideName
								+ " to your To Do List!",
						Toast.LENGTH_LONG).show(); // show a toast confirming
													// the user just marked TO
													// on a specific ride

				finish();// finishes activity
			}
		});

	}
}
