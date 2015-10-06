package com.example.cpsc.demoapplication.adapters;

import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.example.cpsc.demoapplication.R;
import com.example.cpsc.demoapplication.models.RecentLocationModel;
import com.example.cpsc.demoapplication.models.WeatherResultModel;
import com.example.cpsc.demoapplication.tasks.GetWeatherTask;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by ryanb on 10/5/2015.
 */
public class RecentLocationAdapter extends ArrayAdapter<RecentLocationModel>
{
    Context _ctx;
    List<RecentLocationModel> locations;

    public RecentLocationAdapter(Context context, List<RecentLocationModel> objects)
    {
        super(context, R.layout.location_item, objects);
        _ctx = context;
        locations = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        RecentLocationModel location = locations.get(position);

        LayoutInflater inflater = LayoutInflater.from(_ctx);

        View row = inflater.inflate(R.layout.location_item, parent, false);

        final TextView areaView = (TextView) row.findViewById(R.id.tv_area);
        final TextView tempView = (TextView) row.findViewById(R.id.tv_current_temp);
        final TextView hiView = (TextView) row.findViewById(R.id.tv_high);
        final TextView lowView = (TextView) row.findViewById(R.id.tv_low);

        final GetWeatherTask getWeatherTask = new GetWeatherTask(_ctx);
        getWeatherTask.onFinish = new Runnable()
        {
            @Override
            public void run()
            {
                areaView.setText(getWeatherTask._result.name);
                tempView.setText(String.valueOf(getWeatherTask._result.main.temp));
                hiView.setText(String.valueOf(getWeatherTask._result.main.temp_max));
                lowView.setText(String.valueOf(getWeatherTask._result.main.temp_min));
            }
        };
        getWeatherTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,location.getZip());

        return row;
    }
}
