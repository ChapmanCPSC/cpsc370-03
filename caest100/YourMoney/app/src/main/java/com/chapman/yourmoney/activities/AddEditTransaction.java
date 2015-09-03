/*
 * Martin Caestecker
 * CPSC 370
 * Fluger
 * Version 1.0
 * 12/17/2014
 */

package com.chapman.yourmoney.activities;

import com.chapman.yourmoney.R;
import com.chapman.yourmoney.adapters.TransactionDbHelper;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class AddEditTransaction extends Activity{

	//define class variables
	private int transType = 0;
	private boolean isEdit = false;
	private int transactionID = 0;
	TextView title;
	EditText et_desc;
	EditText et_amt;
	DatePicker dp_transDate;
	TransactionDbHelper dbHelper;
	ArrayAdapter<String> categories;
	Spinner spn_cat;
	Button btn_cancel;
	Button btn_submit;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_add_edit_transaction);
		
		//reference views
		et_desc = (EditText) findViewById(R.id.et_Desc);
		et_amt = (EditText) findViewById(R.id.et_Amt);
		dp_transDate = (DatePicker) findViewById(R.id.dp_transDate);
		
		dbHelper = new TransactionDbHelper(getApplicationContext());
		
		//get extras from intent
		Intent i = getIntent();
		transType = i.getExtras().getInt("transType");
		isEdit = i.getExtras().getBoolean("isEdit");
		
		title = (TextView) findViewById(R.id.tv_ActivityTitle);
		
		//decide how to use data
		//transType == 0 is expense, transType == 1 is income
		if(isEdit == true && transType == 0)
			title.setText("Edit Expense");
		else if(isEdit == true && transType == 1)
			title.setText("Edit Income");
		else if(isEdit == false && transType == 0)
			title.setText("Add Expense");
		else if(isEdit == false && transType == 1)
			title.setText("Add Income");
		else{
			//in future version, turn into dialog with ok button
			Toast.makeText(getApplicationContext(), "An Error Occurred, please try again", Toast.LENGTH_LONG).show();
			finish();
		}		
		
		String[] categoryArray;
		//set spinner's adapter
		if(transType == 0)
			categoryArray = dbHelper.getAllCategories(0);
		else
			categoryArray = dbHelper.getAllCategories(1);
		
		categories = new ArrayAdapter<String>(this, R.layout.spinner_style, categoryArray);
		
		spn_cat = (Spinner) findViewById(R.id.spn_cat);
		spn_cat.setAdapter(categories);
		
		if(isEdit == true){
			//grab the current record
			transactionID = i.getExtras().getInt("transactionID");
			Cursor res = dbHelper.getTransaction(transactionID);
			res.moveToFirst();
			
			String desc = res.getString(res.getColumnIndex(TransactionDbHelper.TRANS_COLUMN_DESC));
			et_desc.setText(desc);
			
			//set date on DatePicker
			String date = res.getString(res.getColumnIndex(TransactionDbHelper.TRANS_COLUMN_DATE));
			try{
				int year = Integer.parseInt(date.substring(0, 4));
				int month = Integer.parseInt(date.substring(5, 7)) - 1;
				int dayOfMonth = Integer.parseInt(date.substring(8));
				dp_transDate.updateDate(year, month, dayOfMonth);
			} catch(NumberFormatException e){
				e.printStackTrace();
			}
			
			//if the category exists, set the spinner
			String cat = res.getString(res.getColumnIndex(TransactionDbHelper.TRANS_COLUMN_CAT));
			for(int j = 0; j < spn_cat.getCount(); j++){
				if(spn_cat.getItemAtPosition(j).toString().equals(cat)){
					spn_cat.setSelection(j);
					break;
				}
			}
			
			double amt = res.getDouble(res.getColumnIndex(TransactionDbHelper.TRANS_COLUMN_AMT));
			String amtDisplay = Double.toString(amt);
			et_amt.setText(amtDisplay);
			
		}
		
		btn_cancel = (Button) findViewById(R.id.btn_cancel);
		btn_submit = (Button) findViewById(R.id.btn_submit);
		
		btn_cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
		btn_submit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String desc = et_desc.getText().toString();
				String amt = et_amt.getText().toString();
				String cat = spn_cat.getSelectedItem().toString();
				double dblAmt = 0.0;
				
				//parse amount from EditText
				try{
					dblAmt = Double.parseDouble(amt);
				} catch (NumberFormatException e){
					
					AlertDialog.Builder builder = new AlertDialog.Builder(AddEditTransaction.this);
				    builder.setTitle("Amount Error!");
				    builder.setMessage("You entered the amount for this transaction incorrectly.  Please try again.");
				    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							return;
						}
					});
				    builder.create().show();
				}
				
				//get date from DatePicker
				String year = Integer.toString(dp_transDate.getYear());
				String month = Integer.toString(dp_transDate.getMonth() + 1);
				String day = Integer.toString(dp_transDate.getDayOfMonth());
				
				//format date for sqLite
				if(month.length() == 1)
					month = "0" + month;
				
				if(day.length() == 1)
					day = "0" + day;
				
				String sqlDate = year + "-" + month + "-" + day;
				
				if(isEdit == true){
					//edit the transaction
					if(dbHelper.updateTransaction(transactionID, sqlDate, desc, dblAmt, cat, transType) == true){
						Toast.makeText(getApplicationContext(), "Success!", Toast.LENGTH_SHORT).show();
						finish();
					}
					else{
						AlertDialog.Builder builder = new AlertDialog.Builder(AddEditTransaction.this);
					    builder.setTitle("Insertion Error!");
					    builder.setMessage("There was an error saving this transaction.  Please try again.");
					    builder.setPositiveButton("OK", null);
					    builder.create().show();
					}
				} else{
					//insert a new transaction
					if(dbHelper.insertTransaction(sqlDate, desc, dblAmt, cat, transType) == true){
							Toast.makeText(getApplicationContext(), "Success!", Toast.LENGTH_SHORT).show();
							finish();
					}
					else{
						AlertDialog.Builder builder = new AlertDialog.Builder(AddEditTransaction.this);
					    builder.setTitle("Insertion Error!");
					    builder.setMessage("There was an error saving this transaction.  Please try again.");
					    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								return;
							}
						});
					    builder.create().show();
					}
				}
			}
		});
	}
	
	@Override
	public void onBackPressed() {
		finish();
	}
}
