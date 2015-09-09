package com.alecrichter.textalarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

public class IncomingSmsReceiver extends BroadcastReceiver {
	
	public void onReceive(Context context, Intent intent) {

        String number = null;
        String message = null;

		Bundle bundle = intent.getExtras();
		
		try {
			
			if(bundle != null) {

				//Protocol data unit
				Object[] pdusObj = (Object[]) bundle.get("pdus");
				
				for(int i = 0; i < pdusObj.length; i++) {
					
					//Get text message data
					SmsMessage currentMsg = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
					number = currentMsg.getDisplayOriginatingAddress();
					number = number.replace(" ", "").replace("-", "").replace("+", "");
					message = currentMsg.getDisplayMessageBody();
				}
			}
		} catch(Exception e) {
			Log.e("SmsReceiver", "Exception: " + e);
		}

        //Check for match
		new TextChecker(number, message, context);
	}

}
