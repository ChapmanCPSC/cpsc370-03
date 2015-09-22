package edu.chapman.martin.stationmaster;

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
import android.widget.ScrollView;
import android.widget.TextView;

import edu.chapman.martin.stationmaster.models.StationStatusResultModel;
import edu.chapman.martin.stationmaster.models.TrainData;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment
{
    View rootView;
    EditText codeField;
    Button submitButton;
    ScrollView trainInfo;
    TextView trainNoLb;
    TextView dueAtLbl;

    public MainActivityFragment()
    {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        rootView = inflater.inflate(R.layout.fragment_main, container, false);

        codeField = (EditText) rootView.findViewById(R.id.et_stationCode);
        trainInfo = (ScrollView) rootView.findViewById(R.id.sv_trainInfo);
        trainNoLb = (TextView) rootView.findViewById(R.id.tv_trainNoLbl);
        dueAtLbl = (TextView) rootView.findViewById(R.id.tv_dueAtLbl);


        Button submitButton = (Button) rootView.findViewById(R.id.btn_submit);
        submitButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String stationCode = codeField.getText().toString();
                if (!stationCode.isEmpty())
                {
                    new StationInfoTask().execute(stationCode);
                }
            }
        });
        return rootView;
    }

    private class StationInfoTask extends AsyncTask<String, Void, StationStatusResultModel>
    {

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
            TrainData[] rowSource = result.response.results[0].data;
            LinearLayout trains = StationAPIWrapper.formatResults(rowSource, codeField.getContext(), rootView);
            trainInfo.addView(trains);

        }
    }
}
