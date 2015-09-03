package com.applica.applicaandroid.controllers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.applica.applicaandroid.R;
import com.applica.applicaandroid.models.LoginResult;
import com.applica.applicaandroid.models.SignUpResult;

public class CartAdapter extends BaseAdapter {

    Context context;
    String[][] data;
    private static LayoutInflater inflater = null;

    public CartAdapter(Context context, String[][] data) {
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
            // Set the convertView
            convertView = inflater.inflate(R.layout.activity_cart_row, null);
        }

        ImageView schoolImage = (ImageView) convertView.findViewById(R.id.cartRowSchoolImage);
        TextView schoolName = (TextView) convertView.findViewById(R.id.cartRowSchoolName);
        TextView appPrice = (TextView) convertView.findViewById(R.id.cartRowAppPrice);


        // Find views with ids and set their data here
        return convertView;
    }

}
