package com.chapman.hawaiiclub;

import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class EventsList extends ListActivity{
	
	public static ListView eventsLV;
	private static boolean backClicked;
	
	static ArrayAdapter<Event> adapter;
	
	private EventDataSource datasource;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events_list);
        
        eventsLV = (ListView)findViewById(android.R.id.list);
		
        if(backClicked){
        	backClicked = false; 
        	
        	adapter.clear();
        	datasource = new EventDataSource(this);
            datasource.open();
            
            List<Event> values = datasource.getAllEvents();
            adapter = new ArrayAdapter<Event>(this, R.layout.list_item, values);
            eventsLV.setAdapter(adapter);
            
            datasource.close();
        } else {
        	backClicked = false;
        	
        	datasource = new EventDataSource(this);
            datasource.open();
            
            List<Event> values = datasource.getAllEvents();
            adapter = new ArrayAdapter<Event>(this, R.layout.list_item, values);
            eventsLV.setAdapter(adapter);
            
            datasource.close();
        }
        
		
		
		
    }
	
	public void onListItemClick(ListView lv, View view, int position, long id){
		super.onListItemClick(lv, view, position, id);
		
		Event event = (Event)(lv.getItemAtPosition(position));
		
		long eventID = event.getId();
		System.out.println(eventID);
		
		Intent intent = new Intent(EventsList.this, ViewEvent.class);
		intent.putExtra("EVENT_LIST_ID", eventID);
		startActivity(intent);
	}


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.event_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.addEvent) {
        	Intent intent = new Intent(EventsList.this, AddEvent.class);
        	startActivity(intent);
            return true;
        }
        if(id == R.id.homepage2){
        	backClicked = true;
        	Intent intent = new Intent(EventsList.this, HomePage.class);
        	startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

}
