package com.example.kelly.gasmeup.adapters;

import android.content.Context;
import android.location.Location;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.kelly.gasmeup.MapsActivity;
import com.example.kelly.gasmeup.R;
import com.example.kelly.gasmeup.models.SaveLocationModel;
import com.example.kelly.gasmeup.tasks.GetDistanceTask;

import java.io.IOException;
import java.util.List;

/**
 * Created by Kelly on 10/7/15.
 */
public class SavedLocationAdapter extends ArrayAdapter<SaveLocationModel>{

    MapsActivity maps;
    List<SaveLocationModel> locations;
    Location _location;


    public SavedLocationAdapter(MapsActivity maps, List<SaveLocationModel> objects, Location location) {
        super(maps, R.layout.favorites_item, objects);
        this.maps = maps;
        this.locations = objects;
        this._location = location;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final SaveLocationModel location = locations.get(position);
        LayoutInflater inflater = LayoutInflater.from(maps);

        View row = inflater.inflate(R.layout.favorites_item, parent, false);

        final TextView locationName = (TextView) row.findViewById(R.id.tv_name);
        final TextView distTimeView = (TextView)row.findViewById(R.id.tv_dist_time);

        final GetDistanceTask distanceTask = new GetDistanceTask(maps);
        distanceTask.onFinish = new Runnable() {
            @Override
            public void run() {
                locationName.setText(location.getLocationName());
                //distTimeView.setText(location.getAddress());
                distTimeView.setText(distanceTask.result.routes[0].legs[0].distance.text
                        + " (" + distanceTask.result.routes[0].legs[0].duration.text + ")" );
            }
        };
        //distanceTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,
          //      location.getLocationName(), location.getAddress());

        String address;
        try {
            address = maps.getAddress(_location);
            address = address.replace(".", "").replace(",","").replace(" ", "+");
            //getDistanceTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, address, location.getAddress());
            distanceTask.execute(address, location.getAddress());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return row;
    }
}
