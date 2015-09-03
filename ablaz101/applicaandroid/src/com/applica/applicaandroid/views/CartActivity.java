package com.applica.applicaandroid.views;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.ListView;

import com.applica.applicaandroid.ApplicaFont;
import com.applica.applicaandroid.R;
import com.applica.applicaandroid.controllers.CartAdapter;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.iconics.view.IconicsImageView;

public class CartActivity extends NavigationDrawerActivity {

    private IconicsImageView topBarSchoolList;
    private ListView cartListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Setup the top bar
//        setupTopBar();

        String[][] data = new String[5][3];
        cartListView = (ListView) findViewById(R.id.cartListView);
        cartListView.setAdapter(new CartAdapter(this, data));
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_cart;
    }

    protected void setupTopBar() {
        super.setupTopBar();
        topBarSchoolList = (IconicsImageView) findViewById(R.id.topBarSchoolList);
        topBarSchoolList.setIcon(new IconicsDrawable(this, ApplicaFont.Icon.cart_icon)
                .color(Color.parseColor("#2dafd5")).sizeDp(72));
    }

}