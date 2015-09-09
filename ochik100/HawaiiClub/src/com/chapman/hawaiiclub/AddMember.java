package com.chapman.hawaiiclub;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddMember extends Activity{
	
	EditText eName, eNumber, eEmail, eId, eMajor;
	Button submitMember, listMembers;
	String name, number, email, studentID, major;
	
	public static boolean membersListClicked;
	
	
	private MemberDataSource datasource;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_member);
        
        eName = (EditText)findViewById(R.id.eName);
        eNumber = (EditText)findViewById(R.id.eNumber);
        eEmail = (EditText)findViewById(R.id.eEmail);
        eId = (EditText)findViewById(R.id.eId);
        eMajor = (EditText)findViewById(R.id.eMajor);
        
        
        submitMember = (Button)findViewById(R.id.submitMember);
        
        datasource = new MemberDataSource(this);
        datasource.open();
        
        
        submitMember.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				
				membersListClicked = true;
				
				// Obtains string values from EditTexts
				name = eName.getText().toString();
				number = eNumber.getText().toString();
				email = eEmail.getText().toString();
				studentID = eId.getText().toString();
				major = eMajor.getText().toString();
				
				if(name.equals("") || number.equals("") || email.equals("") || studentID.equals("") || major.equals("")){
					
					// Display toast if a field is left blank
					Toast.makeText(AddMember.this, "A required field is blank", Toast.LENGTH_SHORT).show();
					
				} else if(!email.contains("@")){
					
					// Display toast if email address is invalid 
					Toast.makeText(AddMember.this, "Invalid email address", Toast.LENGTH_SHORT).show();
					
				} else if(number.startsWith("0")){
					
					// Display toast if phone number begins with 0 
					Toast.makeText(AddMember.this, "Invalid phone number", Toast.LENGTH_SHORT).show();
					
				} 
				else {
					
					// Add member to the database 
					Member member = null;
					member = datasource.addMember(name, number, email, studentID, major);
					
					Intent intent = new Intent(AddMember.this, MembersList.class);
					startActivity(intent);
					
				}
	
				
				
			}
        	
        });
        
        
        
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.add_member_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.addmember_back) {
        	membersListClicked = true;
			
			datasource.close();
			
			Intent intent = new Intent(AddMember.this, MembersList.class);
			startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
