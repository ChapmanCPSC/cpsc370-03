package me.ablaz101.cpsc370.stockinfogetter.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import me.ablaz101.cpsc370.stockinfogetter.R;
import me.ablaz101.cpsc370.stockinfogetter.db.StockInfoDataProvider;
import me.ablaz101.cpsc370.stockinfogetter.models.LookupResultModel;
import me.ablaz101.cpsc370.stockinfogetter.models.PreferredStockModel;
import me.ablaz101.cpsc370.stockinfogetter.models.QuoteModel;
import me.ablaz101.cpsc370.stockinfogetter.tasks.QuoteBackgroundTask;

public class PreferredStockAdapter extends BaseAdapter {

    Context context;
    List<PreferredStockModel> data;
    private static LayoutInflater inflater = null;

    public PreferredStockAdapter(Context context, List<PreferredStockModel> data) {
        this.context = context;
        this.data = data;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
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

        PreferredStockModel model = data.get(position);
        companyName.setText(model.getLookupResultModel().name);
        symbol.setText(model.getLookupResultModel().symbol+", " + model.getLookupResultModel().exchange);

        TextView quote = (TextView) convertView.findViewById(R.id.tv_quote);
        quote.setText(" High: " + model.getQuoteModel().High + ", Low: " + model.getQuoteModel().Low + ", Open: " + model.getQuoteModel().Open);
        quote.setVisibility(View.VISIBLE);

        Button btn = (Button) convertView.findViewById(R.id.btn_add);
        btn.setVisibility(View.GONE);

        return convertView;
    }

}