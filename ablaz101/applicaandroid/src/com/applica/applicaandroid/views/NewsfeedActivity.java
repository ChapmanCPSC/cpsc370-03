package com.applica.applicaandroid.views;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.applica.applicaandroid.ApplicaFont;
import com.applica.applicaandroid.R;
import com.applica.applicaandroid.controllers.NewsfeedAdapter;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.iconics.view.IconicsImageView;

public class NewsfeedActivity extends NavigationDrawerActivity {

    private IconicsImageView topBarNewsFeed;
    private ListView newsfeedListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Setup the top bar
        setupTopBar();

        String[][] data = new String[2][4]; // [Amount of Newsfeed Items][Article Title, Desc, Date, Category]
        data[0][0] = "Article Title1";
        data[0][1] = "UP's slots for Top 10 students in 2015-2016 filled up by UPCAT passers";
        data[0][2] = "02/04/2017";
        data[0][3] = "News";

        data[1][0] = "Article Title2";
        data[1][1] = "UP's slots for Top 10 students in 2015-2016 filled up by UPCAT passers";
        data[1][2] = "02/04/2017";
        data[1][3] = "News";

        newsfeedListView = (ListView) findViewById(R.id.newsfeedListView);
        newsfeedListView.setAdapter(new NewsfeedAdapter(this, data));
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_newsfeed;
    }

    protected void setupTopBar() {
        super.setupTopBar();
//        topBarNewsFeed = (IconicsImageView) findViewById(R.id.topBarNewsfeed);
//        topBarNewsFeed.setIcon(new IconicsDrawable(this, ApplicaFont.Icon.newsfeed_icon_line_blue)
//                .color(Color.parseColor("#2dafd5")).sizeDp(72));
    }

}