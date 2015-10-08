package edu.chapman.martin.stationmaster;

import android.content.Context;
import android.os.AsyncTask;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import org.w3c.dom.Text;

import java.util.ArrayList;

import edu.chapman.martin.stationmaster.models.StationStatusResultModel;
import edu.chapman.martin.stationmaster.models.TrainData;

/**
 * Created by Martin on 9/21/2015.
 */
public class StationAPIWrapper
{

    private static String baseurl = "http://dixielandsoftware.net/Amtrak/solari/data/";

    public static StationStatusResultModel GetArrivals(String stationCode, String timeZone)
    {
        stationCode = stationCode.toUpperCase();
        timeZone = timeZone.toUpperCase();

        String method = stationCode + "_schedule.php";
        String queryString = "?data=" + stationCode + "&tz=" + timeZone;
        String fullURL = baseurl + method + queryString;

        String response = "";

        try
        {
            response = new WebRequest(fullURL).get();
            StationStatusResultModel model = new Gson().fromJson(response, StationStatusResultModel.class);
            return model;

        } catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public static void formatResults(TrainData[] trains, Context context, View rootView)
    {
        TextView trainNoLbl = (TextView) rootView.findViewById(R.id.tv_trainNoLbl);
        TextView dueAtLbl = (TextView) rootView.findViewById(R.id.tv_dueAtLbl);
        TextView statusLbl = (TextView) rootView.findViewById(R.id.tv_statusLbl);

        for (TrainData train : trains)
        {
            String trainNo = train.trainno.trim();
            String dueAt = train.scheduled.trim();
            String status = train.remarks_noboarding.trim();

            if(trainNo.length() > 0){
                LayoutInflater inflater = (LayoutInflater)   context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                View child = inflater.inflate(R.layout.lvlayout, null);

                TextView tv_trainNo = (TextView) rootView.findViewById(R.id.tv_rowTrainNo);
                TextView tv_dueAt = (TextView) rootView.findViewById(R.id.tv_rowDueAt);
                TextView tv_status = (TextView) rootView.findViewById(R.id.tv_rowStatus);

                tv_trainNo.setText(trainNo);
                tv_dueAt.setText(dueAt);
                tv_status.setText(status);

                ListView ll_main = (ListView) rootView.findViewById(R.id.lv_trainInfo);
                ll_main.addView(child);
            }
        }
    }
}
