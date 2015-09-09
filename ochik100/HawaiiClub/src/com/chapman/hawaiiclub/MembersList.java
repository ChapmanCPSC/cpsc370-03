package com.chapman.hawaiiclub;

import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MembersList extends ListActivity{
	
	static ArrayAdapter<Member> adapter;
	
	private static boolean backClicked;
	
	
	private ListView lv;

	private MemberDataSource datasource;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_members_list);
        
        
	    lv = (ListView)findViewById(android.R.id.list);
	    
	    
	    if(AddMember.membersListClicked){
	    	
	    	// Clears the adapter so no duplicates of members are added to the adapter
	    	
	    	AddMember.membersListClicked = false;
	    	backClicked = false;
	    	ViewMember.deletedMember = false;
	    	ViewMember.viewMemberBack = false;
	    	UpdateMember.updateMember = false;
	    	
	    	adapter.clear();
		    datasource = new MemberDataSource(this);
		    datasource.open();
		    
		    List<Member> values = datasource.getAllMembers();
		    adapter = new ArrayAdapter<Member>(this,
		    		R.layout.list_item, values);
		    lv.setAdapter(adapter);
	
			datasource.close();
	    } else if (UpdateMember.updateMember){
	    	
	    	// Clears the adapter so no duplicates of members are added to the adapter
	    	ViewMember.viewMemberBack = false;
	    	backClicked = false;
	    	ViewMember.deletedMember = false;
	    	AddMember.membersListClicked = false;
	    	UpdateMember.updateMember = false;
	    	
	    	adapter.clear();
		    datasource = new MemberDataSource(this);
		    datasource.open();
		    
		    List<Member> values = datasource.getAllMembers();
		    adapter = new ArrayAdapter<Member>(this,
		    		R.layout.list_item, values);
		    lv.setAdapter(adapter);
	
			datasource.close();
	    }else if (ViewMember.viewMemberBack){
	    	
	    	// Clears the adapter so no duplicates of members are added to the adapter
	    	ViewMember.viewMemberBack = false;
	    	backClicked = false;
	    	ViewMember.deletedMember = false;
	    	AddMember.membersListClicked = false;
	    	UpdateMember.updateMember = false;
	    	
	    	adapter.clear();
		    datasource = new MemberDataSource(this);
		    datasource.open();
		    
		    List<Member> values = datasource.getAllMembers();
		    adapter = new ArrayAdapter<Member>(this,
		    		R.layout.list_item, values);
		    lv.setAdapter(adapter);
	
			datasource.close();
	    } else if (backClicked){
	    	
	    	// Clears the adapter so no duplicates of members are added to the adapter
	    	
	    	backClicked = false;
	    	ViewMember.deletedMember = false;
	    	AddMember.membersListClicked = false;
	    	ViewMember.viewMemberBack = false;
	    	UpdateMember.updateMember = false;
	    	
	    	adapter.clear();
		    datasource = new MemberDataSource(this);
		    datasource.open();
		    
		    List<Member> values = datasource.getAllMembers();
		    adapter = new ArrayAdapter<Member>(this,
		    		R.layout.list_item, values);
		    lv.setAdapter(adapter);
	
			datasource.close();
	    } else if (ViewMember.deletedMember){
	    	
	    	// Clears the adapter so no duplicates of members are added to the adapter
	    	
	    	ViewMember.deletedMember = false;
	    	AddMember.membersListClicked = false;
	    	backClicked = false;
	    	ViewMember.viewMemberBack = false;
	    	UpdateMember.updateMember = false;
	    	
	    	adapter.clear();
		    datasource = new MemberDataSource(this);
		    datasource.open();
		    
		    List<Member> values = datasource.getAllMembers();
		    adapter = new ArrayAdapter<Member>(this,
		    		R.layout.list_item, values);
		    lv.setAdapter(adapter);
	
			datasource.close();
	    }
	    else {
	    	
	    	// Sets the adapter to the list of members
	    	
	    	AddMember.membersListClicked = false;
	    	backClicked = false;
	    	ViewMember.deletedMember = false;
	    	UpdateMember.updateMember = false;
	    	ViewMember.viewMemberBack = false;
	    	
		    datasource = new MemberDataSource(this);
		    datasource.open();
		    
		    List<Member> values = datasource.getAllMembers();
		    adapter = new ArrayAdapter<Member>(this,
			          R.layout.list_item, values);
		    lv.setAdapter(adapter);
	
			datasource.close();
	    	
	    }
		
		
	    
    }

	
	public void onListItemClick(ListView lv, View view, int position, long id){
		super.onListItemClick(lv, view, position, id);
		
		Member member = (Member)(lv.getItemAtPosition(position));
		long memberID = member.getId();
		
		// When a member is clicked on, the MemberId of the member is sent through an intent to ViewMember
		Intent intent = new Intent(MembersList.this, ViewMember.class);
		intent.putExtra("MEMBER_LIST_ID", memberID);
		startActivity(intent);
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.member_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.addMember) {
        	// Opens the AddMember activity
        	Intent intent = new Intent(MembersList.this, AddMember.class);
        	startActivity(intent);
            return true;
        }
        if(id == R.id.homepage){
        	// Goes to the HomePage Activity
        	backClicked = true;
        	Intent intent = new Intent(MembersList.this, HomePage.class);
        	startActivity(intent);
        	return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
