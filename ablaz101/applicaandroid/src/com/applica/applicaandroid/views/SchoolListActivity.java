package com.applica.applicaandroid.views;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.ListView;

import com.applica.applicaandroid.ApplicaFont;
import com.applica.applicaandroid.R;
import com.applica.applicaandroid.controllers.SchoolListAdapter;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.iconics.view.IconicsImageView;

public class SchoolListActivity extends NavigationDrawerActivity {

    private IconicsImageView topBarSchoolList;
    private ListView schoolListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Setup the top bar
//        setupTopBar();

        String[][] data = new String[5][2];
        data[0][1] = "University of the Philippines";
        data[1][1] = "Chapman University";
        data[2][1] = "Ateneo de Manila University";
        data[3][1] = "Stanford University";
        data[4][1] = "Mapua Insititute of Technology";

        schoolListView = (ListView) findViewById(R.id.schoolListView);
        schoolListView.setAdapter(new SchoolListAdapter(this, data));
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_school_list;
    }

    protected void setupTopBar() {
        super.setupTopBar();
        topBarSchoolList = (IconicsImageView) findViewById(R.id.topBarSchoolList);
        topBarSchoolList.setIcon(new IconicsDrawable(this, ApplicaFont.Icon.school_list_icon)
                .color(Color.parseColor("#2dafd5")).sizeDp(72));
    }

}