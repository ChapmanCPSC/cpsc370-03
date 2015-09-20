package me.ablaz101.cpsc.assignment1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.cpsc.assignment1.R;

import me.ablaz101.cpsc.assignment1.models.LookupResultModel;

public class LookupResultAdapter extends BaseAdapter {

    Context context;
    LookupResultModel[] data;
    private static LayoutInflater inflater = null;

    public LookupResultAdapter(Context context, LookupResultModel[] data) {
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
            convertView = inflater.inflate(R.layout.fragment_lookup_row, null);
        }

        // Find views with ids and set their data here
        TextView companyName = (TextView) convertView.findViewById(R.id.tv_company_name);
        TextView symbol = (TextView) convertView.findViewById(R.id.tv_symbol);

        for (int i=0; i<data.length; i++) {
            LookupResultModel model = data[i];
            companyName.setText(model.name);
            symbol.setText(model.symbol);
        }

        return convertView;
    }

}