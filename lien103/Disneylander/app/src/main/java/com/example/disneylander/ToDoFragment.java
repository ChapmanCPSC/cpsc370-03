package com.example.disneylander;



import java.util.ArrayList;
import com.example.disneylander.db.DbHelpers;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
 
 public class ToDoFragment extends Fragment {
     
    public ToDoFragment(){}
    public final static String EXTRA_MESSAGE = "com.example.ridesapp.MESSAGE";
   	
    
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
  
    	final View rootView = inflater.inflate(R.layout.fragment_todo, container, false);//Don't change this <---  
    	
    	ListView toDoRidesList = (ListView) rootView.findViewById(R.id.toDoListView);
        
        //Begin list filling code ---->
        final ArrayList<String> displayList = new ArrayList<String>(); //creates an array to populate the list
    	
        final ArrayList<RideModel> toDoRideModelList = DbHelpers.getToDoList(getActivity());//adds rides to the display list
        for (int i = 0; i < toDoRideModelList.size(); i++) 
    	{
    		RideModel rm = toDoRideModelList.get(i);
    		displayList.add(rm.m_rideName);
    	}	
        
        
		final ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(getActivity(),R.layout.simplerow, displayList);
    	toDoRidesList.setAdapter( listAdapter );
    	//End List Filling Code <---- 
    	
    	toDoRidesList.setOnItemClickListener(new OnItemClickListener() {

    		//Listens for clicks on todolist items
            @Override  
            public void onItemClick(AdapterView<?> parent, View view, int position,
                    long id) {
            	
            	toDoRideModelList.get(position).m_isToDo = false; //Sets the TOdo status to false
				DbHelpers.UpdateRide(toDoRideModelList.get(position), getActivity().getApplicationContext());//updates to TODo Status
            	
				DbHelpers.AddToRideLog(toDoRideModelList.get(position),getActivity().getApplicationContext());//adds a ride to the ride count
            	
            //get value for listItems using position    
            	String rideNameString = displayList.get(position);  
            	Toast.makeText(getActivity().getApplicationContext(), "You rode " + rideNameString + "!", 
            			   Toast.LENGTH_LONG).show();        	
            	
            	startDetailsActivity(rootView, toDoRideModelList.get(position).m_rideId);//go to main menu
            }
        }); 
    	
        //Leave code below at the end
        return rootView;
    }
    
    public void startDetailsActivity(View view, int rideId) {
        // Start classroom list activity intent
    	Intent intent = new Intent(getActivity().getApplicationContext(), MainActivity.class);  
    	
    	intent.putExtra(EXTRA_MESSAGE, Integer.toString(rideId));///sends the ride id as a string to the new activity
    	startActivityForResult(intent, 4000);
    }
}