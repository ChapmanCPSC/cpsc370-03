package edu.chapman.martin.stationmaster.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

import edu.chapman.martin.stationmaster.StationAPIWrapper;
import edu.chapman.martin.stationmaster.adapters.StatusDisplayAdapter;
import edu.chapman.martin.stationmaster.models.StationStatusResultModel;
import edu.chapman.martin.stationmaster.models.TrainData;

/**
 * Created by Martin on 10/6/2015.
 */
public class StationInfoTask extends AsyncTask<String, Void, Void>
{
    Context ctx;
    public Runnable onFinish;
    public StationStatusResultModel result;

    public StationInfoTask(Context ctx){
        this.ctx = ctx;
    }
    @Override
    protected void onPreExecute()
    {
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(String... params)
    {

        try
        {
            result = StationAPIWrapper.GetArrivals(params[0], "PT");
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void params)
    {
        super.onPostExecute(params);

        if(onFinish != null){
            onFinish.run();
        }

    }
}
