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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class UpdateEvent extends Activity{
	
	TextView uEvent;
	EditText uDate, uTime, uPlace;
	ListView lv;
	String eventName, eventDate, eventTime, eventPlace;
	Long eventId;
	Button editListBtn;
	Event event;
	
	static boolean updatedEvent;
	boolean editedList;
	
	AlertDialog.Builder save;
	
	final ArrayList<Long> mSelectedItems = new ArrayList<Long>();
	
	private EventDataSource datasource;
	private MemberDataSource mds;
	
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_event);
        
        datasource = new EventDataSource(this);
        datasource.open();
        
        mds = new MemberDataSource(this);
        mds.open();
        
        uEvent = (TextView)findViewById(R.id.uEvent1);
        uDate = (EditText)findViewById(R.id.uDate);
        uTime = (EditText)findViewById(R.id.uTime);
        uPlace = (EditText)findViewById(R.id.uPlace);
        lv = (ListView)findViewById(android.R.id.list);
        editListBtn = (Button)findViewById(R.id.editListBtn);
        
        Intent intent = getIntent();
        eventId = intent.getLongExtra("com.chapman.hawaiiclub.eventid", 0);
        
        event = datasource.getEvent(eventId);
        
        if(!AddEvent.clickedOkay){
        	
        } else {
        
        List<String> values = mds.getNamesAttendingArray(event.getEventName());
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.list_item, values);
        lv.setAdapter(adapter);
        }
        
        uEvent.setText(event.getEventName());
        uDate.setText(event.getEventDate());
        uTime.setText(event.getEventTime());
        uPlace.setText(event.getEventPlace());
        
        datasource.close();
        mds.close();
        
        editListBtn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				
				mds.open();
				// Obtain list of all members in the club and convert it to a CharSequence array 
				List<String> members = mds.getAllMembersArray();
				CharSequence[] cs = members.toArray(new CharSequence[members.size()]);
				mds.close();
				
				AlertDialog.Builder edit = new AlertDialog.Builder(UpdateEvent.this);
				edit.setTitle("Edit Members Attending");
				edit.setMultiChoiceItems(cs, null, new DialogInterface.OnMultiChoiceClickListener() {
					
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
				edit.setPositiveButton("Save", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						editedList = true;
						
						
					}
				});
				edit.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						
						editedList = false;
						
					}
				});
				edit.create().show();
				
			}
        	
        });
        
        
	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.update_event_menu, menu);
        return true;
    }
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.save_event) {
        	
        	save = new AlertDialog.Builder(UpdateEvent.this);
        	save.setIcon(android.R.drawable.ic_menu_save);
        	save.setTitle("Save changes?");
        	save.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
				
				@Override
			 	public void onClick(DialogInterface dialog, int which) {
					updatedEvent = true;
					
					// Obtain string values from the EditTexts
					eventName = uEvent.getText().toString();
		        	eventDate = uDate.getText().toString();
		        	eventTime = uTime.getText().toString();
		        	eventPlace = uPlace.getText().toString();
		        	
		        	datasource.open();
		        	// Update the event with the new values
		        	datasource.updateEvent(eventId, eventName, eventDate, eventTime, eventPlace);
		        	datasource.close();
		        	
		        	if(editedList){
			        	mds.open();
			        	
			        	// Update members attending table 
			        	mds.clickedOkay(mSelectedItems, eventId, event.getEventName(), true);
			        	
			        	mds.close();
		        	}
		    
		        	Intent intent = new Intent(UpdateEvent.this, EventsList.class);
		        	startActivity(intent);
					
				}
			});
        	save.setNegativeButton("No", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					updatedEvent = false;
					
					// Return to list of events 
					Intent intent = new Intent(UpdateEvent.this, EventsList.class);
		        	startActivity(intent);
					
				}
			});
        	save.create().show();
        	
            return true;
        }
        
        return super.onOptionsItemSelected(item);
    }

}
