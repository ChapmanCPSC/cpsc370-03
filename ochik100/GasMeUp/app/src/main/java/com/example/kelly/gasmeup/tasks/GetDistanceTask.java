package com.example.kelly.gasmeup.tasks;

import android.os.AsyncTask;

import com.example.kelly.gasmeup.wrappers.DirectionsAPIWrapper;
import com.example.kelly.gasmeup.MapsActivity;
import com.example.kelly.gasmeup.models.DirectionModel;

/**
 * Created by Kelly on 9/23/15.
 */
public class GetDistanceTask extends AsyncTask<String, Void, DirectionModel>{

    MapsActivity _maps;

    public GetDistanceTask(MapsActivity maps){
        _maps = maps;
    }

    @Override
    protected DirectionModel doInBackground(String... params) {
        DirectionModel model = DirectionsAPIWrapper.GetDistance(params[0], params[1]);
        return model;
    }

    @Override
    protected void onPostExecute(DirectionModel directionModel) {
        super.onPostExecute(directionModel);
        _maps.loadingFinished(directionModel);

    }
}
