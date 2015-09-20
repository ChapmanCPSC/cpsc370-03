package me.ablaz101.cpsc370.stockinfogetter;

import android.os.AsyncTask;
import android.widget.TextView;

import me.ablaz101.cpsc370.stockinfogetter.models.QuoteModel;

/**
 * Created by Xavi Ablaza on 9/19/15.
 */
public class QuoteBackgroundTask extends AsyncTask<String, Void, QuoteModel>
{
    public TextView hiView;
    public TextView lowView;
    public TextView openView;
    public QuoteBackgroundTask(TextView hiView, TextView lowView, TextView openView) {
        this.hiView = hiView;
        this.lowView = lowView;
        this.openView = openView;
    }

    @Override
    protected QuoteModel doInBackground(String... params) {
        QuoteModel qm = StockInfoAPIWrapper.GetStockQuote(params[0]);

        return qm;
    }

    @Override
    protected void onPostExecute(QuoteModel qm) {
        hiView.setText(String.valueOf(qm.High));
        lowView.setText(String.valueOf(qm.Low));
        openView.setText(String.valueOf(qm.Open));    }
}
