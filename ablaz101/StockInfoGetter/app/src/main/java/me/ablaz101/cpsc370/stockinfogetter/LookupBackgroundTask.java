package me.ablaz101.cpsc370.stockinfogetter;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.ListView;

import me.ablaz101.cpsc370.stockinfogetter.models.LookupResultModel;

/**
 * Created by Xavi Ablaza on 9/19/15.
 */
public class LookupBackgroundTask extends AsyncTask<String, Void, LookupResultModel[]>
{
    public Context context;
    public ListView listView;
    public LookupBackgroundTask(Context context, ListView listView) {
        this.context = context;
        this.listView = listView;
    }

    @Override
    protected LookupResultModel[] doInBackground(String... params) {
        LookupResultModel[] lookupResults =
                StockInfoAPIWrapper.GetLookupResults(params[0]);

        return lookupResults;
    }

    @Override
    protected void onPostExecute(LookupResultModel[] lookupResults) {
        // Populate ListView
        if (lookupResults != null) {
            listView.setAdapter(new LookupResultAdapter(context, lookupResults));
        }
    }
}
