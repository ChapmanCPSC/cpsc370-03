/*
 * Martin Caestecker
 * CPSC 370
 * Fluger
 * Version 1.0
 * 12/17/2014
 */

package com.chapman.yourmoney.activities;

import com.chapman.yourmoney.R;
import com.chapman.yourmoney.activities.mainMenuFragments.*;
import com.chapman.yourmoney.adapters.*;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;

public class MainMenu extends FragmentActivity {

	private ViewPager mViewPager;
	private TabsAdapter mTabsAdapter;
	final String TAG = "MainMenu";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.v(TAG, "in onCreate()");
		
		//set up view pager for fragments
		mViewPager = new ViewPager(this);
		mViewPager.setId(R.id.pager);
		
		setContentView(mViewPager);
		Log.v(TAG, "content view set");
		
		final ActionBar bar = getActionBar();
		bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		Log.v(TAG, "got the action bar");
		
		//create adapter, add tabs to view pager
		mTabsAdapter = new TabsAdapter(this, mViewPager);
		Log.v(TAG, "instantiated new tabs adapter");
		mTabsAdapter.addTab(bar.newTab().setText("Income"), Income_Fragment.class, null);
		mTabsAdapter.addTab(bar.newTab().setText("Expense"), Expense_Fragment.class, null);
		mTabsAdapter.addTab(bar.newTab().setText("Report"), Report_Fragment.class, null);
		Log.v(TAG, "added fragments as tabs");
	}
}