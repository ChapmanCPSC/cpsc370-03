package edu.chapman.martin.stationmaster.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import edu.chapman.martin.stationmaster.R;
import edu.chapman.martin.stationmaster.activities.FavoritesActivity;
import edu.chapman.martin.stationmaster.adapters.StatusDisplayAdapter;
import edu.chapman.martin.stationmaster.db.FavoriteStationProvider;
import edu.chapman.martin.stationmaster.models.FavoriteStationModel;
import edu.chapman.martin.stationmaster.models.TrainData;
import edu.chapman.martin.stationmaster.tasks.StationInfoTask;

public class SearchFragment extends Fragment
{
    View rootView;
    EditText codeField;
    ListView trainInfo;
    TextView trainNoLb;
    TextView dueAtLbl;
    Context context;
    ProgressBar loadingWheel;
    TextView stationDisplay;
    TextView moreStationInfo;
    String stationCode;

    public SearchFragment()
    {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        rootView = inflater.inflate(R.layout.fragment_search, container, false);

        codeField = (EditText) rootView.findViewById(R.id.et_stationCode);
        trainInfo = (ListView) rootView.findViewById(R.id.lv_trainInfo);
        trainNoLb = (TextView) rootView.findViewById(R.id.tv_trainNoLbl);
        dueAtLbl = (TextView) rootView.findViewById(R.id.tv_dueAtLbl);
        loadingWheel = (ProgressBar) rootView.findViewById(R.id.pb_loadingWheel);
        stationDisplay = (TextView) rootView.findViewById(R.id.tv_stationDisplay);
        moreStationInfo = (TextView) rootView.findViewById(R.id.tv_moreStationInfo);


        Button submitButton = (Button) rootView.findViewById(R.id.btn_submit);
        submitButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                stationCode = codeField.getText().toString();
                if (!stationCode.isEmpty())
                {
                    //pre-execute code
                    loadingWheel.setVisibility(View.VISIBLE);
                    codeField.setEnabled(false);

                    //create the task
                    final StationInfoTask stationTask = new StationInfoTask(getActivity());

                    //create the runnable to execute after task finishes
                    stationTask.onFinish = new Runnable() {
                        @Override
                        public void run() {
                            try
                            {
                                TrainData[] rowSource = stationTask.result.response.results[0].data;
                                ArrayList<TrainData> trainDataList = new ArrayList<TrainData>();

                                for (TrainData train : rowSource)
                                {
                                    trainDataList.add(train);
                                }

                                StatusDisplayAdapter trainAdapter = new StatusDisplayAdapter(context, trainDataList);
                                trainInfo.setAdapter(trainAdapter);
                                trainInfo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        TrainData train = (TrainData) parent.getItemAtPosition(position);

                                        String alertMessage = "Origin: " + train.origin + "\n"
                                                + "Destination: " + train.destination + "\n"
                                                + "Remarks: " + train.remarks_noboarding;

                                        if(train.newtime.length() >=4) alertMessage += "\nEstimated time in: " + train.newtime;

                                        new AlertDialog.Builder(getActivity())
                                                .setTitle(train.service + " Train " + train.trainno)
                                                .setMessage(alertMessage)
                                                .setPositiveButton("OK", null)
                                                .show();
                                    }
                                });

                                //store a recent location in DB
                                FavoriteStationModel model = new FavoriteStationModel(stationCode);
                                FavoriteStationProvider.InsertFavoriteStation(model, getActivity());

                            } catch(NullPointerException e){
                                Toast.makeText(context, "No Results Found.  Please try another station code.", Toast.LENGTH_SHORT).show();
                                trainInfo.setAdapter(null);

                                codeField.setText("");
                                codeField.setEnabled(true);

                                stationDisplay.setText(R.string.stationDisplayDefault);

                                moreStationInfo.setVisibility(View.INVISIBLE);
                                loadingWheel.setVisibility(View.INVISIBLE);

                                return;
                            }

                            codeField.setEnabled(true);
                            codeField.setText("");
                            stationDisplay.setText("Next Trains Arriving At " + stationCode.toUpperCase() + ":");
                            moreStationInfo.setVisibility(View.VISIBLE);

                            moreStationInfo.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent browserIntent = new Intent(
                                            Intent.ACTION_VIEW,
                                            Uri.parse("http://m.amtrak.com/mt/www.amtrak.com/servlet/ContentServer?pagename=am/am2Station/Station_Page&code="+stationCode)
                                    );
                                    startActivity(browserIntent);
                                }
                            });

                            loadingWheel.setVisibility(View.INVISIBLE);
                        }
                    };

                    //execute the task
                    stationTask.execute(stationCode);
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

        Button viewFavorites = (Button) rootView.findViewById(R.id.btn_favorites);
        viewFavorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), FavoritesActivity.class);
                startActivity(i);
            }
        });

        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        context = getActivity().getApplicationContext();
        //StatusDisplayAdapter trainAdapter = new StatusDisplayAdapter(context, null);
    }
}
