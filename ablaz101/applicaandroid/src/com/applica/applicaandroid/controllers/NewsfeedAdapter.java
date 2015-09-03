package com.applica.applicaandroid.controllers;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.applica.applicaandroid.R;

public class NewsfeedAdapter extends BaseAdapter {

    Context context;
    String[][] data;
    private static LayoutInflater inflater = null;

    public NewsfeedAdapter(Context context, String[][] data) {
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
            convertView = inflater.inflate(R.layout.activity_newsfeed_single_column, null);
        }

        // Find views with ids and set their data here
        // TODO proper implementation
        TextView articleTitle = (TextView) convertView.findViewById(R.id.newsfeedRowArticleTitle);
        TextView articleDesc = (TextView) convertView.findViewById(R.id.newsfeedDescription);
        TextView articleDate = (TextView) convertView.findViewById(R.id.newsfeedRowDate);
        TextView articleCategory = (TextView) convertView.findViewById(R.id.newsfeedRowCategory);

        Typeface tf = Typeface.createFromAsset(context.getAssets(), "fonts/helvetica_neue.ttf");
        articleTitle.setTypeface(tf);
        articleDesc.setTypeface(tf);
        articleDate.setTypeface(tf);
        articleCategory.setTypeface(tf);

        articleTitle.setText(data[position][0]);
        articleDesc.setText(data[position][1]);
        articleDate.setText(data[position][2]);
        articleCategory.setText(data[position][3]);

        return convertView;
    }

}
