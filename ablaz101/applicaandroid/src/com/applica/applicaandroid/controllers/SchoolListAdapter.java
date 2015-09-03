package com.applica.applicaandroid.controllers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.applica.applicaandroid.R;

public class SchoolListAdapter extends BaseAdapter {

    Context context;
    String[][] data;
    private static LayoutInflater inflater = null;

    public SchoolListAdapter(Context context, String[][] data) {
        // TODO Auto-generated constructor stub
        this.context = context;
        this.data = data;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return data.length;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return data[position];
    }

    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        if (convertView == null) {
            // set the convertview
            convertView = inflater.inflate(R.layout.activity_school_list_row, null);
        }

        ImageView background = (ImageView) convertView.findViewById(R.id.schoolListImage);
        TextView universityName = (TextView) convertView.findViewById(R.id.universityName);

        universityName.setText(data[position][1]);

        // Find views with ids and set their data here
        return convertView;
    }

}
