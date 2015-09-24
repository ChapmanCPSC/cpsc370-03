package edu.chapman.martin.stationmaster;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import edu.chapman.martin.stationmaster.adapters.CustomAdapter;
import edu.chapman.martin.stationmaster.models.StationStatusResultModel;
import edu.chapman.martin.stationmaster.models.TrainData;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment
{
    View rootView;
    EditText codeField;
    ListView trainInfo;
    TextView trainNoLb;
    TextView dueAtLbl;
    Context context;
    ProgressBar loadingWheel;
    TextView stationDisplay;
    String stationCode;

    public MainActivityFragment()
    {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        rootView = inflater.inflate(R.layout.fragment_main, container, false);

        codeField = (EditText) rootView.findViewById(R.id.et_stationCode);
        trainInfo = (ListView) rootView.findViewById(R.id.lv_trainInfo);
        trainNoLb = (TextView) rootView.findViewById(R.id.tv_trainNoLbl);
        dueAtLbl = (TextView) rootView.findViewById(R.id.tv_dueAtLbl);
        loadingWheel = (ProgressBar) rootView.findViewById(R.id.pb_loadingWheel);
        stationDisplay = (TextView) rootView.findViewById(R.id.tv_stationDisplay);


        Button submitButton = (Button) rootView.findViewById(R.id.btn_submit);
        submitButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                stationCode = codeField.getText().toString();
                if (!stationCode.isEmpty())
                {
                    new StationInfoTask().execute(stationCode);

                }
            }
        });

        Button viewStationCodes = (Button) rootView.findViewById(R.id.btn_viewStationCodes);
        viewStationCodes.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.amtrak.com/html/stations_A.html"));
                startActivity(browserIntent);
            }
        });

        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        context = getActivity().getApplicationContext();
        //CustomAdapter trainAdapter = new CustomAdapter(context, null);
    }

    private class StationInfoTask extends AsyncTask<String, Void, StationStatusResultModel>
    {
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            loadingWheel.setVisibility(View.VISIBLE);
            codeField.setEnabled(false);
        }

        @Override
        protected StationStatusResultModel doInBackground(String... params)
        {

            try
            {
                StationStatusResultModel stationResult = StationAPIWrapper.GetArrivals(params[0], "PT");
                return stationResult;
            } catch (Exception e)
            {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(StationStatusResultModel result)
        {
            super.onPostExecute(result);

            try
            {
                TrainData[] rowSource = result.response.results[0].data;
                ArrayList<TrainData> trainDataList = new ArrayList<TrainData>();

                for (TrainData train : rowSource)
                {
                    trainDataList.add(train);
                }

                CustomAdapter trainAdapter = new CustomAdapter(context, trainDataList);
                trainInfo.setAdapter(trainAdapter);
            } catch(NullPointerException e){
                Toast.makeText(context, "No Results Found.  Please try another station code.", Toast.LENGTH_SHORT).show();
                trainInfo.setAdapter(null);
                codeField.setText("");
            }

            codeField.setEnabled(true);
            codeField.setText("");
            stationDisplay.setText("Next Trains Arriving At " + stationCode.toUpperCase() + ":");
            loadingWheel.setVisibility(View.INVISIBLE);

        }
    }
}
