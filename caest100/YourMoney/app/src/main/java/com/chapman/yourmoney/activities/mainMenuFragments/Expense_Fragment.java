/*
 * Martin Caestecker
 * CPSC 370
 * Fluger
 * Version 1.0
 * 12/17/2014
 */

package com.chapman.yourmoney.activities.mainMenuFragments;

import java.text.NumberFormat;
import java.util.Calendar;

import com.chapman.yourmoney.R;
import com.chapman.yourmoney.activities.AddEditTransaction;
import com.chapman.yourmoney.activities.SelectTransaction;
import com.chapman.yourmoney.adapters.TransactionDbHelper;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class Expense_Fragment extends Fragment {

	//define class variables
	Button btnTransAdd;
	Button btnTransEdit;
	Button btnTransDelete;
	
	Button btnCatAdd;
	Button btnCatDelete;
	
	TextView tv_ytdTotalDisp;
	
	ProgressBar pb_ytdLoad;
	String TAG = "Income_Fragment";
	Double expenseAmt = 0.0;
	
	DatePicker dp_transDate;
	NumberFormat formatter;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		Log.v(TAG, "in onCreateView()");
		return inflater.inflate(R.layout.fragment_income, container, false);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		
		//set title text
		TextView title = (TextView) getView().findViewById(R.id.tv_lblYTD);
		title.setText(getResources().getText(R.string.lblYtdExpenses));
		
		Log.v(TAG, "In onResume()");
		
		//reference all views
		btnTransAdd = (Button) getView().findViewById(R.id.btnTransAdd);
		btnTransEdit = (Button) getView().findViewById(R.id.btnTransEdit);
		btnTransDelete = (Button) getView().findViewById(R.id.btnTransDelete);
		
		btnCatAdd = (Button) getView().findViewById(R.id.btnCatAdd);
		btnCatDelete = (Button) getView().findViewById(R.id.btnCatDelete);
		
		tv_ytdTotalDisp = (TextView) getView().findViewById(R.id.tv_ytdTotalDisp);
		pb_ytdLoad = (ProgressBar) getView().findViewById(R.id.pb_ytdLoad);
		pb_ytdLoad.setVisibility(View.INVISIBLE);
		
		dp_transDate = (DatePicker) getView().findViewById(R.id.dp_transDate);
		
		Log.v(TAG, "Views Referenced");
		
		//run AsyncTask
		new GetExpense().execute();
		
		btnTransAdd.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//add new expense transaction
				Intent i = new Intent(Expense_Fragment.this.getActivity(), AddEditTransaction.class);
				i.putExtra("transType", 0);
				i.putExtra("isEdit", false);
				startActivity(i);
			}
		});
		
		btnTransEdit.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//edit an existing expense
				Intent i = new Intent(Expense_Fragment.this.getActivity(), SelectTransaction.class);
				i.putExtra("transType", 0);
				i.putExtra("isEdit", true);
				startActivity(i);
			}
		});
		
		btnTransDelete.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//delete an existing expense transaction
				Intent i = new Intent(Expense_Fragment.this.getActivity(), SelectTransaction.class);
				i.putExtra("transType", 0);
				i.putExtra("isEdit", false);
				startActivity(i);
			}
		});
		
		btnCatAdd.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//add a new expense category
				DialogFragment newFragment = new AddCategoryDialogFragment();
			    newFragment.show(getFragmentManager(), "add category");
			}
		});
		
		btnCatDelete.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//delete an expense category
				DialogFragment newFragment = new DeleteCategoryDialogFragment();
			    newFragment.show(getFragmentManager(), "delete category");
			}
		});
		
		Log.v(TAG, "Button clicks instantiated");
	}
	
	private class GetExpense extends AsyncTask<String, Void, String> {
		NumberFormat formatter;
		double setExpense = 0.0;
		
		@Override
		protected void onPreExecute() {
			//prep views while loading
			super.onPreExecute();
			pb_ytdLoad.setVisibility(View.VISIBLE);
			tv_ytdTotalDisp.setText("");
		}
		
		@Override
		protected String doInBackground(String... arg0) {
			Log.v(TAG, "views preped for update");
			
			TransactionDbHelper transHelper = new TransactionDbHelper(getActivity());
			
			Calendar cal = Calendar.getInstance();
	    	
			//get the total expenses from January 1 of the current year to present
	    	setExpense = transHelper.getTotal(0, cal.get(Calendar.YEAR));
	    	formatter = NumberFormat.getCurrencyInstance();
	    	Log.v(TAG, "incomeAmt updated to: " + formatter.format(setExpense));
			return null;
		}
		
		@Override
		protected void onPostExecute(String result) {
			//update the display
			super.onPostExecute(result);
			
			Log.v(TAG, "in onPostExecute");
			pb_ytdLoad.setVisibility(View.INVISIBLE);
			tv_ytdTotalDisp.setText(formatter.format(setExpense));
		}
	}
	
	public class AddCategoryDialogFragment extends DialogFragment {
		@Override
		@SuppressLint("InflateParams")
		public Dialog onCreateDialog(Bundle savedInstanceState) {
		    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		    // Get the layout inflater
		    LayoutInflater inflater = getActivity().getLayoutInflater();

		    // Inflate and set the layout for the dialog
		    // Pass null as the parent view because its going in the dialog layout
		    
		    builder.setView(inflater.inflate(R.layout.dialog_add, null))
		    // Add action buttons
		           .setPositiveButton(R.string.btnAdd, new DialogInterface.OnClickListener() {
					@Override
		               public void onClick(DialogInterface dialog, int id) {
		                   // add a category
		            	   
		            	   EditText name = (EditText) getDialog().findViewById(R.id.et_name);
		            	   
		            	   TransactionDbHelper helper = new TransactionDbHelper(Expense_Fragment.this.getActivity());
		            	   helper.insertCategory(name.getText().toString(), 0);
		            	   
		            	   Toast.makeText(Expense_Fragment.this.getActivity(), "Category Added!", Toast.LENGTH_SHORT).show();
		               }
		           })
		           .setNegativeButton(R.string.btnCancel, new DialogInterface.OnClickListener() {
		               public void onClick(DialogInterface dialog, int id) {
		                   AddCategoryDialogFragment.this.getDialog().cancel();
		               }
		           });      
		    return builder.create();
		}
	}
	
	public class DeleteCategoryDialogFragment extends DialogFragment{
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			//get existing categories
			final TransactionDbHelper helper = new TransactionDbHelper(Expense_Fragment.this.getActivity());
			final String[] allCats = helper.getAllCategories(0);
			
		    final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		    builder.setTitle(R.string.lbl_deleteCat)
		           .setItems(allCats, new DialogInterface.OnClickListener() {
		               public void onClick(DialogInterface dialog, int which) {
		            	   //on click, remove the category
		            	   //will add dialog box to confirm deletion in future version
		            	   
		            	   helper.deleteCategory(allCats[which]);
		            	   Toast.makeText(Expense_Fragment.this.getActivity(), "Category Deleted.  Transactions are not affected", Toast.LENGTH_LONG).show();
		           }
		            
		    });
		    return builder.create();
		}
	}
}
