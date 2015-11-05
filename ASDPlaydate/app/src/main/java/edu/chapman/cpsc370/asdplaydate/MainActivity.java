package edu.chapman.cpsc370.asdplaydate;

import java.util.Locale;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

import edu.chapman.cpsc370.asdplaydate.activities.ChatActivity;
import edu.chapman.cpsc370.asdplaydate.activities.TempSettingsActivity;
import edu.chapman.cpsc370.asdplaydate.fragments.FindFragment;
import edu.chapman.cpsc370.asdplaydate.fragments.InboxFragment;
import edu.chapman.cpsc370.asdplaydate.fragments.ResultListFragment;

public class MainActivity extends AppCompatActivity implements ActionBar.TabListener
{
    SectionsPagerAdapter mSectionsPagerAdapter;
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener()
        {
            @Override
            public void onPageSelected(int position)
            {
                actionBar.setSelectedNavigationItem(position);
            }
        });

        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++)
        {
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(mSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener(this));
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction)
    {
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction)
    {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction)
    {
    }

    public void settings(MenuItem item)
    {
        //ISAAC HI
        Intent myIntent = new Intent(MainActivity.this, TempSettingsActivity.class);
        MainActivity.this.startActivity(myIntent);
    }

    public void chat(MenuItem item)
    {
        Intent i = new Intent(this, ChatActivity.class);
        i.putExtra("userID", 12345); //// TODO: 11/3/2015 change the hardcoded userID to a get command from user data object
        startActivity(i);
    }

    public void account(MenuItem item)
    {
        Intent intent = new Intent(MainActivity.this,AccountActivity.class);
        MainActivity.this.startActivity(intent);
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter
    {
        public SectionsPagerAdapter(FragmentManager fm)
        {
            super(fm);
        }

        @Override
        public Fragment getItem(int position)
        {
            Fragment f;
            switch (position)
            {
                case 0:
                    f = new FindFragment();
                    break;
                case 1:
                    f = new InboxFragment();
                    break;
                default:
                    f = new ResultListFragment();
                    break;
            }
            return f;
        }

        @Override
        public int getCount()
        {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position)
        {
            Locale locale = Locale.getDefault();
            switch (position)
            {
                case 0:
                    return getString(R.string.find).toUpperCase(locale);
                case 1:
                    return getString(R.string.chat).toUpperCase(locale);
                default:
                    return "LIST";
            }
        }
    }
}
