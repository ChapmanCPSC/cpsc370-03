package com.applica.applicaandroid.controllers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;

import com.applica.applicaandroid.R;

public class SubjectListAdapter extends BaseAdapter {

    Context context;
    String[][] data;
    private static LayoutInflater inflater = null;

    public SubjectListAdapter(Context context, String[][] data) {
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

        View vi = convertView;
        if (vi == null) {
            // Set the convertView
            vi = inflater.inflate(R.layout.activity_academics_form_subject, null);
        }

        // Find views with ids and set their data here
        EditText editText = (EditText) vi.findViewById(R.id.subjectInput);
        editText.setText(data[position][0]);

        editText = (EditText) vi.findViewById(R.id.classInput);
        editText.setText(data[position][1]);

        editText = (EditText) vi.findViewById(R.id.gradeInput);
        editText.setText(data[position][2]);

        return vi;
    }

}
