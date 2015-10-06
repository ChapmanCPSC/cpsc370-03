package me.ablaz101.cpsc370.stockinfogetter.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.TextView;

import me.ablaz101.cpsc370.stockinfogetter.web.StockInfoAPIWrapper;
import me.ablaz101.cpsc370.stockinfogetter.models.QuoteModel;

/**
 * Created by Xavi Ablaza on 9/19/15.
 */
public class QuoteBackgroundTask extends AsyncTask<String, Void, Void>
{
    Context _ctx;
    public Runnable onFinish;
    public QuoteModel _result;

    public QuoteBackgroundTask(Context ctx)
    {
        _ctx = ctx;
    }

    @Override
    protected Void doInBackground(String... params) {
        _result = StockInfoAPIWrapper.GetStockQuote(params[0]);
        return null;
    }

    @Override
    protected void onPostExecute(Void params) {
        super.onPostExecute(params);
        if (onFinish != null)
        {
            onFinish.run();
        }
    }
}
