package me.ablaz101.cpsc370.stockinfogetter.adapters;

import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import me.ablaz101.cpsc370.stockinfogetter.MainActivity;
import me.ablaz101.cpsc370.stockinfogetter.R;
import me.ablaz101.cpsc370.stockinfogetter.db.StockInfoDataProvider;
import me.ablaz101.cpsc370.stockinfogetter.models.LookupResultModel;
import me.ablaz101.cpsc370.stockinfogetter.models.PreferredStockModel;
import me.ablaz101.cpsc370.stockinfogetter.models.QuoteModel;
import me.ablaz101.cpsc370.stockinfogetter.tasks.QuoteBackgroundTask;

public class LookupResultAdapter extends BaseAdapter {

    Context context;
    ListView preferredStockList;
    LookupResultModel[] data;
    private static LayoutInflater inflater = null;

    public LookupResultAdapter(Context context, ListView preferredStockList, LookupResultModel[] data) {
        this.context = context;
        this.preferredStockList = preferredStockList;
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
        final TextView symbol = (TextView) convertView.findViewById(R.id.tv_symbol);

        final LookupResultModel model = data[position];
        companyName.setText(model.name);
        symbol.setText(model.symbol+", " + model.exchange);

        Button add = (Button) convertView.findViewById(R.id.btn_add);
        add.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {

                // TODO Construct QuoteModel (API Call)
                final QuoteBackgroundTask quoteTask = new QuoteBackgroundTask(context);
                quoteTask.onFinish = new Runnable()
                {
                    @Override
                    public void run()
                    {
                        // Construct LookupResultModel (From View)
                        LookupResultModel lookupResultModel = new LookupResultModel(
                                model.symbol, model.name, model.exchange
                        );

                        // Construct PreferredStockModel
                        PreferredStockModel preferredStockModel = new PreferredStockModel(lookupResultModel, quoteTask._result);
                        System.out.println(quoteTask._result.High);

                        // Insert into Database
                        StockInfoDataProvider.InsertPreferredStockEntry(preferredStockModel, context);

                        // TODO Update List
                        List<PreferredStockModel> preferredStockModels = StockInfoDataProvider.GetPreferredStocks(context);
                        preferredStockList.setAdapter(new PreferredStockAdapter(context, preferredStockModels));
                    }
                };
                quoteTask.execute(model.symbol);
            }
        });

        return convertView;
    }

}