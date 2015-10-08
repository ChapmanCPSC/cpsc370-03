package com.example.kelly.gasmeup.tasks;

import android.os.AsyncTask;

import com.example.kelly.gasmeup.wrappers.DirectionsAPIWrapper;
import com.example.kelly.gasmeup.MapsActivity;
import com.example.kelly.gasmeup.models.DirectionModel;

/**
 * Created by Kelly on 9/23/15.
 */
public class GetDistanceTask extends AsyncTask<String, Void, Void>{

    MapsActivity _maps;
    public Runnable onFinish;
    public DirectionModel result;

    public GetDistanceTask(MapsActivity maps){
        _maps = maps;
    }

    @Override
    protected Void doInBackground(String... params) {
        result = DirectionsAPIWrapper.GetDistance(params[0], params[1]);
        return null;
    }

    @Override
    protected void onPostExecute(Void params) {
        super.onPostExecute(params);
        //_maps.loadingFinished(directionModel);
        if(onFinish != null){
            onFinish.run();
        }

    }
}
