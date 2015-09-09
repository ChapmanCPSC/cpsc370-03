package com.chapman.hawaiiclub;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

public class UpdateMember extends Activity{
	
	private MemberDataSource datasource;
	
	static boolean updateMember;
	long membersId;
	EditText uName, uEmail, uNumber, uID, uMajor;
	String name, email, number, studentId, major;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_member);
        
        uName = (EditText)findViewById(R.id.uName);
        uEmail = (EditText)findViewById(R.id.uEmail);
        uNumber = (EditText)findViewById(R.id.uNumber);
        uID = (EditText)findViewById(R.id.uID);
        uMajor = (EditText)findViewById(R.id.uMajor);
        
        Intent intent = getIntent();
        membersId = intent.getLongExtra("UM_MembersID", 0);
        
        datasource = new MemberDataSource(this);
        datasource.open();
        
        Member member = null;
        member = datasource.getMember(membersId);
        
        MembersPhone membersPhone = null;
        membersPhone = datasource.getMembersPhone(membersId);
        
        MembersInfo membersInfo = null;
        membersInfo = datasource.getMembersInfo(membersId);
        
        uName.setText(member.getName());
        uNumber.setText(membersPhone.getPhoneNumber());
        uEmail.setText(membersInfo.getEmail());
        uID.setText(membersInfo.getStudentID());
        uMajor.setText(membersInfo.getMajor());
        
        datasource.close();
        
    }
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.update_member_menu, menu);
        return true;
    }
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if(id == R.id.saveMember){
        	AlertDialog.Builder save = new AlertDialog.Builder(UpdateMember.this);
        	save.setTitle("Save changes?");
        	save.setIcon(android.R.drawable.ic_menu_save);
        	save.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					updateMember = true;
					
					name = uName.getText().toString();
					number = uNumber.getText().toString();
					email = uEmail.getText().toString();
					studentId = uID.getText().toString();
					major = uMajor.getText().toString();
					
					datasource.open();
					datasource.updateMember(membersId, name, number, email, studentId, major);
					datasource.close();
					
					Intent intent = new Intent(UpdateMember.this, MembersList.class);
					startActivity(intent);
					
				}
			});
        	save.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					updateMember = true;
					
					Intent intent = new Intent(UpdateMember.this, MembersList.class);
					startActivity(intent);
				}
			});
        	save.create().show();
        }
        return super.onOptionsItemSelected(item);
	}

}
