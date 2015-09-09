package com.alecrichter.textalarm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import android.provider.Telephony.Mms;

public class IncomingMmsReceiver extends BroadcastReceiver {

	private ContentResolver content;
    private String mmsId = null;
    private SharedPreferences prefs;
    private Context context;

	public void onReceive(Context context, Intent intent) {

        this.context = context;
		content = context.getContentResolver();

        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String currentId = prefs.getString("pref_current_mms_id", "0");

        new MmsIdChecker().execute(new String[] {currentId});
	}
	
	private String getPart(String mmsId) {

        String fullData = "";
		
		//Uri for message parts
		Uri uri = Uri.parse("content://mms/part/");
		
		//Get part for current mms
		String selection = "mid=" + mmsId;
		Cursor cursor = content.query(uri, null, selection, null, null);
		
		//Go to first entry
		if(cursor.moveToFirst()) {
			
			do {
				//Get Id and content type
				String partId = cursor.getString(cursor.getColumnIndex("_id"));
				String type = cursor.getString(cursor.getColumnIndex(Mms.Part.CONTENT_TYPE));
				
				//Continue if content is plain text
				if("text/plain".equals(type)) {
					
					//Get data info if it exists
					String data = cursor.getString(cursor.getColumnIndex(Mms.Part._DATA));
					
					//If there is additional data, get text through getText()
					if(data != null) {
						fullData += getText(partId);
					}
					
					//If no additional data, set message to the current text
					else {
						fullData += cursor.getString(cursor.getColumnIndex(Mms.Part.TEXT));
					}
				}
			
			} while(cursor.moveToNext());
		}

        //Close cursor
        cursor.close();

        return fullData;
	}
	
	private String getText(String id) {
		
		//Get part with partId
		Uri partUri = Uri.parse("content://mms/part/" + id);
		
		InputStream is = null;
		StringBuilder sb = new StringBuilder();
		
		try {
			
			//Open input stream
			is = content.openInputStream(partUri);
			
			//If stream is not empty, get the text data
			if(is != null) {
				
				InputStreamReader isr = new InputStreamReader(is, "UTF-8");
				BufferedReader reader = new BufferedReader(isr);
				
				//Read first line
				String temp = reader.readLine();
				
				//Continue reading until end
				while(temp != null) {
					sb.append(temp);
					temp = reader.readLine();
				}
			}
			
		} catch(IOException e) {}
		
		finally {
			
			//Close input stream
			if(is != null) 
				try {
					is.close();
				} catch(IOException e) {}
		}
		
		//Return text data
		return sb.toString();
	}
	
	private String getNumber(String id) {
		
		//Select address from current message
		String selection = Mms.Addr.MSG_ID + "=" + id;
		
		//Get address for current mms
		Uri addrUri = Uri.parse("content://mms/" + id + "/addr");
		Cursor cursor = content.query(addrUri, null, selection, null, null);
		
		String number = null;
		
		//Go to first entry
		if(cursor.moveToFirst()) {

            //Get phone number
            number = cursor.getString(cursor.getColumnIndex(Mms.Addr.ADDRESS));

            //Continue if there is data
            if(number != null) {
                //Try to remove '-' from number
                number = number.replace(" ", "").replace("-", "").replace("+", "");
            }
		}
		
		//Close cursor
		cursor.close();
		
		//Return phone number
		return number;
		
	}

    public class MmsIdChecker extends AsyncTask<String, Void, String> {

        protected String doInBackground(String... params) {

            int count = 0;

            try {

                //Loop until newest mms id changes or count reaches 50
                do {
                    //Query the MMS database
                    Cursor mmsCursor = content.query(Mms.Inbox.CONTENT_URI, new String[] {"_id"}, null, null, null);

                    //Go to newest message
                    if(mmsCursor.moveToFirst())
                        mmsId = mmsCursor.getString(0);

                    //Close cursor
                    mmsCursor.close();

                    //Wait 0.5 seconds until next check
                    Thread.sleep(500);

                    //Increment count
                    count++;

                } while(mmsId.equals(params[0]) && count < 50);

            } catch (Exception e) {
                e.printStackTrace();
            }

            if(count < 50)
                return "true";
            else
                return "false";
        }

        protected void onPostExecute(String result) {

            if(result.equals("true")) {

                //Save new mms id
                SharedPreferences.Editor edit = prefs.edit();
                edit.putString("pref_current_mms_id", mmsId);
                edit.apply();

                //Get message
                String message = getPart(mmsId);

                //Get sender address
                String number = getNumber(mmsId);

                if(!number.equals("insertaddresstoken")) {
                    //Check for match
                    new TextChecker(number, message, context);
                }
            }
        }
    }

}
