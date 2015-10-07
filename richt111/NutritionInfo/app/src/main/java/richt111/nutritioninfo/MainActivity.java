package richt111.nutritioninfo;

import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;

import richt111.nutritioninfo.fragments.SearchFragment;
import richt111.nutritioninfo.fragments.TotalsFragment;
import richt111.nutritioninfo.util.ViewPagerAdapter;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

        // Set tab layout
        ViewPager viewPager = (ViewPager) findViewById(R.id.main_viewpager);
        setupViewPager(viewPager);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.main_tabs);
        tabLayout.setupWithViewPager(viewPager);

    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter =  new ViewPagerAdapter(getSupportFragmentManager());

        adapter.addFrag(new SearchFragment(), "Search");
        adapter.addFrag(new TotalsFragment(), "Daily Totals");

        viewPager.setAdapter(adapter);
    }

}
