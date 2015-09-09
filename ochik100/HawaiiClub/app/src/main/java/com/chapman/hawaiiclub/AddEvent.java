package com.chapman.hawaiiclub;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddEvent extends Activity{
	
	// remove from this list when event is deleted, drop table for event too
	static List<String> eventsList = new ArrayList<String>();
	
	boolean membersadded;
	static boolean clickedOkay;
	
	EditText eEvent, eDate, eTime, ePlace;
	String event, date, time, place;
	Button submitEvent, eventList, selectMembersBtn;
	
	AlertDialog.Builder dialog;
	final ArrayList<Long> mSelectedItems = new ArrayList<Long>();
	
	private EventDataSource datasource;
	private MemberDataSource memberDatasource;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);
        
        for(String i : eventsList){
    		System.out.println(i);
    	}
        
        clickedOkay = false;
        
        datasource = new EventDataSource(this);
        datasource.open();
        
        membersadded = false;
        
        eEvent = (EditText)findViewById(R.id.eEvent);
        eDate = (EditText)findViewById(R.id.eDate);
        eTime = (EditText)findViewById(R.id.eTime);
        ePlace = (EditText)findViewById(R.id.ePlace);
        
        submitEvent = (Button)findViewById(R.id.submitEvent);
        eventList = (Button)findViewById(R.id.eventList);
        selectMembersBtn = (Button)findViewById(R.id.selectMembersBtn);
        
        selectMembersBtn.setOnClickListener(new OnClickListener(){
        	
			@Override
			public void onClick(View v) {
				membersadded = true;
				
				memberDatasource = new MemberDataSource(AddEvent.this);
				memberDatasource.open();
				
				// Obtain a list of all members in the club and convert it to a CharSequence array
				List<String> members = memberDatasource.getAllMembersArray();
				CharSequence[] cs = members.toArray(new CharSequence[members.size()]);
				
				memberDatasource.close();
				
				// Creating a dialog to allow the user to select members attending an event
				dialog = new AlertDialog.Builder(AddEvent.this);
				dialog.setTitle("Members Attending");
				dialog.setMultiChoiceItems(cs, null, new DialogInterface.OnMultiChoiceClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which, boolean isChecked) {
						
						
						long _which = Long.valueOf(which);
						
						if (isChecked) {
	                       // If the user checked the item, add it to the selected items
	                       mSelectedItems.add(_which);
	                           
	                    } else if (mSelectedItems.contains(_which)) {
	                       // Else, if the item is already in the array, remove it 
	                       mSelectedItems.remove(_which);

	                    }
						
					}
				});
				dialog.setPositiveButton("Save", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						clickedOkay = true;
						
					}
				})
				.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						clickedOkay = false;
						dialog.cancel();
					}
				});
		
				
				dialog.create();
				dialog.show();
				
				
			}
        	
        });
        
        eventList.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				datasource.close();
				
				Intent intent = new Intent(AddEvent.this, EventsList.class);
				startActivity(intent);
				
			}
        	
        });
        
        submitEvent.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				
				// Obtain user input for Event info
				event = eEvent.getText().toString();
		        date = eDate.getText().toString();
		        time = eTime.getText().toString();
		        place = ePlace.getText().toString();
				
				if(!clickedOkay){
					
					// Display toast if no members were selected
					Toast.makeText(AddEvent.this, "At least one member must attend an event", Toast.LENGTH_SHORT).show();
					
				}else if(event.equals("") || date.equals("") || time.equals("") || place.equals("")){
					
					// Display toast if a field is left blank
		        	Toast.makeText(AddEvent.this, "A required field is blank", Toast.LENGTH_SHORT).show();
		        	
		        } else {
		        	
		        	// Add event to database
		        	Event _event = null;
					_event = datasource.addEvent(event, date, time, place);
					
					// Add event to ListView 
					EventsList.adapter.add(_event);
					eventsList.add(event);
					
					if(!clickedOkay){
						
						// Display toast if no members were selected
						Toast.makeText(AddEvent.this, "Must select members attending", Toast.LENGTH_SHORT).show();
					}
					
					if(clickedOkay){
						
						// Save members attending to database 
						memberDatasource.open();
						long eventId = _event.getId();
						memberDatasource.clickedOkay(mSelectedItems, eventId, event, false);
					}
					
					datasource.close();
					memberDatasource.close();
					
					
					Intent intent = new Intent(AddEvent.this, EventsList.class);
					startActivity(intent);
		        	
		        }
				
				
				
				
			}
        	
        });
        
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_page, menu);
        return true;
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

}

