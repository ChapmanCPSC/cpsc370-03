package com.applica.applicaandroid.controllers;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.applica.applicaandroid.R;

public class SideBarAdapter extends BaseAdapter {

    Context context;
    String[][] data;
    private static LayoutInflater inflater = null;

    public SideBarAdapter(Context context, String[][] data) {
        this.context = context;
        this.data = data;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return data.length;
    }

    @Override
    public Object getItem(int position) {
        return data[position];
    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            // Set the convertView
            convertView = inflater.inflate(R.layout.widget_side_menu_row, null);
        }

        // Find views with ids and set their data here
        // TODO proper implementation
        TextView universityName = (TextView) convertView.findViewById(R.id.slideMenuSchoolName);
        TextView appStatus1 = (TextView) convertView.findViewById(R.id.slideMenuStatusDenied);

        Typeface tf = Typeface.createFromAsset(context.getAssets(), "fonts/helvetica_neue.ttf");
        universityName.setTypeface(tf);
        appStatus1.setTypeface(tf);

        universityName.setText(data[position][0]);

        return convertView;
    }

}
