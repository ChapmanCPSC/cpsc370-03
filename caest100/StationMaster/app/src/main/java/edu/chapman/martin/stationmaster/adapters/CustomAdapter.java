package edu.chapman.martin.stationmaster.adapters;

import android.content.ClipData;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import edu.chapman.martin.stationmaster.MainActivityFragment;
import edu.chapman.martin.stationmaster.R;

/**
 * Created by Martin on 9/22/2015.
 */
public class CustomAdapter extends BaseAdapter
{
    Context context;
    public CustomAdapter(Context context){
        this.context = context;
    }

    @Override
    public int getCount()
    {
        return 0;
    }

    @Override
    public Object getItem(int position)
    {
        return null;
    }

    @Override
    public long getItemId(int position)
    {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View v = convertView;

        if(v == null){
            LayoutInflater vi;
            vi = LayoutInflater.from(context);
            v = vi.inflate(R.layout.lvlayout, null);
        }

        Object p = getItem(position);

        if(p != null){
            TextView trainNo = (TextView) v.findViewById(R.id.tv_rowTrainNo);
            TextView dueAt = (TextView) v.findViewById(R.id.tv_rowDueAt);
            TextView status = (TextView) v.findViewById(R.id.tv_rowStatus);

            if(trainNo != null){
                trainNo.setText("");
            }
        }

        return null;
    }

    @Override
    public void notifyDataSetChanged()
    {
        super.notifyDataSetChanged();
    }
}
