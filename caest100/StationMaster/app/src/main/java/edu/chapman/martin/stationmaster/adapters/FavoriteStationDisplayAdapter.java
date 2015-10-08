package edu.chapman.martin.stationmaster.adapters;

import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import edu.chapman.martin.stationmaster.R;
import edu.chapman.martin.stationmaster.db.FavoriteStationProvider;
import edu.chapman.martin.stationmaster.models.FavoriteStationModel;
import edu.chapman.martin.stationmaster.models.TrainData;
import edu.chapman.martin.stationmaster.tasks.StationInfoTask;

/**
 * Created by Martin on 10/6/2015.
 */
public class FavoriteStationDisplayAdapter extends ArrayAdapter<FavoriteStationModel> {

    Context context;
    List<FavoriteStationModel> favorites;
    LayoutInflater inflater;

    public FavoriteStationDisplayAdapter(Context context, List<FavoriteStationModel> favorites) {
        super(context, R.layout.row_favorite,favorites);

        this.context = context;
        this.favorites = favorites;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        final FavoriteStationModel favorite = favorites.get(position);

        View rootView = inflater.inflate(R.layout.row_favorite, parent, false);

        final TextView tv_stationCode = (TextView) rootView.findViewById(R.id.tv_stationCode);
        final ListView trainInfo = (ListView) rootView.findViewById(R.id.lv_favoriteStationTrains);

        final StationInfoTask task = new StationInfoTask(context);
        task.onFinish = new Runnable() {
            @Override
            public void run() {
                String code = favorite.getCode();
                tv_stationCode.setText(code.toUpperCase());
                
                //// TODO: 10/7/2015 get train information with asynctask using the station code from favorite
                try
                {
                    TrainData[] rowSource = task.result.response.results[0].data;
                    ArrayList<TrainData> trainDataList = new ArrayList<TrainData>();

                    for (TrainData train : rowSource)
                    {
                        trainDataList.add(train);
                    }

                    StatusDisplayAdapter trainAdapter = new StatusDisplayAdapter(context, trainDataList);
                    trainInfo.setAdapter(trainAdapter);

                } catch(NullPointerException e){
                    Toast.makeText(context, "An error occurred getting station information at " + code + ".", Toast.LENGTH_SHORT).show();
                }
            }
        };

        task.execute(favorite.getCode());

        return rootView;
    }
}
