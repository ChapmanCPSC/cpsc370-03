/*
 * Martin Caestecker
 * CPSC 370
 * Fluger
 * Version 1.0
 * 12/17/2014
 */

package com.chapman.yourmoney.activities;

import com.chapman.yourmoney.R;
import com.chapman.yourmoney.adapters.TransactionAdapter;
import com.chapman.yourmoney.adapters.TransactionDbHelper;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class SelectTransaction extends ListActivity{

	//define class variables
	final String TAG = "SelectTransaction";
	private TransactionAdapter adapter;
	private ListView lv_display;
	
	private int transactionID = 0;
	private boolean isEdit = true;
	private int transType = 0;
	private String startDate = "";
	private String endDate = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.v(TAG, "in onCreate");
		
		lv_display = getListView();
		
		//get intent information
		transType = getIntent().getIntExtra("transType", 1);
		isEdit = getIntent().getBooleanExtra("isEdit", true);
		startDate = getIntent().getStringExtra("startDate");
		endDate = getIntent().getStringExtra("endDate");
		
		//if start and end date are present, set adapter to display date range
		if(startDate != null && endDate != null)
			adapter = new TransactionAdapter(this, transType, startDate, endDate);
		else
			//display all transactions
			adapter = new TransactionAdapter(this, transType);
		
		if(adapter.getCount() == 0){
			//no results were found
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
		    builder.setTitle("No Results Found");
		    builder.setMessage("Your search returned no results.  Please try again.");
		    builder.setPositiveButton("OK", new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					finish();
				}
			});
		    builder.create().show();
		}
		
		lv_display.setAdapter(adapter);
		Log.v(TAG, "adapter set");
		
		lv_display.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				//get hidden id TextView from LinearLayout
				LinearLayout row = (LinearLayout) lv_display.getChildAt(position);
				TextView tv_id = (TextView) row.findViewById(R.id.tv_id);
				
				try{
					transactionID = Integer.parseInt(tv_id.getText().toString());
				} catch (NumberFormatException e){
					e.printStackTrace();
				}
				
				if(isEdit == true){
					//edit the transaction on the AddEditTransaction activity
					Intent i = new Intent(SelectTransaction.this, AddEditTransaction.class);
					i.putExtra("transactionID", transactionID);
					i.putExtra("isEdit", true);
					i.putExtra("transType", transType);
					startActivity(i);
					finish();
				} else{
					//delete the selected transaction
					AlertDialog.Builder builder = new AlertDialog.Builder(SelectTransaction.this);
				    builder.setTitle("Delete Transaction");
				    builder.setMessage("Are you sure you want to delete the selected transaction?");
				    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							TransactionDbHelper dbHelper = new TransactionDbHelper(SelectTransaction.this);
							dbHelper.deleteTransaction(transactionID);
							finish();
							Toast.makeText(SelectTransaction.this, "Transaction Deleted", Toast.LENGTH_SHORT).show();
							adapter.notifyDataSetChanged();
						}
					});
				    builder.setNegativeButton("No", null);
				    builder.create().show();
				}
				
				
			}
		});
	}
	
	
}
