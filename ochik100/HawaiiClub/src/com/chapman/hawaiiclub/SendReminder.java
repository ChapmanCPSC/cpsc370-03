package com.chapman.hawaiiclub;

import android.app.Activity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class SendReminder extends Activity{
	
	Button sendBtn;
	EditText messageBody;
	
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_reminder);
        
        sendBtn = (Button)findViewById(R.id.sendBtn);
        messageBody = (EditText)findViewById(R.id.messageBody);
        
        sendBtn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				
				System.out.println("clicked");
				/*try {   
                    //GMailSender sender = new GMailSender("aznchica8808@gmail.com", "Gymnast7");
                    //sender.sendMail("This is Subject",   
                      //      "This is Body",   
                       /     "aznchica8808@gmail.com",   
                            "ochik100@mail.chapman.edu");   
                } catch (Exception e) {   
                	System.out.println("error occured");
                    Log.e("SendMail", e.getMessage(), e);   
                }*/
				
			}
        	
        });
        
	}
	
	private void sendSMS(String phoneNumber, String message)
	{
		SmsManager sms = SmsManager.getDefault();
		sms.sendTextMessage(phoneNumber, null, message, null, null);
	} 

}
