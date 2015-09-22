package edu.chapman.martin.stationmaster;

import android.content.Context;
import android.os.AsyncTask;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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

    public static LinearLayout formatResults(TrainData[] trains, Context context, View rootView)
    {
        LinearLayout result = new LinearLayout(context);
        result.setOrientation(LinearLayout.VERTICAL);

        TextView trainNoLbl = (TextView) rootView.findViewById(R.id.tv_trainNoLbl);
        TextView dueAtLbl = (TextView) rootView.findViewById(R.id.tv_dueAtLbl);
        TextView statusLbl = (TextView) rootView.findViewById(R.id.tv_statusLbl);

        for (TrainData train : trains)
        {
            String trainNo = train.trainno.trim();
            String dueAt = train.scheduled.trim();
            String status = train.remarks_noboarding.trim();
            if(trainNo.length() > 0){
                LinearLayout parent = new LinearLayout(context);
                parent.setOrientation(LinearLayout.VERTICAL);

                LinearLayout child = new LinearLayout(context);
                child.setOrientation(LinearLayout.HORIZONTAL);

                TextView tv_trainNo = new TextView(context);
                TextView tv_dueAt = new TextView(context);
                TextView tv_status = new TextView(context);

                tv_trainNo.setWidth(trainNoLbl.getWidth());
                tv_dueAt.setWidth(dueAtLbl.getWidth());
                tv_status.setWidth(statusLbl.getWidth());

                tv_trainNo.setText(trainNo);
                tv_dueAt.setText(dueAt);
                tv_status.setText(status);

                tv_trainNo.setGravity(Gravity.CENTER_HORIZONTAL);
                tv_dueAt.setGravity(Gravity.CENTER_HORIZONTAL);
                tv_status.setGravity(Gravity.CENTER_HORIZONTAL);

                child.addView(tv_trainNo);
                child.addView(tv_dueAt);
                child.addView(tv_status);

                parent.addView(child);

                result.addView(parent);
            }
        }
        return result;
    }
}
