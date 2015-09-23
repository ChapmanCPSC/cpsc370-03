package edu.chapman.martin.stationmaster.adapters;

import android.content.ClipData;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import edu.chapman.martin.stationmaster.MainActivityFragment;
import edu.chapman.martin.stationmaster.R;
import edu.chapman.martin.stationmaster.models.TrainData;

/**
 * Created by Martin on 9/22/2015.
 */
public class CustomAdapter extends BaseAdapter{
    private LayoutInflater inflater;
    private List<TrainData> trainData;

    public CustomAdapter(Context context, List<TrainData> trainData){
        this.inflater = LayoutInflater.from(context);
        this.trainData = trainData;
    }

    @Override
    public int getCount()
    {
        return trainData.size();
    }

    @Override
    public Object getItem(int position)
    {
        return trainData.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View view;
        ViewHolder holder;

        if(convertView == null){
            view = inflater.inflate(R.layout.row_layout, parent, false);
            holder = new ViewHolder();
            holder.trainNo = (TextView)view.findViewById(R.id.tv_rowTrainNo);
            holder.dueAt = (TextView)view.findViewById(R.id.tv_rowDueAt);
            holder.status = (TextView)view.findViewById(R.id.tv_rowStatus);
            view.setTag(holder);
        } else{
            view = convertView;
            holder = (ViewHolder)view.getTag();
        }

        TrainData train = trainData.get(position);
        String trainno = train.trainno.trim();
        String scheduled = train.scheduled.trim();
        String remarks = train.remarks_noboarding.trim();
        holder.trainNo.setText(trainno);
        holder.dueAt.setText(scheduled);
        holder.status.setText(remarks);

        return view;
    }

    private class ViewHolder{
        public TextView trainNo, dueAt, status;
    }
}
