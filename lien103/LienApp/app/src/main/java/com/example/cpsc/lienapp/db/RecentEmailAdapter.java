package com.example.cpsc.lienapp.db;

import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.cpsc.lienapp.R;
import com.example.cpsc.lienapp.tasks.GetEmailTask;

import java.util.List;
/**
 * Created by IsaacLien on 10/6/15.
 */
public class RecentEmailAdapter extends ArrayAdapter<RecentEmailModel>
{
    Context _ctx;
    List<RecentEmailModel> locations;

    public RecentEmailAdapter(Context context, List<RecentEmailModel> objects)
    {
        super(context, R.layout.email_item, objects);
        _ctx = context;
        locations = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        RecentEmailModel location = locations.get(position);

        LayoutInflater inflater = LayoutInflater.from(_ctx);

        View row = inflater.inflate(R.layout.email_item, parent, false);

        final TextView genderView = (TextView) row.findViewById(R.id.tv_area);
        final TextView nameView = (TextView) row.findViewById(R.id.tv_name);

        final GetEmailTask getEmailTask = new GetEmailTask(_ctx);
        getEmailTask.onFinish = new Runnable()
        {
            @Override
            public void run()
            {
                genderView.setText(getEmailTask._result.gender);
                nameView.setText(String.valueOf(getEmailTask._result.name));

            }
        };
        getEmailTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, location.getEmailAddress());

        return row;
    }
}
