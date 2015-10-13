package com.example.cpsc.demoapplication.activities;

import android.app.Activity;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.widget.DrawerLayout;

import com.example.cpsc.demoapplication.R;
import com.example.cpsc.demoapplication.fragments.HomeFragment;
import com.example.cpsc.demoapplication.fragments.NavigationDrawerFragment;
import com.example.cpsc.demoapplication.fragments.RecentFragment;
import com.example.cpsc.demoapplication.fragments.UITestFragment;
import com.example.cpsc.demoapplication.fragments.WeatherFragment;
import com.example.cpsc.demoapplication.services.RainCheckService;

public class MainActivity extends Activity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks
{

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        RainCheckService.StartChecker(this);
    }

    @Override
    public void onNavigationDrawerItemSelected(int position)
    {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getFragmentManager();

        Fragment f;

        switch (position)
        {
            case 0:
                f = new HomeFragment();
                break;
            case 1:
                f = new WeatherFragment();
                break;
            case 2:
                f = new RecentFragment();
                break;
            case 3:
                f = new UITestFragment();
                break;
            default:
                f = new HomeFragment();
                break;

        }

        fragmentManager.beginTransaction().replace(R.id.container, f).commit();
    }

    public void onSectionAttached(int number)
    {
        switch (number)
        {
            case 0:
                mTitle = getString(R.string.home);
                break;
            case 1:
                mTitle = getString(R.string.weather);
                break;
            case 2:
                mTitle = getString(R.string.recent);
                break;
            case 3:
                mTitle = getString(R.string.ui_test);
                break;
        }
    }

    public void restoreActionBar()
    {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        if (!mNavigationDrawerFragment.isDrawerOpen())
        {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    public void settings(MenuItem item)
    {
        startActivity(new Intent(this, SettingsActivity.class));
    }

}
