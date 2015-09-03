package com.applica.applicaandroid.views;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.ListView;

import com.applica.applicaandroid.ApplicaFont;
import com.applica.applicaandroid.R;
import com.applica.applicaandroid.controllers.CartAdapter;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.iconics.view.IconicsImageView;

public class AboutTheSchoolActivity extends NavigationDrawerActivity {

    private IconicsImageView topBarSchoolList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Setup the top bar
//        setupTopBar();
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_about_the_school;
    }

    protected void setupTopBar() {
        super.setupTopBar();
        topBarSchoolList = (IconicsImageView) findViewById(R.id.topBarSchoolList);
        topBarSchoolList.setIcon(new IconicsDrawable(this, ApplicaFont.Icon.school_list_icon)
                .color(Color.parseColor("#2dafd5")).sizeDp(72));
    }

}