package me.ablaz101.cpsc370.stockinfogetter.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.ListView;

import me.ablaz101.cpsc370.stockinfogetter.web.StockInfoAPIWrapper;
import me.ablaz101.cpsc370.stockinfogetter.adapters.LookupResultAdapter;
import me.ablaz101.cpsc370.stockinfogetter.models.LookupResultModel;

/**
 * Created by Xavi Ablaza on 9/19/15.
 */
public class LookupBackgroundTask extends AsyncTask<String, Void, LookupResultModel[]>
{
    public Context context;
    public ListView companyList;
    public ListView preferredStockList;
    public LookupBackgroundTask(Context context, ListView companyList, ListView preferredStockList) {
        this.context = context;
        this.companyList = companyList;
        this.preferredStockList = preferredStockList;
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
            companyList.setAdapter(new LookupResultAdapter(context, preferredStockList, lookupResults));
        }
    }
}
