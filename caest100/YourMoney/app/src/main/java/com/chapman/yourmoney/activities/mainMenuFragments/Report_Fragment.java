/*
 * Martin Caestecker
 * CPSC 370
 * Fluger
 * Version 1.0
 * 12/17/2014
 */

package com.chapman.yourmoney.activities.mainMenuFragments;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import com.chapman.yourmoney.R;
import com.chapman.yourmoney.activities.SelectTransaction;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.TextView;

public class Report_Fragment extends Fragment {

	//define class variables
	String TAG = "Report_Fragment";
	TextView tv_startDateSet;
	TextView tv_endDateSet;
	Button btn_viewReport;
	RadioButton rb_income;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		Log.v(TAG, "in onCreateView()");
		return inflater.inflate(R.layout.fragment_report, container, false);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		
		//on click, open dialog to set start date
		//fragment with DatePickers on the fragment loaded too slowly
		tv_startDateSet = (TextView) getActivity().findViewById(R.id.tv_startDate);
		tv_startDateSet.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//show the date picker
				DialogFragment picker = new DatePickerFragment();
				picker.show(getFragmentManager(), "startDatePicker");
			}
		});
		
		//on click, open dialog to set end date
		tv_endDateSet = (TextView) getActivity().findViewById(R.id.tv_endDate);
		tv_endDateSet.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//show the date picker
				DialogFragment picker = new DatePickerFragment();
				picker.show(getFragmentManager(), "endDatePicker");
			}
		});
		
		//on click, view report
		btn_viewReport = (Button) getActivity().findViewById(R.id.btn_viewReport);
		btn_viewReport.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//view the report
				String startDate = tv_startDateSet.getText().toString();
				String endDate = tv_endDateSet.getText().toString();
				
				//check to make sure dates are set
				if(startDate.equals(getResources().getString(R.string.hintClickUpdate)) || endDate.equals(getResources().getString(R.string.hintClickUpdate))){
					AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
				    builder.setTitle("Date Entry Error");
				    builder.setMessage("One or both of the dates are not set correctly.  Please tap on the date fields to update them.");
				    builder.setPositiveButton("OK", null);
				    builder.create().show();
				    return;
				}
				
				//check to make sure startDate is before or the same as endDate
				int compare = startDate.compareTo(endDate);
				if(compare == 1){
					AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
				    builder.setTitle("Date Entry Error");
				    builder.setMessage("The start date comes after the end date you entered.  Please tap on the date fields to update them.");
				    builder.setPositiveButton("OK", null);
				    builder.create().show();
				    return;
				}
				
				//get transaction type
				int transType = 0;
				rb_income = (RadioButton) getActivity().findViewById(R.id.rb_income);
				if(rb_income.isChecked())
					transType = 1;
				
				//run query, start activity
				Intent i = new Intent(Report_Fragment.this.getActivity(), SelectTransaction.class);
				i.putExtra("transType", transType);
				i.putExtra("isEdit", true);
				i.putExtra("startDate", startDate);
				i.putExtra("endDate", endDate);
				
				startActivity(i);
			}
		});
	}
	
	public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
	//found at http://stackoverflow.com/a/20923857
	
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			// Use the current date as the default date in the picker
			final Calendar c = Calendar.getInstance();
			int year = c.get(Calendar.YEAR);
			int month = c.get(Calendar.MONTH);
			int day = c.get(Calendar.DAY_OF_MONTH);
		
			// Create a new instance of DatePickerDialog and return it
			return new DatePickerDialog(getActivity(), this, year, month, day);
		}
	
		@Override
		public void onDateSet(DatePicker view, int year, int month, int day) {
			Calendar c = Calendar.getInstance();
			c.set(year, month, day);
			
			//format date for insertion into sqLite
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
			String formattedDate = sdf.format(c.getTime());
			
			//update TextViews
			if(this.getTag().equals("startDatePicker"))
				tv_startDateSet.setText(formattedDate);
			else
				tv_endDateSet.setText(formattedDate);
				
		}
	}
}
