package com.applica.applicaandroid.views;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.ListView;

import com.applica.applicaandroid.ApplicaFont;
import com.applica.applicaandroid.R;
import com.applica.applicaandroid.controllers.NewsfeedAdapter;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.iconics.view.IconicsImageView;

public class ProfileActivity extends NavigationDrawerActivity {

    private IconicsImageView topBarNewsFeed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Setup the top bar
//        setupTopBar();
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_profile;
    }

    protected void setupTopBar() {
        super.setupTopBar();
        topBarNewsFeed = (IconicsImageView) findViewById(R.id.topBarProfile);
        topBarNewsFeed.setIcon(new IconicsDrawable(this, ApplicaFont.Icon.profile_icon)
                .color(Color.parseColor("#2dafd5")).sizeDp(72));
    }

}