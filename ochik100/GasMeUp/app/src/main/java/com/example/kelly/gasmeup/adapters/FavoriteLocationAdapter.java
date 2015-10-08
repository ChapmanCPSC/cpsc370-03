package com.example.kelly.gasmeup.adapters;

import android.location.Location;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.kelly.gasmeup.MapsActivity;
import com.example.kelly.gasmeup.R;
import com.example.kelly.gasmeup.models.FavoriteLocationModel;
import com.example.kelly.gasmeup.tasks.GetDistanceTask;

import java.io.IOException;
import java.util.List;

/**
 * Created by Kelly on 10/7/15.
 */
public class FavoriteLocationAdapter extends ArrayAdapter<FavoriteLocationModel>{

    MapsActivity maps;
    List<FavoriteLocationModel> locations;
    Location _location;

    public FavoriteLocationAdapter(MapsActivity maps, List<FavoriteLocationModel> objects) {
        super(maps, R.layout.favorites_item, objects);
        this.maps = maps;
        this.locations = objects;
    }

    public FavoriteLocationAdapter(MapsActivity maps, List<FavoriteLocationModel> objects, Location myLocation) {
        super(maps, R.layout.favorites_item, objects);
        this.maps = maps;
        this.locations = objects;
        this._location = myLocation;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final FavoriteLocationModel location = locations.get(position);

        LayoutInflater inflater = LayoutInflater.from(maps);


        View row = inflater.inflate(R.layout.favorites_item, parent, false);

        final TextView nameView = (TextView) row.findViewById(R.id.tv_name);
        final TextView distTimeView = (TextView)row.findViewById(R.id.tv_dist_time);

        final GetDistanceTask getDistanceTask = new GetDistanceTask(maps);
        getDistanceTask.onFinish = new Runnable() {
            @Override
            public void run() {
                nameView.setText(getDistanceTask.result.routes[0].legs[0].end_address);
                distTimeView.setText(getDistanceTask.result.routes[0].legs[0].distance.text
                        + " (" + getDistanceTask.result.routes[0].legs[0].duration.text + ")");

            }
        };

        //final String latlng = String.valueOf(_location.getLatitude()) + "," + String.valueOf(_location.getLongitude());
        String address;
        try {
            address = maps.getAddress(_location);
            address = address.replace(" ", "+");
            //getDistanceTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, address, location.getAddress());
            getDistanceTask.execute(address, location.getAddress());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return row;
    }
}
