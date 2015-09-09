package com.chapman.hawaiiclub;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class ViewMember extends Activity{
	
	TextView viewName, viewNumber, viewEmail, viewId, viewMajor;
	long membersID;
	
	Member member;
	static boolean deletedMember;
	static boolean viewMemberBack;
	
	
	
	AlertDialog.Builder delete;
	
	private MemberDataSource datasource;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_member);
        
        datasource = new MemberDataSource(this);
        datasource.open();
        
        viewName = (TextView)findViewById(R.id.viewName);
        viewNumber = (TextView)findViewById(R.id.viewNumber);
        viewEmail = (TextView)findViewById(R.id.viewEmail);
        viewId = (TextView)findViewById(R.id.viewId);
        viewMajor = (TextView)findViewById(R.id.viewMajor);
        
        Intent intent = getIntent();
        membersID = intent.getLongExtra("MEMBER_LIST_ID", 0);
        System.out.println("membersID = " + membersID);
        //membersID = position+1;
        
        member = datasource.getMember(membersID);
        MembersPhone membersPhone = datasource.getMembersPhone(membersID);
        MembersInfo membersInfo = datasource.getMembersInfo(membersID);
        
        viewName.setText(member.getName());
        viewNumber.setText(membersPhone.getPhoneNumber());
        viewEmail.setText(membersInfo.getEmail());
        viewId.setText(membersInfo.getStudentID());
        viewMajor.setText(membersInfo.getMajor());
        
        datasource.close();
        
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.view_member_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        
        if(id == R.id.updateMember){
        	System.out.println("working?");
        	Intent intent = new Intent(ViewMember.this, UpdateMember.class);
        	intent.putExtra("UM_MembersID", membersID);
        	startActivity(intent);
        	return true;
        }
        if (id == R.id.view_member_back) {
        	System.out.println("back");
        	viewMemberBack = true;
        	Intent intent = new Intent(ViewMember.this, MembersList.class);
        	startActivity(intent);
            return true;
        }
        if(id == R.id.delete_member){
        	deletedMember = true;
        	
        	delete = new AlertDialog.Builder(ViewMember.this);
        	delete.setTitle("Permanently delete member?");
        	delete.setIcon(android.R.drawable.ic_delete);
        	delete.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					datasource.open();
					
					datasource.deleteMember(member);
					
					datasource.close();
					
					Intent intent = new Intent(ViewMember.this, MembersList.class);
					startActivity(intent);
					
				}
			});
        	delete.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					
				}
			});
        	delete.create().show();
        	return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
