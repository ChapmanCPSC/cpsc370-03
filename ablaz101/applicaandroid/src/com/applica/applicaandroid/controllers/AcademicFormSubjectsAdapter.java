package com.applica.applicaandroid.controllers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.applica.applicaandroid.R;

public class AcademicFormSubjectsAdapter extends BaseAdapter {

    Context context;
    String[][] data;
    private static LayoutInflater inflater = null;

    public AcademicFormSubjectsAdapter(Context context, String[][] data) {
        // TODO Auto-generated constructor stub
        this.context = context;
        this.data = data;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public String[][] getData() {
        return data;
    }

    public void setData(String[][] data) {
        this.data = data;
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
            // Set the convertView
            convertView = inflater.inflate(R.layout.activity_academics_form_subject, null);
        }

        // Find views with ids and set their data here
        return convertView;
    }

}
