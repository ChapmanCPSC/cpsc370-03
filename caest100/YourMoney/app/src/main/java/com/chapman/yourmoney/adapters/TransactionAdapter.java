/*
 * Martin Caestecker
 * CPSC 370
 * Fluger
 * Version 1.0
 * 12/17/2014
 */

package com.chapman.yourmoney.adapters;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.chapman.yourmoney.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

@SuppressLint("SimpleDateFormat")
public class TransactionAdapter extends BaseAdapter{

	//set class variables
	Context context;
	private LayoutInflater inflater;
	final String TAG = "TransactionAdapter";
	private TransactionDbHelper dbHelper;
	private Cursor allTransactions;
	private int type;
	
	
	public TransactionAdapter(Context context, int type){
		//get all of the transactions from the type
		//for expenses, type == 0
		//for incomes, type == 1
		this.context = context;
		this.type = type;
		
		dbHelper = new TransactionDbHelper(context);
		allTransactions = dbHelper.getAllTransactions(type);
		
		inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		Log.v(TAG, "new TransactionAdapter instantiated");
	}
	
	public TransactionAdapter(Context context, int type, String sqlStartDate, String sqlEndDate){
		//get all transactions of the defined transaction type between a date range
		//for expenses, type == 0
		//for incomes, type == 1
		this.context = context;
		this.type = type;
		
		dbHelper = new TransactionDbHelper(context);
		allTransactions = dbHelper.getTransactionsFromRange(type, sqlStartDate, sqlEndDate);
		
		inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		Log.v(TAG, "new TransactionAdapter instantiated");
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return allTransactions.getCount();
	}
	
	public class Holder
    {
    	LinearLayout ll;
    	TextView tv_date;
    	TextView tv_desc;
    	TextView tv_cat;
    	TextView tv_amt;
    	TextView tv_id;
    }

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@SuppressLint({ "ViewHolder", "InflateParams", "SimpleDateFormat" })
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		allTransactions.moveToPosition(position);
		
		//get date from transaction list
		String transDate = allTransactions.getString(allTransactions.getColumnIndex(TransactionDbHelper.TRANS_COLUMN_DATE));
		SimpleDateFormat newFormat = new SimpleDateFormat("MMMM dd, yyyy");
		SimpleDateFormat sqlDate = new SimpleDateFormat("yyyy-MM-dd");
	    Date convertedCurrentDate = null;
	    
	    //format for easier viewing
		try {
			convertedCurrentDate = sqlDate.parse(transDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    String displayDate=newFormat.format(convertedCurrentDate );

		
		String transDesc = allTransactions.getString(allTransactions.getColumnIndex(TransactionDbHelper.TRANS_COLUMN_DESC));
		
		String transCat = allTransactions.getString(allTransactions.getColumnIndex(TransactionDbHelper.TRANS_COLUMN_CAT));
		
		//format amount to look like a currency
		double transAmt = allTransactions.getDouble(allTransactions.getColumnIndex(TransactionDbHelper.TRANS_COLUMN_AMT));
		NumberFormat formatter = NumberFormat.getCurrencyInstance();
		String displayAmt = formatter.format(transAmt);
		
		//get transaction id to hide in a view for use later
		int transID = allTransactions.getInt(allTransactions.getColumnIndex(TransactionDbHelper.TRANS_COLUMN_ID));
		String displayID = Integer.toString(transID);
		
		Holder holder=new Holder();
		View rowView = inflater.inflate(R.layout.activity_select_transaction, null);
		
		//fill all of the views with information
		holder.tv_date = (TextView) rowView.findViewById(R.id.tv_date);
        holder.tv_date.setTextSize(20);
	    holder.tv_date.setPadding(5, 10, 5, 0);
	    holder.tv_date.setText("Date: " + displayDate);
	    holder.tv_date.setTextColor(Color.WHITE);
	    
	    holder.tv_desc = (TextView) rowView.findViewById(R.id.tv_desc);
        holder.tv_desc.setTextSize(20);
	    holder.tv_desc.setPadding(5, 5, 5, 0);
	    holder.tv_desc.setText(transDesc);
	    holder.tv_desc.setTextColor(Color.WHITE);
	    
	    holder.tv_cat = (TextView) rowView.findViewById(R.id.tv_cat);
        holder.tv_cat.setTextSize(20);
	    holder.tv_cat.setPadding(5, 5, 5, 0);
	    holder.tv_cat.setText("Category: " + transCat);
	    holder.tv_cat.setTextColor(Color.WHITE);
	    
	    holder.tv_amt = (TextView) rowView.findViewById(R.id.tv_amt);
        holder.tv_amt.setTextSize(20);
	    holder.tv_amt.setPadding(5, 5, 5, 10);
	    holder.tv_amt.setText("Amount: " + displayAmt);
	    holder.tv_amt.setTextColor(Color.WHITE);
	    
	    holder.tv_id = (TextView) rowView.findViewById(R.id.tv_id);
	    holder.tv_id.setText(displayID);
		
		return rowView;
	}
	
    @Override
    public void notifyDataSetChanged() {
    	super.notifyDataSetChanged();
    	allTransactions = dbHelper.getAllTransactions(type);
    }

}
