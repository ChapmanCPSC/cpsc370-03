package me.ablaz101.cpsc370.stockinfogetter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import me.ablaz101.cpsc370.stockinfogetter.models.LookupResultModel;

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
            convertView = inflater.inflate(R.layout.view_lookup_row, null);
        }

        // Find views with ids and set their data here
        TextView companyName = (TextView) convertView.findViewById(R.id.tv_company_name);
        TextView symbol = (TextView) convertView.findViewById(R.id.tv_symbol);

        LookupResultModel model = data[position];
        companyName.setText(model.name);
        symbol.setText(model.symbol+", " + model.exchange);

        return convertView;
    }

}