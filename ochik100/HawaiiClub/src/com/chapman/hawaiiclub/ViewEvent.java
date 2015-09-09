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
import android.widget.ListView;
import android.widget.TextView;

public class ViewEvent extends Activity{
	
	long eventsID;
	TextView viewEvent, viewDate, viewTime, viewPlace;
	ListView lv;
	Button reminderBtn;
	Long eventID;
	
	AlertDialog.Builder delete;
	static ArrayAdapter<String> adapter;
	
	Event event;
	
	private EventDataSource datasource;
	private MemberDataSource mds;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_event);
        
        datasource = new EventDataSource(this);
        mds = new MemberDataSource(this);
        
        datasource.open();
        
        viewEvent = (TextView)findViewById(R.id.viewEvent);
        viewDate = (TextView)findViewById(R.id.viewDate);
        viewTime = (TextView)findViewById(R.id.viewTime);
        viewPlace = (TextView)findViewById(R.id.viewPlace);
        lv = (ListView)findViewById(android.R.id.list);
        reminderBtn = (Button)findViewById(R.id.reminderBtn);
        
        // Get eventID from the intent passed 
        Intent intent = getIntent();
        eventID = intent.getLongExtra("EVENT_LIST_ID", 0);
        System.out.println("Event id = " + eventID);
        
        // Using the eventID to retrieve the event
        event = datasource.getEvent(eventID);
        
        // Displaying the event information
        viewEvent.setText(event.getEventName());
        viewDate.setText(event.getEventDate());
        viewTime.setText(event.getEventTime());
        viewPlace.setText(event.getEventPlace());
        
        if(!AddEvent.clickedOkay){
        	
        } else {
        
	        if(UpdateEvent.updatedEvent){
	        	
	        	UpdateEvent.updatedEvent = false;
	        	
	        	mds = new MemberDataSource(this);
	        	mds.open();
	            
	        	adapter.clear();
	            List<String> values = mds.getNamesAttendingArray(event.getEventName());
	            adapter = new ArrayAdapter<String>(this, R.layout.list_item, values);
	            lv.setAdapter(adapter);
	            mds.countAttending(event.getEventName());
	            
	            mds.close();
	        	
	        	
	        } else {
	        	
	        	UpdateEvent.updatedEvent = false;
	        	
	        	mds = new MemberDataSource(this);
	        	mds.open();
	            
	            List<String> values = mds.getNamesAttendingArray(event.getEventName());
	            adapter = new ArrayAdapter<String>(this, R.layout.list_item, values);
	            lv.setAdapter(adapter);
	            mds.countAttending(event.getEventName());
	            
	            mds.close();
	            
	            
	        }
        }
        
        datasource.close();
        reminderBtn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				
				
				// Get an ArrayList of the list of emails of everyone attending the event
				mds.open();
				ArrayList<String> emails = mds.getEmailsAttending(event.getEventName());
				mds.close();
				
				// Convert the ArrayList to an array
				String[] emailarray = new String[emails.size()];
				emailarray = emails.toArray(emailarray);
				
				// Generate the subject and message 
				String subject = event.getEventName().toUpperCase() + " IS COMING UP!";
				String message = "Hello Everyone! \n\nYou are recieving this message because you have showed interest in attending " 
						+ event.getEventName().toUpperCase() + "!\n" + "This is just a friendly reminder that " + event.getEventName().toUpperCase()
						+ " is on " + event.getEventDate() + " at " + event.getEventTime() + ", located at the " + event.getEventPlace() + ". \n\n" 
						+ "We hope to see you all there! ";
				
				// Create an intent that is used to populate an email client with the list of emails, 
				// 		subject, and an automatically generated message 
				Intent email = new Intent(Intent.ACTION_SEND);
				email.putExtra(Intent.EXTRA_EMAIL, emailarray);
				email.putExtra(Intent.EXTRA_SUBJECT, subject);
				email.putExtra(Intent.EXTRA_TEXT, message);
				email.setType("message/rfc822");
				
				startActivity(Intent.createChooser(email, "Select an email client: "));
			}
        	
        });
        
    }
	
	


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.view_event_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.delete_event) {
        	
        	delete = new AlertDialog.Builder(ViewEvent.this);
        	delete.setTitle("Permanently delete event?");
        	
        	delete.setIcon(android.R.drawable.ic_delete);
        	delete.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					datasource.open();
					mds.open();
					
					// Delete event from database 
					datasource.deleteEvent(event);
					mds.dropMemberAttending(event.getEventName());
					mds.deleteFromTable(event.getId(), event.getEventName());
					
					datasource.close();
					mds.close();
					
					Intent intent = new Intent(ViewEvent.this, EventsList.class);
					startActivity(intent);
				}
			});
        	delete.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// do nothing
					
				}
			});
        	delete.create().show();

            return true;
        }
        if(id == R.id.view_back){
        	Intent intent = new Intent(ViewEvent.this, EventsList.class);
        	startActivity(intent);
        }
        if(id == R.id.update_event){
        	Intent intent = new Intent(ViewEvent.this, UpdateEvent.class);
        	intent.putExtra("com.chapman.hawaiiclub.eventid", eventID);
        	startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }


}
